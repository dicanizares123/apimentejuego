package com.puce.apimentejuego.controllers

import com.puce.apimentejuego.models.requests.CategoryRequest
import com.puce.apimentejuego.models.responses.CategoryResponse
import com.puce.apimentejuego.services.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController




@RestController
@RequestMapping(value = ["/categories"])
class CategoryController(
    private val categoryService: CategoryService
) {

    // 1. CREAR (POST /category)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody request: CategoryRequest ): CategoryResponse{
        return categoryService.save(request)
    }

    // 1.1 CREAR MULTIPLES (POST /category/batch)
    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveAll(@RequestBody requests: List<CategoryRequest>): List<CategoryResponse> {
        return categoryService.saveAll(requests)
    }

    // 2. LEER TODOS (GET /category)
    @GetMapping
    fun findAll(): List<CategoryResponse>{
        return categoryService.findAll()
    }

    // 3. LEER POR ID (GET /category/{id})
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): CategoryResponse{
        return categoryService.findById(id)
    }

    // 4. ACTUALIZAR (PUT /category/{id})
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: CategoryRequest): CategoryResponse {
        return categoryService.update(id, request)
    }

    // 5. ELIMINAR (DELETE /category/{id})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Devuelve 204 No Content (Estándar al borrar)
    fun delete(@PathVariable id: Long){
        categoryService.deleteById(id)
    }







}