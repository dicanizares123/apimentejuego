package com.puce.apimentejuego.models.responses

data class GameResultResponse(
    val gameId: Long,
    val totalScore: Int,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val unanswered: Int
)

