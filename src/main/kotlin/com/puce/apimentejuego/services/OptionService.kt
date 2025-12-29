package com.puce.apimentejuego.services

import com.puce.apimentejuego.mappers.OptionMapper
import com.puce.apimentejuego.models.entities.Option
import com.puce.apimentejuego.models.requests.OptionRequest
import com.puce.apimentejuego.models.responses.OptionResponse
import com.puce.apimentejuego.repositories.OptionRepository
import org.springframework.stereotype.Service


@Service
class OptionService(
    private val optionRepository: OptionRepository,
    private val optionMapper: OptionMapper,

) {
    // C: Create
    fun save (request: OptionRequest): OptionResponse{
        val entity = optionMapper.toEntity(request)
        val savedOption = optionRepository.save(entity)

        return optionMapper.toResponse(savedOption)

    }

    // R: Read By ID
    fun findById(id: Long): OptionResponse{
        val foundOption = optionRepository.findById(id)
            .orElseThrow { NoSuchElementException("Option with ID $id not found") }
        return optionMapper.toResponse(foundOption)
    }

    // R: Read All
    fun findAll(): List<OptionResponse> {
        return optionRepository.findAll()
            .map { optionMapper.toResponse(it) }
    }

    // u: Update
    fun update(id: Long, request: OptionRequest): OptionResponse{
        val existingOption = optionRepository.findById(id)
            .orElseThrow { NoSuchElementException("Option with ID $id not found") }

        // 2. Actualizar campos manualmente
        existingOption.possibleAnswer = request.possibleAnswer

        // 3. Guardar cambios
        val updatedOption = optionRepository.save(existingOption)

        return optionMapper.toResponse(updatedOption)

    } fun deleteById(id: Long){
        if (!optionRepository.existsById(id)){
            throw NoSuchElementException("Option with ID $id not found")
        }

        optionRepository.deleteById(id)

    }

    // D: Delete





}