package com.puce.apimentejuego.controllers

import com.puce.apimentejuego.models.requests.UserRequest
import com.puce.apimentejuego.models.responses.UserResponse
import com.puce.apimentejuego.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/users"])
class UserController(
    private val userService: UserService
) {

    // 1. CREAR (POST /users)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Devuelve 201 Created (Buena práctica)
    fun save(@RequestBody user: UserRequest): UserResponse {
        return userService.save(user)
    }

    // 2. LEER TODOS (GET /users)
    @GetMapping
    fun findAll(): List<UserResponse> {
        return userService.findAll()
    }

    // 3. LEER POR ID (GET /users/{id})
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): UserResponse {
        return userService.findById(id)
    }

    // 4. ACTUALIZAR (PUT /users/{id})
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody user: UserRequest): UserResponse {
        return userService.update(id, user)
    }

    // 5. ELIMINAR (DELETE /users/{id})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Devuelve 204 No Content (Estándar al borrar)
    fun delete(@PathVariable id: Long) {
        userService.deleteById(id)
    }
}