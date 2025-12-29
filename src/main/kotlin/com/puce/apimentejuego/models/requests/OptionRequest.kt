package com.puce.apimentejuego.models.requests

import com.fasterxml.jackson.annotation.JsonProperty


data class OptionRequest(
    @JsonProperty("possible_answer")
    val possibleAnswer: String
)
