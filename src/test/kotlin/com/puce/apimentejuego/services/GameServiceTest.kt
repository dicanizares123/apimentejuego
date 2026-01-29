package com.puce.apimentejuego.services

import com.puce.apimentejuego.mappers.GameMapper
import com.puce.apimentejuego.models.entities.*
import com.puce.apimentejuego.models.requests.GameRequest
import com.puce.apimentejuego.models.requests.SubmitAnswersRequest
import com.puce.apimentejuego.models.requests.AnswerSubmission
import com.puce.apimentejuego.models.responses.GameResponse
import com.puce.apimentejuego.repositories.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.*
import java.util.Optional
import java.time.LocalDateTime

class GameServiceTest {

    private lateinit var gameRepository: GameRepository
    private lateinit var userRepository: UserRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var gameMapper: GameMapper
    private lateinit var gameAnswerRepository: GameAnswerRepository
    private lateinit var questionOptionRepository: QuestionOptionRepository
    private lateinit var questionRepository: QuestionRepository

    private lateinit var gameService: GameService

    @BeforeEach
    fun init() {
        // Inicialización de todos los mocks (dependencias falsas)
        gameRepository = mock(GameRepository::class.java)
        userRepository = mock(UserRepository::class.java)
        categoryRepository = mock(CategoryRepository::class.java)
        gameMapper = mock(GameMapper::class.java)
        gameAnswerRepository = mock(GameAnswerRepository::class.java)
        questionOptionRepository = mock(QuestionOptionRepository::class.java)
        questionRepository = mock(QuestionRepository::class.java)

        gameService = GameService(
            gameRepository,
            userRepository,
            categoryRepository,
            gameMapper,
            gameAnswerRepository,
            questionOptionRepository,
            questionRepository
        )
    }

    // --- HELPER METHODS---

    private fun createUserDummy(id: Long): User {
        return User(
            firstName = "Test",
            lastName = "User",
            email = "test@mail.com",
            password = "123",
            username = "testuser"
        ).apply { this.id = id }
    }

    private fun createCategoryDummy(id: Long): Category {
        return Category(
            title = "General",
            description = "General Knowledge",
            shortDescription = "Gen",
            slug = "general",
            difficulty = "Easy",
            duration_in_minutes = 10,
            questionsPerGame = 5
        ).apply { this.id = id }
    }

    private fun createQuestionDummy(id: Long, category: Category): Question {
        return Question(
            question = "¿Pregunta de prueba $id?",
            category = category,
            explanation = "Explicación genérica",
            isActive = true
        ).apply { this.id = id }
    }

    private fun createOptionDummy(id: Long, question: Question, correct: Boolean): QuestionOption {
        // Creamos la entidad Option primero
        val optionEntity = Option(
            possibleAnswer = "Respuesta Posible $id"
        ).apply { this.id = id }

        return QuestionOption(
            option = optionEntity,
            isCorrect = correct,
            question = question
        ).apply { this.id = id }
    }

    // --- TESTS DE INICIO DE JUEGO ---

    // TEST 1: Verificar que crea un juego nuevo si no existe uno previo
    @Test
    fun `SHOULD create a new game GIVEN it does not exist`() {
        val request = GameRequest(userId = 1L, categoryId = 1L)

        val user = createUserDummy(1L)
        val category = createCategoryDummy(1L)

        val newGame = Game(user = user, category = category)
        val savedGame = Game(user = user, category = category).apply { id = 10L }
        val expectedResponse = GameResponse(10L, 1L, 1L, 0, LocalDateTime.now(), null)

        `when`(gameRepository.findByUserIdAndCategoryId(1L, 1L)).thenReturn(Optional.empty())
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))
        `when`(categoryRepository.findById(1L)).thenReturn(Optional.of(category))
        `when`(gameMapper.toEntity(user, category)).thenReturn(newGame)
        `when`(gameRepository.save(newGame)).thenReturn(savedGame)
        `when`(gameMapper.toResponse(savedGame)).thenReturn(expectedResponse)

        val result = gameService.startOrGetGame(request)

        assertEquals(10L, result.id)
        verify(gameRepository, times(1)).save(newGame)
    }

    // TEST 2: Verificar que devuelve el juego existente (sin crear duplicados)
    @Test
    fun `SHOULD return existing game GIVEN it already exists`() {
        val request = GameRequest(userId = 1L, categoryId = 1L)
        val user = createUserDummy(1L)
        val category = createCategoryDummy(1L)

        val existingGame = Game(user, category).apply { id = 55L }
        val expectedResponse = GameResponse(55L, 1L, 1L, 0, LocalDateTime.now(), null)

        `when`(gameRepository.findByUserIdAndCategoryId(1L, 1L)).thenReturn(Optional.of(existingGame))
        `when`(gameMapper.toResponse(existingGame)).thenReturn(expectedResponse)

        val result = gameService.startOrGetGame(request)

        assertEquals(55L, result.id)
        verify(gameRepository, never()).save(any(Game::class.java))
    }

    // TEST 3: Verificar manejo de errores (Usuario no encontrado)
    @Test
    fun `SHOULD throw NoSuchElementException when user not found`() {
        val request = GameRequest(userId = 99L, categoryId = 1L)
        `when`(gameRepository.findByUserIdAndCategoryId(99L, 1L)).thenReturn(Optional.empty())
        `when`(userRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(NoSuchElementException::class.java) {
            gameService.startOrGetGame(request)
        }
    }

    // --- TESTS DE ENVÍO DE RESPUESTAS (LÓGICA MATEMÁTICA) ---

    // TEST 4: Verificar cálculo de puntos (Bien +1, Mal -2, Nula -2)
    @Test
    fun `SHOULD calculate score correctly (Correct +1, Incorrect -2, Null -2)`() {
        val gameId = 100L
        val user = createUserDummy(1L)
        val category = createCategoryDummy(1L)
        val game = Game(user, category, score = 10).apply { id = gameId }

        val q1 = createQuestionDummy(1L, category)
        val q2 = createQuestionDummy(2L, category)
        val q3 = createQuestionDummy(3L, category)

        val optCorrect = createOptionDummy(10L, q1, true)
        val optIncorrect = createOptionDummy(20L, q2, false)

        val answer1 = AnswerSubmission(questionId = 1L, selectedOptionId = 10L)
        val answer2 = AnswerSubmission(questionId = 2L, selectedOptionId = 20L)
        val answer3 = AnswerSubmission(questionId = 3L, selectedOptionId = null)

        val request = SubmitAnswersRequest(gameId, listOf(answer1, answer2, answer3))

        `when`(gameRepository.findById(gameId)).thenReturn(Optional.of(game))

        `when`(questionRepository.findById(1L)).thenReturn(Optional.of(q1))
        `when`(questionRepository.findById(2L)).thenReturn(Optional.of(q2))
        `when`(questionRepository.findById(3L)).thenReturn(Optional.of(q3))

        `when`(questionOptionRepository.findById(10L)).thenReturn(Optional.of(optCorrect))
        `when`(questionOptionRepository.findById(20L)).thenReturn(Optional.of(optIncorrect))

        val result = gameService.submitAnswers(request)

        assertEquals(7, result.totalScore)
        verify(gameRepository).save(game)
    }

    // TEST 5: Verificar que el puntaje nunca sea negativo (Reset a 0)
    @Test
    fun `SHOULD reset score to 0 if result is negative`() {
        val user = createUserDummy(1L)
        val category = createCategoryDummy(1L)
        val game = Game(user, category, score = 0).apply { id = 1L }

        val q1 = createQuestionDummy(1L, category)
        val optIncorrect = createOptionDummy(2L, q1, false)

        val answer = AnswerSubmission(questionId = 1L, selectedOptionId = 2L)
        val request = SubmitAnswersRequest(1L, listOf(answer))

        `when`(gameRepository.findById(1L)).thenReturn(Optional.of(game))
        `when`(questionRepository.findById(1L)).thenReturn(Optional.of(q1))
        `when`(questionOptionRepository.findById(2L)).thenReturn(Optional.of(optIncorrect))

        val result = gameService.submitAnswers(request)

        assertEquals(0, result.totalScore)
    }

    // TEST 6: Verificar integridad
    @Test
    fun `SHOULD throw IllegalArgumentException if option does not belong to question`() {
        val user = createUserDummy(1L)
        val category = createCategoryDummy(1L)
        val game = Game(user, category).apply { id = 1L }

        val q1 = createQuestionDummy(1L, category)
        val q2 = createQuestionDummy(2L, category)

        val optionOfQ2 = createOptionDummy(99L, q2, true)

        val answer = AnswerSubmission(questionId = 1L, selectedOptionId = 99L)
        val request = SubmitAnswersRequest(1L, listOf(answer))

        `when`(gameRepository.findById(1L)).thenReturn(Optional.of(game))
        `when`(questionRepository.findById(1L)).thenReturn(Optional.of(q1))
        `when`(questionOptionRepository.findById(99L)).thenReturn(Optional.of(optionOfQ2))

        assertThrows(IllegalArgumentException::class.java) {
            gameService.submitAnswers(request)
        }
    }

    // --- TEST DE OBTENCIÓN DE PUNTAJES ---

    // TEST 7: Verificar listado de puntajes
    @Test
    fun `SHOULD return list of game scores`() {
        val user = createUserDummy(1L)
        val category = createCategoryDummy(1L)
        val game = Game(user, category)
        val response = GameResponse(1L, 1L, 1L, 100, LocalDateTime.now(), null)

        `when`(gameRepository.findByUserIdAndCategoryIdIn(anyLong(), anyList()))
            .thenReturn(listOf(game))
        `when`(gameMapper.toResponse(game)).thenReturn(response)

        val result = gameService.getScoresByCategories(1L, listOf(1L, 2L))

        assertEquals(1, result.size)
        assertEquals(100, result[0].score)
    }
}