package com.puce.apimentejuego.models.requests

import com.fasterxml.jackson.annotation.JsonProperty


data class CategoryRequest(
    val slug: String,
    @JsonProperty("questions_per_game")
    val questionsPerGame: Int,
    val description: String,
    @JsonProperty("short_description")
    val shortDescription: String,
    val difficulty: String,
    val title: String,
    val duration_in_minutes: Int
)