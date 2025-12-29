package com.puce.apimentejuego.controllers

import com.puce.apimentejuego.models.requests.QuestionOptionRequest
import com.puce.apimentejuego.models.responses.QuestionOptionResponse
import com.puce.apimentejuego.models.responses.QuestionWithOptionsResponse
import com.puce.apimentejuego.services.QuestionOptionService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/question-options")
class QuestionOptionController(
    private val questionOptionService: QuestionOptionService
) {

    // 1. CREAR (POST /question-options)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody request: QuestionOptionRequest): QuestionOptionResponse {
        return questionOptionService.save(request)
    }

    // 2. LEER TODOS (GET /question-options)
    @GetMapping
    fun findAll(): List<QuestionOptionResponse> {
        return questionOptionService.findAll()
    }

    // 3. LEER POR ID (GET /question-options/{id})
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): QuestionOptionResponse {
        return questionOptionService.findById(id)
    }

    // 4. ACTUALIZAR (PUT /question-options/{id})
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: QuestionOptionRequest): QuestionOptionResponse {
        return questionOptionService.update(id, request)
    }

    // 5. ELIMINAR (DELETE /question-options/{id})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        questionOptionService.deleteById(id)
    }

    // 6. OBTENER PREGUNTAS CON OPCIONES (GET /question-options/with-options/{categoryId})
    @GetMapping("/with-options/{categoryId}")
    fun getQuestionsWithOptions(@PathVariable categoryId: Long): List<QuestionWithOptionsResponse> {
        return questionOptionService.getQuestionsWithOptions(categoryId)
    }
}