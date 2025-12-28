package com.puce.apimentejuego.models.responses

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    @JsonProperty( "created_at")
    val createdAt: LocalDateTime,
    val firstName: String,
    val lastName: String,
)
