package com.puce.apimentejuego.services

import com.puce.apimentejuego.exceptions.CategoryNotFoundException
import com.puce.apimentejuego.exceptions.GameNotFoundException
import com.puce.apimentejuego.exceptions.MissingParameterException
import com.puce.apimentejuego.exceptions.QuestionIdNotFoundException
import com.puce.apimentejuego.exceptions.QuestionOptionNotFoundException
import com.puce.apimentejuego.exceptions.UserNotFoundException
import com.puce.apimentejuego.mappers.GameMapper
import com.puce.apimentejuego.models.requests.GameRequest
import com.puce.apimentejuego.models.responses.GameResponse
import com.puce.apimentejuego.repositories.CategoryRepository
import com.puce.apimentejuego.repositories.GameRepository
import com.puce.apimentejuego.repositories.UserRepository
import org.springframework.stereotype.Service
import com.puce.apimentejuego.models.entities.GameAnswer
import com.puce.apimentejuego.models.entities.QuestionOption
import com.puce.apimentejuego.models.requests.SubmitAnswersRequest
import com.puce.apimentejuego.models.responses.GameResultResponse
import com.puce.apimentejuego.repositories.GameAnswerRepository
import com.puce.apimentejuego.repositories.QuestionOptionRepository
import com.puce.apimentejuego.repositories.QuestionRepository
import org.springframework.transaction.annotation.Transactional

@Service
class GameService(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val gameMapper: GameMapper,
    private val gameAnswerRepository: GameAnswerRepository,
    private val questionOptionRepository: QuestionOptionRepository,
    private val questionRepository: QuestionRepository
) {


    // Inicia un nuevo juego o devuelve uno existente para el usuario y categoría dados
    fun startOrGetGame(request: GameRequest): GameResponse {
        // Validar campos requeridos
        if (request.userId == null) {
            throw MissingParameterException("Field 'user_id' is required")
        }
        if (request.categoryId == null) {
            throw MissingParameterException("Field 'category_id' is required")
        }

        val existingGame = gameRepository.findByUserIdAndCategoryId(request.userId, request.categoryId)

        if (existingGame.isPresent) {
            return gameMapper.toResponse(existingGame.get())
        }

        val user = userRepository.findById(request.userId)
            .orElseThrow { UserNotFoundException("User with ID ${request.userId} not found") }
        val category = categoryRepository.findById(request.categoryId)
            .orElseThrow { CategoryNotFoundException("Category with ID ${request.categoryId} not found") }

        val newGame = gameMapper.toEntity(user, category)
        val savedGame = gameRepository.save(newGame)

        return gameMapper.toResponse(savedGame)
    }

    @Transactional
    fun submitAnswers(request: SubmitAnswersRequest): GameResultResponse {
        // Validar campos requeridos
        if (request.gameId == null) {
            throw MissingParameterException("Field 'game_id' is required")
        }
        if (request.answers == null) {
            throw MissingParameterException("Field 'answers' is required")
        }

        val game = gameRepository.findById(request.gameId)
            .orElseThrow { GameNotFoundException("Game with ID ${request.gameId} not found") }


        var currentScore = 0
        var correctCount = 0
        var incorrectCount = 0
        var unansweredCount = 0

        val answersToSave = mutableListOf<GameAnswer>()

        for (answer in request.answers) {
            // Validar que cada respuesta tenga questionId
            if (answer.questionId == null) {
                throw MissingParameterException("Field 'question_id' is required in each answer")
            }

            val question = questionRepository.findById(answer.questionId)
                .orElseThrow { QuestionIdNotFoundException("Question with ID ${answer.questionId} not found") }

            var isCorrect = false
            var selectedOption: QuestionOption? = null

            if (answer.selectedOptionId != null) {
                val option = questionOptionRepository.findById(answer.selectedOptionId)
                    .orElseThrow { QuestionOptionNotFoundException("Option with ID ${answer.selectedOptionId} not found") }

                // Validar que la opción pertenezca a la pregunta
                if (option.question.id != question.id) {
                    throw IllegalArgumentException("Option ${answer.selectedOptionId} does not belong to question ${answer.questionId}")
                }

                selectedOption = option
                if (option.isCorrect) {
                    isCorrect = true
                    currentScore += 1 // Sumar puntos por respuesta correcta (puedes ajustar este valor)
                    correctCount++
                } else {
                    currentScore -= 2 // Restar 1 punto por respuesta incorrecta
                    incorrectCount++
                }
            } else {
                // No respondió (se acabó el tiempo o saltó la pregunta)
                currentScore -= 2 // Restar 1 punto por no responder
                unansweredCount++
            }

            answersToSave.add(
                GameAnswer(
                    game = game,
                    question = question,
                    selectedOption = selectedOption,
                    isCorrect = isCorrect
                )
            )
        }


        // Guardar respuestas
        gameAnswerRepository.saveAll(answersToSave)


        // Actualizar juego
        // 1. Sumamos el puntaje actual al acumulado histórico
        game.score += currentScore



        // 2. Si el resultado total bajó de 0, lo reseteamos a 0 para no manejar numeros negativos
        if (game.score < 0) {
            game.score = 0
        }
        gameRepository.save(game)

        return GameResultResponse(
            gameId = game.id,
            totalScore = game.score,
            correctAnswers = correctCount,
            incorrectAnswers = incorrectCount,
            unanswered = unansweredCount
        )
    }

    fun getScoresByCategories(userId: Long, categoryIds: List<Long>): List<GameResponse> {
        val games = gameRepository.findByUserIdAndCategoryIdIn(userId, categoryIds)
        return games.map { gameMapper.toResponse(it) }
    }
}
