package com.puce.apimentejuego.services

import com.puce.apimentejuego.exceptions.UserNotFoundException
import org.springframework.stereotype.Service
import com.puce.apimentejuego.repositories.UserRepository
import com.puce.apimentejuego.mappers.UserMapper
import com.puce.apimentejuego.models.requests.UserRequest
import com.puce.apimentejuego.models.responses.UserResponse


@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) {

    fun save (request: UserRequest): UserResponse{

        val entity = userMapper.toEntity(request)

        val savedUser = userRepository.save(entity)

        return userMapper.toResponse(savedUser)

    }

    fun findById(id: Long): UserResponse{
        val foundUser = userRepository.findById(id).orElse(null)

        if (foundUser == null){
           throw UserNotFoundException(message = "User not found")
        }

        return userMapper.toResponse(foundUser)
    }

}
