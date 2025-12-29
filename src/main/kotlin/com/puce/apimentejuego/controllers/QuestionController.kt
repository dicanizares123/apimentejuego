package com.puce.apimentejuego.controllers

import com.puce.apimentejuego.models.requests.QuestionRequest
import com.puce.apimentejuego.models.responses.QuestionResponse
import com.puce.apimentejuego.services.QuestionService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/questions")
class QuestionController(
    private val questionService: QuestionService
) {

    // 1. CREAR (POST /questions)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody request: QuestionRequest): QuestionResponse {
        return questionService.save(request)
    }

    // 2. LEER TODOS (GET /questions)
    @GetMapping
    fun findAll(): List<QuestionResponse> {
        return questionService.findAll()
    }

    // 3. LEER POR ID (GET /questions/{id})
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): QuestionResponse {
        return questionService.findById(id)
    }


    // 4. ACTUALIZAR (PUT /questions/{id})
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: QuestionRequest): QuestionResponse {
        return questionService.update(id, request)
    }

    // 5. ELIMINAR (DELETE /questions/{id})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        questionService.deleteById(id)
    }
}