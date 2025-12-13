package com.puce.apimentejuego.controllers

import com.puce.apimentejuego.models.requests.UserRequest
import com.puce.apimentejuego.models.responses.UserResponse
import com.puce.apimentejuego.services.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(value = ["/users"])
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun save(@RequestBody user: UserRequest): UserResponse {
        return userService.save(user)
    }

    @GetMapping
    fun findById(@RequestParam id: Long ): UserResponse{
        return userService.findById(id)
    }


}