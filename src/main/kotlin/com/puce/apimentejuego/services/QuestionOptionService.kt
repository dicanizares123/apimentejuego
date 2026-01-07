package com.puce.apimentejuego.services

import com.puce.apimentejuego.mappers.QuestionOptionMapper
import com.puce.apimentejuego.models.requests.QuestionOptionRequest
import com.puce.apimentejuego.models.responses.OptionSimpleResponse
import com.puce.apimentejuego.models.responses.QuestionOptionResponse
import com.puce.apimentejuego.models.responses.QuestionWithOptionsResponse
import com.puce.apimentejuego.repositories.CategoyRepositoy
import com.puce.apimentejuego.repositories.OptionRepository
import com.puce.apimentejuego.repositories.QuestionOptionRepository
import com.puce.apimentejuego.repositories.QuestionRepository
import org.springframework.stereotype.Service

@Service
class QuestionOptionService(
    private val questionOptionRepository: QuestionOptionRepository,
    private val questionRepository: QuestionRepository,
    private val optionRepository: OptionRepository,
    private val questionOptionMapper: QuestionOptionMapper,
    private val categoryRepository: CategoyRepositoy
) {

    // C: Create
    fun save(request: QuestionOptionRequest): QuestionOptionResponse {
        val question = questionRepository.findById(request.questionId)
            .orElseThrow { NoSuchElementException("Question with ID ${request.questionId} not found") }

        val option = optionRepository.findById(request.optionId)
            .orElseThrow { NoSuchElementException("Option with ID ${request.optionId} not found") }

        val entity = questionOptionMapper.toEntity(request, question, option)
        val savedEntity = questionOptionRepository.save(entity)

        return questionOptionMapper.toResponse(savedEntity)
    }

    // R: Read By ID
    fun findById(id: Long): QuestionOptionResponse {
        val entity = questionOptionRepository.findById(id)
            .orElseThrow { NoSuchElementException("QuestionOption with ID $id not found") }
        return questionOptionMapper.toResponse(entity)
    }

    // R: Read All
    fun findAll(): List<QuestionOptionResponse> {
        return questionOptionRepository.findAll()
            .map { questionOptionMapper.toResponse(it) }
    }

    // U: Update
    fun update(id: Long, request: QuestionOptionRequest): QuestionOptionResponse {
        val existingEntity = questionOptionRepository.findById(id)
            .orElseThrow { NoSuchElementException("QuestionOption with ID $id not found") }

        if (existingEntity.question.id != request.questionId) {
            val newQuestion = questionRepository.findById(request.questionId)
                .orElseThrow { NoSuchElementException("Question with ID ${request.questionId} not found") }
            existingEntity.question = newQuestion
        }

        if (existingEntity.option.id != request.optionId) {
            val newOption = optionRepository.findById(request.optionId)
                .orElseThrow { NoSuchElementException("Option with ID ${request.optionId} not found") }
            existingEntity.option = newOption
        }

        existingEntity.isCorrect = request.isCorrect

        val updatedEntity = questionOptionRepository.save(existingEntity)
        return questionOptionMapper.toResponse(updatedEntity)
    }

    // D: Delete
    fun deleteById(id: Long) {
        if (!questionOptionRepository.existsById(id)) {
            throw NoSuchElementException("QuestionOption with ID $id not found")
        }
        questionOptionRepository.deleteById(id)
    }

    // Custom Endpoint: Get Questions with Options structure (Randomized by Category limit)
    fun getQuestionsWithOptions(categoryId: Long): List<QuestionWithOptionsResponse> {
        // 1. Obtener la categoría para saber el límite de preguntas (questionsPerGame)
        val category = categoryRepository.findById(categoryId)
            .orElseThrow { NoSuchElementException("Category with ID $categoryId not found") }

        // 2. Obtener todas las preguntas de esa categoría
        val allQuestions = questionRepository.findByCategoryId(categoryId)

        // 3. Mezclar aleatoriamente y tomar solo la cantidad necesaria
        // .take(n) es seguro: si hay menos preguntas que 'n', devuelve todas las que haya.
        val selectedQuestions = allQuestions.shuffled().take(category.questionsPerGame)

        // 4. Mapear a la respuesta
        return selectedQuestions.map { question ->
            val options = question.questionOptions.map { qo ->
                OptionSimpleResponse(
                    questionOptionId = qo.id, // ID de la relación (question_options)
                    possibleAnswer = qo.option.possibleAnswer // Texto de la opción
                )
            }

            QuestionWithOptionsResponse(
                questionId = question.id,
                question = question.question,
                categoryId = question.category.id,
                options = options
            )
        }
    }
}