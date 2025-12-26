package com.puce.apimentejuego.mappers

import com.puce.apimentejuego.models.entities.User
import com.puce.apimentejuego.models.requests.UserRequest
import com.puce.apimentejuego.models.responses.UserResponse
import org.springframework.stereotype.Component


@Component
class UserMapper {
    fun toEntity(user: UserRequest): User{
        return User(
            firstName = user.firstName,
            lastName = user.lastName,
            username = user.username,
            email = user.email,
            password = user.password,
        )
    }

    fun toResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id,
            username = user.username,
            email = user.email,
            createdAt = user.createdAt,
        )
    }

}