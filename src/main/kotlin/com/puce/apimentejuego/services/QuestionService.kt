package com.puce.apimentejuego.services

import com.puce.apimentejuego.mappers.QuestionsMapper
import com.puce.apimentejuego.models.requests.QuestionRequest
import com.puce.apimentejuego.models.responses.QuestionResponse
import com.puce.apimentejuego.repositories.CategoyRepositoy
import com.puce.apimentejuego.repositories.QuestionRepository
import org.springframework.stereotype.Service

@Service
class QuestionService(
    private val questionRepository: QuestionRepository,
    private val questionsMapper: QuestionsMapper,
    private val categoryRepository: CategoyRepositoy
) {

    // C: Create
    fun save(request: QuestionRequest): QuestionResponse {
        val category = categoryRepository.findById(request.categoryId)
            .orElseThrow { NoSuchElementException("Category with ID ${request.categoryId} not found") }

        val entity = questionsMapper.toEntity(request, category)
        val savedQuestion = questionRepository.save(entity)

        return questionsMapper.toResponse(savedQuestion)
    }

    // R: Read By ID
    fun findById(id: Long): QuestionResponse {
        val foundQuestion = questionRepository.findById(id)
            .orElseThrow { NoSuchElementException("Question with ID $id not found") }
        return questionsMapper.toResponse(foundQuestion)
    }

    // R: Read All
    fun findAll(): List<QuestionResponse> {
        return questionRepository.findAll()
            .map { questionsMapper.toResponse(it) }
    }

    // R: Read All by Category
    fun findAllByCategoryId(categoryId: Long): List<QuestionResponse> {
        if (!categoryRepository.existsById(categoryId)) {
            throw NoSuchElementException("Category with ID $categoryId not found")
        }
        return questionRepository.findByCategoryId(categoryId)
            .map { questionsMapper.toResponse(it) }
    }

    // U: Update
    fun update(id: Long, request: QuestionRequest): QuestionResponse {
        val existingQuestion = questionRepository.findById(id)
            .orElseThrow { NoSuchElementException("Question with ID $id not found") }

        // Si cambia la categoría, necesitamos buscar la nueva entidad Category
        if (existingQuestion.category.id != request.categoryId) {
            val newCategory = categoryRepository.findById(request.categoryId)
                .orElseThrow { NoSuchElementException("Category with ID ${request.categoryId} not found") }
            existingQuestion.category = newCategory
        }

        existingQuestion.question = request.question
        existingQuestion.explanation = request.explanation
        existingQuestion.isActive = request.isActive

        val updatedQuestion = questionRepository.save(existingQuestion)
        return questionsMapper.toResponse(updatedQuestion)
    }

    // D: Delete
    fun deleteById(id: Long) {
        if (!questionRepository.existsById(id)) {
            throw NoSuchElementException("Question with ID $id not found")
        }
        questionRepository.deleteById(id)
    }
}