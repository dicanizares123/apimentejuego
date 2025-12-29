package com.puce.apimentejuego.models.responses

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class OptionResponse(
    val id: Long,
    @JsonProperty("possible_answer")
    val possibleAnswer: String
)
