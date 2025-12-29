package com.puce.apimentejuego.models.responses

data class QuestionOptionResponse(
    val id: Long,
    val questionId: Long,
    val optionId: Long,
    val isCorrect: Boolean
)
