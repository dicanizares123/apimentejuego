package com.puce.apimentejuego.services

import com.puce.apimentejuego.mappers.QuestionOptionMapper
import com.puce.apimentejuego.models.entities.Category
import com.puce.apimentejuego.models.entities.Option
import com.puce.apimentejuego.models.entities.Question
import com.puce.apimentejuego.models.entities.QuestionOption
import com.puce.apimentejuego.models.requests.QuestionOptionRequest
import com.puce.apimentejuego.models.responses.QuestionOptionResponse
import com.puce.apimentejuego.models.responses.QuestionWithOptionsResponse
import com.puce.apimentejuego.repositories.CategoryRepository
import com.puce.apimentejuego.repositories.OptionRepository
import com.puce.apimentejuego.repositories.QuestionOptionRepository
import com.puce.apimentejuego.repositories.QuestionRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import java.util.Optional

class QuestionOptionServiceTest {

    // 1. Mocks
    private lateinit var questionOptionRepository: QuestionOptionRepository
    private lateinit var questionRepository: QuestionRepository
    private lateinit var optionRepository: OptionRepository
    private lateinit var questionOptionMapper: QuestionOptionMapper
    private lateinit var categoryRepository: CategoryRepository

    // 2. Servicio
    private lateinit var questionOptionService: QuestionOptionService

    @BeforeEach
    fun init() {
        questionOptionRepository = mock(QuestionOptionRepository::class.java)
        questionRepository = mock(QuestionRepository::class.java)
        optionRepository = mock(OptionRepository::class.java)
        questionOptionMapper = mock(QuestionOptionMapper::class.java)
        categoryRepository = mock(CategoryRepository::class.java)

        questionOptionService = QuestionOptionService(
            questionOptionRepository,
            questionRepository,
            optionRepository,
            questionOptionMapper,
            categoryRepository
        )
    }

    // --- HELPER METHODS ---

    private fun createCategoryDummy(id: Long): Category {
        return Category(
            title = "Cat", description = "Desc", shortDescription = "Short",
            slug = "slug", difficulty = "Easy", duration_in_minutes = 10,
            questionsPerGame = 5
        ).apply { this.id = id }
    }

    private fun createQuestionDummy(id: Long, category: Category): Question {
        return Question(
            question = "¿Pregunta $id?", category = category,
            explanation = "Exp", isActive = true
        ).apply { this.id = id }
    }

    private fun createOptionDummy(id: Long): Option {
        return Option(possibleAnswer = "R: $id").apply { this.id = id }
    }

    private fun createQuestionOptionDummy(id: Long, question: Question, option: Option, correct: Boolean): QuestionOption {
        return QuestionOption(
            question = question,
            option = option,
            isCorrect = correct
        ).apply { this.id = id }
    }

    // --- TEST: CREATE ---
    @Test
    fun `SHOULD save a questionOption GIVEN valid request`() {
        val request = QuestionOptionRequest(1L, 2L, true)

        val category = createCategoryDummy(1L)
        val question = createQuestionDummy(1L, category)
        val option = createOptionDummy(2L)

        val entity = createQuestionOptionDummy(10L, question, option, true)
        val response = QuestionOptionResponse(10L, 1L, 2L, true)

        `when`(questionRepository.findById(1L)).thenReturn(Optional.of(question))
        `when`(optionRepository.findById(2L)).thenReturn(Optional.of(option))
        `when`(questionOptionMapper.toEntity(request, question, option)).thenReturn(entity)
        `when`(questionOptionRepository.save(entity)).thenReturn(entity)
        `when`(questionOptionMapper.toResponse(entity)).thenReturn(response)

        val result = questionOptionService.save(request)

        assertEquals(10L, result.id)
        verify(questionOptionRepository).save(entity)
    }

    // --- TEST: FIND BY ID ---
    @Test
    fun `SHOULD return response GIVEN valid id`() {
        val id = 10L
        val category = createCategoryDummy(1L)
        val question = createQuestionDummy(1L, category)
        val option = createOptionDummy(2L)
        val entity = createQuestionOptionDummy(id, question, option, false)
        val response = QuestionOptionResponse(id, 1L, 2L, false)

        `when`(questionOptionRepository.findById(id)).thenReturn(Optional.of(entity))
        `when`(questionOptionMapper.toResponse(entity)).thenReturn(response)

        val result = questionOptionService.findById(id)

        assertEquals(id, result.id)
    }

    // --- TEST: UPDATE (Cambio de Relaciones) ---
    @Test
    fun `SHOULD update question and option references GIVEN request with different ids`() {
        val id = 10L
        // IDs Nuevos en el Request
        val request = QuestionOptionRequest(questionId = 99L, optionId = 88L, isCorrect = false)

        val category = createCategoryDummy(1L)
        // Datos Antiguos
        val oldQuestion = createQuestionDummy(1L, category)
        val oldOption = createOptionDummy(2L)
        val entity = createQuestionOptionDummy(id, oldQuestion, oldOption, true)

        // Datos Nuevos
        val newQuestion = createQuestionDummy(99L, category)
        val newOption = createOptionDummy(88L)

        `when`(questionOptionRepository.findById(id)).thenReturn(Optional.of(entity))
        `when`(questionRepository.findById(99L)).thenReturn(Optional.of(newQuestion))
        `when`(optionRepository.findById(88L)).thenReturn(Optional.of(newOption))
        `when`(questionOptionRepository.save(any(QuestionOption::class.java))).thenReturn(entity)

        val response = QuestionOptionResponse(id, 99L, 88L, false)
        `when`(questionOptionMapper.toResponse(entity)).thenReturn(response)

        questionOptionService.update(id, request)

        // Verificamos que buscó los nuevos IDs
        verify(questionRepository).findById(99L)
        verify(optionRepository).findById(88L)
    }

    // --- TEST: DELETE ---
    @Test
    fun `SHOULD delete entity GIVEN valid id`() {
        val id = 10L
        `when`(questionOptionRepository.existsById(id)).thenReturn(true)

        questionOptionService.deleteById(id)

        verify(questionOptionRepository).deleteById(id)
    }

    // --- TEST: GET QUESTIONS WITH OPTIONS ---
    @Test
    fun `SHOULD return randomized questions with options GIVEN valid category`() {
        val categoryId = 1L
        // Categoría pide 2 preguntas por juego
        val category = createCategoryDummy(categoryId).apply { questionsPerGame = 2 }

        // Simulamos que en la BD hay 3 preguntas disponibles
        val q1 = createQuestionDummy(1L, category)
        val q2 = createQuestionDummy(2L, category)
        val q3 = createQuestionDummy(3L, category)

        // Agregamos opciones a la pregunta 1 para verificar el mapeo interno
        val opt = createOptionDummy(100L)
        val qo = createQuestionOptionDummy(10L, q1, opt, true)
        q1.questionOptions = mutableListOf(qo) // Simulamos la relación OneToMany

        val allQuestions = listOf(q1, q2, q3)

        `when`(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category))
        `when`(questionRepository.findByCategoryId(categoryId)).thenReturn(allQuestions)

        val result = questionOptionService.getQuestionsWithOptions(categoryId)

        // VERIFICACIÓN:
        // 1. Debe devolver máximo 2 preguntas (según questionsPerGame)
        assertTrue(result.size <= 2)

        // 2. Si salió la pregunta 1, verificamos que tenga sus opciones mapeadas
        val resultQ1 = result.find { it.questionId == 1L }
        if (resultQ1 != null) {
            assertEquals(1, resultQ1.options.size)
            assertEquals("R: 100", resultQ1.options[0].possibleAnswer)
        }
    }
}