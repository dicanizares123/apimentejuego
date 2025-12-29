package com.puce.apimentejuego.mappers

import com.puce.apimentejuego.models.entities.Option
import com.puce.apimentejuego.models.entities.Question
import com.puce.apimentejuego.models.entities.QuestionOption
import com.puce.apimentejuego.models.requests.QuestionOptionRequest
import com.puce.apimentejuego.models.responses.QuestionOptionResponse
import org.springframework.stereotype.Component

@Component
class QuestionOptionMapper {

    fun toEntity(request: QuestionOptionRequest, question: Question, option: Option): QuestionOption {
        return QuestionOption(
            question = question,
            option = option,
            isCorrect = request.isCorrect
        )
    }

    fun toResponse(entity: QuestionOption): QuestionOptionResponse {
        return QuestionOptionResponse(
            id = entity.id,
            questionId = entity.question.id,
            optionId = entity.option.id,
            isCorrect = entity.isCorrect
        )
    }
}