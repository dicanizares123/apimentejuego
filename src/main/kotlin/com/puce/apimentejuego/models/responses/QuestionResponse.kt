package com.puce.apimentejuego.models.responses

import com.fasterxml.jackson.annotation.JsonProperty


data class QuestionResponse(
    val id : Long,
    @JsonProperty("category_id")
    val categoryId : Long,
    val question : String,
    val  explanation : String,
    @JsonProperty("is_active")
    val isActive : Boolean
)
