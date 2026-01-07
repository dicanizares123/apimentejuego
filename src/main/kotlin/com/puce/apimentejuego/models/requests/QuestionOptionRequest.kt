package com.puce.apimentejuego.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class QuestionOptionRequest(
    @JsonProperty("question_id")
    val questionId: Long,
    @JsonProperty("option_id")
    val optionId: Long,
    @JsonProperty("is_correct")
    val isCorrect: Boolean
)
