package com.puce.apimentejuego.models.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class QuestionOptionResponse(
    val id: Long,
    @JsonProperty("question_id")
    val questionId: Long,
    @JsonProperty("option_id")
    val optionId: Long,
    @JsonProperty("is_correct")
    val isCorrect: Boolean
)
