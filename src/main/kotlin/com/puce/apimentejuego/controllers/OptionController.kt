package com.puce.apimentejuego.controllers

import com.puce.apimentejuego.models.requests.OptionRequest
import com.puce.apimentejuego.models.responses.OptionResponse
import com.puce.apimentejuego.services.OptionService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/options")
class OptionController(
    private val optionService: OptionService
) {

    // 1. CREATE (POST /options)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody request: OptionRequest): OptionResponse{
        return optionService.save(request)
    }

    // 2. READ ALL (GET /options)
    @GetMapping
    fun findAll(): List<OptionResponse>{
        return optionService.findAll()
    }

    // 3. READ BY ID (GET /options/{id})
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): OptionResponse{
        return optionService.findById(id)
    }

    // 4. UPDATE (PUT /options/{id})
    fun update(@PathVariable id: Long, @RequestBody request: OptionRequest): OptionResponse{
        return optionService.update(id, request)
    }

    // 5. DELETE (DELETE /options/{id})
    fun delete(@PathVariable id: Long){
        optionService.deleteById(id)
    }



}