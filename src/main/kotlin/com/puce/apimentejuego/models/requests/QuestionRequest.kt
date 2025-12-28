package com.puce.apimentejuego.models.requests

import com.fasterxml.jackson.annotation.JsonProperty


data class QuestionRequest(
    @JsonProperty("category_id")
    val categoryId: Long,
    val question: String,
    val explanation: String,
    @JsonProperty("is_active")
    val isActive: Boolean
)