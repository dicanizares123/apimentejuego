package com.puce.apimentejuego.models.responses

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class GameResponse(
    val id: Long,
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("category_id")
    val categoryId: Long,
    val score: Int,
    @JsonProperty("started_at")
    val startedAt: LocalDateTime,
    @JsonProperty("finished_at")
    val finishedAt: LocalDateTime?
)

