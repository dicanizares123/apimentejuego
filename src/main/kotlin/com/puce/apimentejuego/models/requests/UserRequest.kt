package com.puce.apimentejuego.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class UserRequest(
    @JsonProperty( "first_name")
    val firstName: String,
    @JsonProperty( "last_name")
    val lastName: String,
    val username: String,
    val email: String,
    val password: String,
)
