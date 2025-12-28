package com.puce.apimentejuego.mappers

import com.puce.apimentejuego.models.entities.Category
import com.puce.apimentejuego.models.entities.Question
import com.puce.apimentejuego.models.requests.QuestionRequest
import com.puce.apimentejuego.models.responses.CategoryResponse
import com.puce.apimentejuego.models.responses.QuestionResponse
import org.springframework.stereotype.Component


@Component
class QuestionsMapper {

    fun toEntity(request: QuestionRequest, category: Category): Question{
        return Question(
            category = category,
            question = request.question,
            explanation = request.explanation,
            isActive = request.isActive
        )
    }

    fun toResponse(question: Question): QuestionResponse{
        return QuestionResponse(
            id = question.id,
            categoryId = question.category.id,
            question = question.question,
            explanation = question.explanation,
            isActive = question.isActive
        )
    }

}