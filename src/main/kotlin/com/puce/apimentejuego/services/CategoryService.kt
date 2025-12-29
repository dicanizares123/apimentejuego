package com.puce.apimentejuego.services
import com.puce.apimentejuego.mappers.CategoryMapper
import com.puce.apimentejuego.models.requests.CategoryRequest
import com.puce.apimentejuego.models.responses.CategoryResponse
import com.puce.apimentejuego.repositories.CategoyRepositoy
import org.springframework.stereotype.Service


@Service
class CategoryService (
    private val categoryRepository: CategoyRepositoy,
    private val categoryMapper: CategoryMapper
){

    // C: Create
    fun save(request: CategoryRequest): CategoryResponse {
        val entity = categoryMapper.toEntity(request)
        val savedCategory = categoryRepository.save(entity)

        return categoryMapper.toResponse(savedCategory)
    }



    // R: Read By ID
    fun findById(id: Long): CategoryResponse {
        val foundCategory = categoryRepository.findById(id)
            .orElseThrow { NoSuchElementException("Category with ID $id not found") }
        return categoryMapper.toResponse(foundCategory)
    }

    // R: Read All
    fun findAll(): List<CategoryResponse> {
        return categoryRepository.findAll()
            .map { categoryMapper.toResponse(it) } // Convertimos la lista de entidades a lista de respuestas
    }

    fun update(id: Long, request: CategoryRequest): CategoryResponse{
        val existingCategory = categoryRepository.findById(id)
            .orElseThrow { NoSuchElementException("Category with ID $id not found") }

        // 2. Actualizar campos manualmente
        existingCategory.slug = request.slug
        existingCategory.questionsPerGame = request.questionsPerGame
        existingCategory.description = request.description
        existingCategory.shortDescription = request.shortDescription
        existingCategory.difficulty = request.difficulty
        existingCategory.title = request.title

        // 3. Guardar cambios
        val updateCategory = categoryRepository.save(existingCategory)
        return categoryMapper.toResponse(updateCategory)
    }

    // D: Delete
    fun deleteById(id: Long){
        if(!categoryRepository.existsById(id)){
            throw NoSuchElementException("Category with ID $id not found")
        }

        categoryRepository.deleteById(id)
    }

}