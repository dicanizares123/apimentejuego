package com.puce.apimentejuego.models.requests

data class QuestionOptionRequest(
    val questionId: Long,
    val optionId: Long,
    val isCorrect: Boolean
)
