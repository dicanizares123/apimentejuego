package com.puce.apimentejuego.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class GameRequest(
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("category_id")
    val categoryId: Long,
)

