package com.puce.apimentejuego.services

import com.puce.apimentejuego.exceptions.DuplicateResourceException
import com.puce.apimentejuego.exceptions.MissingParameterException
import com.puce.apimentejuego.exceptions.UserNotFoundException
import org.springframework.stereotype.Service
import org.springframework.security.crypto.password.PasswordEncoder
import com.puce.apimentejuego.repositories.UserRepository
import com.puce.apimentejuego.mappers.UserMapper
import com.puce.apimentejuego.models.requests.UserRequest
import com.puce.apimentejuego.models.responses.UserResponse

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val passwordEncoder: PasswordEncoder
) {

    // C: Create
    fun save(request: UserRequest): UserResponse {
        // Validar campos requeridos
        if (request.firstName.isNullOrBlank()) {
            throw MissingParameterException("Field 'first_name' is required and cannot be blank")
        }
        if (request.lastName.isNullOrBlank()) {
            throw MissingParameterException("Field 'last_name' is required and cannot be blank")
        }
        if (request.username.isNullOrBlank()) {
            throw MissingParameterException("Field 'username' is required and cannot be blank")
        }
        if (request.email.isNullOrBlank()) {
            throw MissingParameterException("Field 'email' is required and cannot be blank")
        }
        if (request.password.isNullOrBlank()) {
            throw MissingParameterException("Field 'password' is required and cannot be blank")
        }

        // Validar username duplicado
        if (userRepository.existsByUsername(request.username)) {
            throw DuplicateResourceException("Username '${request.username}' already exists")
        }

        // Validar email duplicado
        if (userRepository.existsByEmail(request.email)) {
            throw DuplicateResourceException("Email '${request.email}' already exists")
        }

        val entity = userMapper.toEntity(request)
        // Hashear la contraseña antes de guardar
        entity.password = passwordEncoder.encode(request.password) ?: throw IllegalStateException("Failed to encode password")
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
        // Validar campos requeridos
        if (request.firstName.isNullOrBlank()) {
            throw MissingParameterException("Field 'first_name' is required and cannot be blank")
        }
        if (request.lastName.isNullOrBlank()) {
            throw MissingParameterException("Field 'last_name' is required and cannot be blank")
        }
        if (request.username.isNullOrBlank()) {
            throw MissingParameterException("Field 'username' is required and cannot be blank")
        }
        if (request.email.isNullOrBlank()) {
            throw MissingParameterException("Field 'email' is required and cannot be blank")
        }
        if (request.password.isNullOrBlank()) {
            throw MissingParameterException("Field 'password' is required and cannot be blank")
        }

        val existingUser = userRepository.findById(id)
            .orElseThrow { UserNotFoundException(message = "User with ID $id not found") }

        // Validar username duplicado solo si cambió
        if (request.username != existingUser.username && userRepository.existsByUsername(request.username)) {
            throw DuplicateResourceException("Username '${request.username}' already exists")
        }

        // Validar email duplicado solo si cambió
        if (request.email != existingUser.email && userRepository.existsByEmail(request.email)) {
            throw DuplicateResourceException("Email '${request.email}' already exists")
        }

        // 2. Actualizar campos manualmente
        existingUser.firstName = request.firstName
        existingUser.lastName = request.lastName
        existingUser.email = request.email
        existingUser.username = request.username
        // Hashear la contraseña si se está actualizando
        existingUser.password = passwordEncoder.encode(request.password) ?: throw IllegalStateException("Failed to encode password")

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