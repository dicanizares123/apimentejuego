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

    // C: Create
    fun save(request: UserRequest): UserResponse {
        val entity = userMapper.toEntity(request)
        val savedUser = userRepository.save(entity)
        return userMapper.toResponse(savedUser)
    }

    // R: Read By ID
    fun findById(id: Long): UserResponse {
        val foundUser = userRepository.findById(id)
            .orElseThrow { UserNotFoundException(message = "User with ID $id not found") }
        return userMapper.toResponse(foundUser)
    }

    // R: Read All
    fun findAll(): List<UserResponse> {
        return userRepository.findAll()
            .map { userMapper.toResponse(it) } // Convertimos la lista de entidades a lista de respuestas
    }

    // U: Update
    fun update(id: Long, request: UserRequest): UserResponse {
        val existingUser = userRepository.findById(id)
            .orElseThrow { UserNotFoundException(message = "User with ID $id not found") }

        // 2. Actualizar campos manualmente
        existingUser.firstName = request.firstName
        existingUser.lastName = request.lastName
        existingUser.email = request.email
        existingUser.username = request.username
        existingUser.password = request.password

        // 3. Guardar cambios
        val updatedUser = userRepository.save(existingUser)
        return userMapper.toResponse(updatedUser)
    }

    // D: Delete
    fun deleteById(id: Long) {
        if (!userRepository.existsById(id)) {
            throw UserNotFoundException(message = "User with ID $id not found")
        }
        userRepository.deleteById(id)
    }
}