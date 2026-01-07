package com.puce.apimentejuego.services

import com.puce.apimentejuego.mappers.GameMapper
import com.puce.apimentejuego.models.requests.GameRequest
import com.puce.apimentejuego.models.responses.GameResponse
import com.puce.apimentejuego.repositories.CategoyRepositoy
import com.puce.apimentejuego.repositories.GameRepository
import com.puce.apimentejuego.repositories.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
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
    private val categoryRepository: CategoyRepositoy,
    private val gameMapper: GameMapper,
    private val gameAnswerRepository: GameAnswerRepository,
    private val questionOptionRepository: QuestionOptionRepository,
    private val questionRepository: QuestionRepository
) {


    // Inicia un nuevo juego o devuelve uno existente para el usuario y categoría dados
    fun startOrGetGame(request: GameRequest): GameResponse {

        val existingGame = gameRepository.findByUserIdAndCategoryId(request.userId, request.categoryId)

        if (existingGame.isPresent) {
            return gameMapper.toResponse(existingGame.get())
        }

        val user = userRepository.findById(request.userId)
            .orElseThrow { NoSuchElementException("User not found") }
        val category = categoryRepository.findById(request.categoryId)
            .orElseThrow { NoSuchElementException("Category not found") }

        val newGame = gameMapper.toEntity(user, category)
        val savedGame = gameRepository.save(newGame)

        return gameMapper.toResponse(savedGame)
    }


    // No esta siendo usado actualmente
    fun updateScore(gameId: Long, score: Int): GameResponse {
        val game = gameRepository.findById(gameId)
            .orElseThrow { NoSuchElementException("Game not found") }

        game.score = score
        // game.updatedAt se actualiza automáticamente si BaseEntity usa @PreUpdate o similar,
        // si no, JPA lo hace al guardar si hay cambios.

        val savedGame = gameRepository.save(game)
        return gameMapper.toResponse(savedGame)
    }

    @Transactional
    fun submitAnswers(request: SubmitAnswersRequest): GameResultResponse {
        val game = gameRepository.findById(request.gameId)
            .orElseThrow { NoSuchElementException("Game not found") }

        // Si el juego ya terminó, no permitir enviar respuestas de nuevo


        var currentScore = 0
        var correctCount = 0
        var incorrectCount = 0
        var unansweredCount = 0

        val answersToSave = mutableListOf<GameAnswer>()

        for (answer in request.answers) {
            val question = questionRepository.findById(answer.questionId)
                .orElseThrow { NoSuchElementException("Question not found: ${answer.questionId}") }

            var isCorrect = false
            var selectedOption: QuestionOption? = null

            if (answer.selectedOptionId != null) {
                val option = questionOptionRepository.findById(answer.selectedOptionId)
                    .orElseThrow { NoSuchElementException("Option not found: ${answer.selectedOptionId}") }

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
        game.finishedAt = LocalDateTime.now()
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
