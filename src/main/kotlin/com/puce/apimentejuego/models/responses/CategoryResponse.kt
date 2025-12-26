package com.puce.apimentejuego.models.responses

import com.fasterxml.jackson.annotation.JsonProperty


data class CategoryResponse(
    val id: Long,
    val slug: String,
    @JsonProperty("questions_per_game")
    val questionsPerGame: Int,
    val description: String,
    @JsonProperty("short_description")
    val shortDescription: String,
    val difficulty: String,
    val title: String,
    val duration: Int

)