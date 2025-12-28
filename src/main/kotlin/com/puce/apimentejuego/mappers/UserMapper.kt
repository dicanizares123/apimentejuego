package com.puce.apimentejuego.mappers

import com.puce.apimentejuego.models.entities.User
import com.puce.apimentejuego.models.requests.UserRequest
import com.puce.apimentejuego.models.responses.UserResponse
import org.springframework.stereotype.Component


@Component
class UserMapper {
    fun toEntity(request: UserRequest): User{
        return User(
            firstName = request.firstName,
            lastName = request.lastName,
            username = request.username,
            email = request.email,
            password = request.password,
        )
    }

    fun toResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id,
            username = user.username,
            email = user.email,
            createdAt = user.createdAt,
            firstName = user.firstName,
            lastName = user.lastName,
        )
    }

}