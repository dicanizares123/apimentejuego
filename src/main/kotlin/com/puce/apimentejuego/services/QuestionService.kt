package com.puce.apimentejuego.services

import com.puce.apimentejuego.exceptions.CategoryNotFoundException
import com.puce.apimentejuego.exceptions.MissingParameterException
import com.puce.apimentejuego.exceptions.QuestionIdNotFoundException
import com.puce.apimentejuego.mappers.QuestionMapper
import com.puce.apimentejuego.models.requests.QuestionRequest
import com.puce.apimentejuego.models.responses.QuestionResponse
import com.puce.apimentejuego.repositories.CategoryRepository
import com.puce.apimentejuego.repositories.QuestionRepository
import org.springframework.stereotype.Service

@Service
class QuestionService(
    private val questionRepository: QuestionRepository,
    private val questionMapper: QuestionMapper,
    private val categoryRepository: CategoryRepository
) {

    // C: Create
    fun save(request: QuestionRequest): QuestionResponse {
        // Validar campos requeridos
        if (request.categoryId == null) {
            throw MissingParameterException("Field 'category_id' is required")
        }
        if (request.question.isNullOrBlank()) {
            throw MissingParameterException("Field 'question' is required and cannot be blank")
        }
        if (request.explanation.isNullOrBlank()) {
            throw MissingParameterException("Field 'explanation' is required and cannot be blank")
        }


        val category = categoryRepository.findById(request.categoryId)
            .orElseThrow { CategoryNotFoundException("Category with ID ${request.categoryId} not found") }

        val entity = questionMapper.toEntity(request, category)
        val savedQuestion = questionRepository.save(entity)

        return questionMapper.toResponse(savedQuestion)
    }

    // R: Read By ID
    fun findById(id: Long): QuestionResponse {
        val foundQuestion = questionRepository.findById(id)
            .orElseThrow { QuestionIdNotFoundException("Question with ID $id not found") }
        return questionMapper.toResponse(foundQuestion)
    }

    // R: Read All
    fun findAll(): List<QuestionResponse> {
        return questionRepository.findAll()
            .map { questionMapper.toResponse(it) }
    }


    // U: Update
    fun update(id: Long, request: QuestionRequest): QuestionResponse {
        // Validar campos requeridos
        if (request.categoryId == null) {
            throw MissingParameterException("Field 'category_id' is required")
        }
        if (request.question.isNullOrBlank()) {
            throw MissingParameterException("Field 'question' is required and cannot be blank")
        }
        if (request.explanation.isNullOrBlank()) {
            throw MissingParameterException("Field 'explanation' is required and cannot be blank")
        }


        val existingQuestion = questionRepository.findById(id)
            .orElseThrow { QuestionIdNotFoundException("Question with ID $id not found") }

        // Si cambia la categoría, necesitamos buscar la nueva entidad Category
        if (existingQuestion.category.id != request.categoryId) {
            val newCategory = categoryRepository.findById(request.categoryId)
                .orElseThrow { CategoryNotFoundException("Category with ID ${request.categoryId} not found") }
            existingQuestion.category = newCategory
        }

        existingQuestion.question = request.question
        existingQuestion.explanation = request.explanation

        val updatedQuestion = questionRepository.save(existingQuestion)
        return questionMapper.toResponse(updatedQuestion)
    }

    // D: Delete
    fun deleteById(id: Long) {
        if (!questionRepository.existsById(id)) {
            throw QuestionIdNotFoundException("Question with ID $id not found")
        }
        questionRepository.deleteById(id)
    }
}