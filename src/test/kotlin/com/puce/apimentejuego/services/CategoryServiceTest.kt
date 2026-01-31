package com.puce.apimentejuego.services

import com.puce.apimentejuego.exceptions.CategoryNotFoundException
import com.puce.apimentejuego.mappers.CategoryMapper
import com.puce.apimentejuego.models.entities.Category
import com.puce.apimentejuego.models.requests.CategoryRequest
import com.puce.apimentejuego.models.responses.CategoryResponse
import com.puce.apimentejuego.repositories.CategoryRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.Optional

class CategoryServiceTest {

    // 1. Mocks
    private lateinit var categoryRepositoryMock: CategoryRepository
    private lateinit var categoryMapperMock: CategoryMapper

    // 2. Servicio
    private lateinit var categoryService: CategoryService

    @BeforeEach
    fun init() {
        categoryRepositoryMock = mock(CategoryRepository::class.java)
        categoryMapperMock = mock(CategoryMapper::class.java)

        categoryService = CategoryService(
            categoryRepositoryMock,
            categoryMapperMock
        )
    }

    // --- HELPER METHOD ---
    private fun createCategoryDummy(id: Long): Category {
        return Category(
            title = "Historia",
            description = "Preguntas de historia universal",
            shortDescription = "Hist",
            slug = "historia",
            difficulty = "Medium",
            duration_in_minutes = 15,
            questionsPerGame = 10
        ).apply { this.id = id }
    }

    // --- TEST: CREATE ---
    @Test
    fun `SHOULD save a category GIVEN a valid request`() {
        val request = CategoryRequest(
            slug = "historia",
            questionsPerGame = 10,
            description = "Preguntas de historia universal",
            shortDescription = "Hist",
            difficulty = "Medium",
            title = "Historia",
            duration_in_minutes = 15
        )

        val categoryEntity = createCategoryDummy(0L)
        val savedEntity = createCategoryDummy(1L)

        val expectedResponse = CategoryResponse(
            id = 1L,
            slug = "historia",
            questionsPerGame = 10,
            description = "Preguntas de historia universal",
            shortDescription = "Hist",
            difficulty = "Medium",
            title = "Historia",
            duration_in_minutes = 15
        )
        `when`(categoryMapperMock.toEntity(request)).thenReturn(categoryEntity)
        `when`(categoryRepositoryMock.save(categoryEntity)).thenReturn(savedEntity)
        `when`(categoryMapperMock.toResponse(savedEntity)).thenReturn(expectedResponse)

        val result = categoryService.save(request)

        assertEquals(1L, result.id)
        verify(categoryRepositoryMock, times(1)).save(categoryEntity)
    }

    // --- TEST: FIND BY ID ---
    @Test
    fun `SHOULD return a category response GIVEN a valid id`() {
        val categoryId = 1L
        val categoryEntity = createCategoryDummy(categoryId)

        val expectedResponse = CategoryResponse(
            id = categoryId,
            slug = "historia",
            questionsPerGame = 10,
            description = "Desc",
            shortDescription = "Short",
            difficulty = "Medium",
            title = "Historia",
            duration_in_minutes = 15
        )

        `when`(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.of(categoryEntity))
        `when`(categoryMapperMock.toResponse(categoryEntity)).thenReturn(expectedResponse)

        val result = categoryService.findById(categoryId)

        assertEquals(1L, result.id)
    }

    // --- TEST: FIND BY ID (Error) ---
    @Test
    fun `SHOULD throw CategoryNotFoundException GIVEN a non existing id`() {
        val categoryId = 99L
        `when`(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.empty())

        assertThrows(CategoryNotFoundException::class.java) {
            categoryService.findById(categoryId)
        }
    }

    // --- TEST: FIND ALL ---
    @Test
    fun `SHOULD return a list of categories`() {
        val categoryList = listOf(createCategoryDummy(1L))
        val response = CategoryResponse(1L, "slug", 5, "desc", "short", "Easy", "Title", 10)

        `when`(categoryRepositoryMock.findAll()).thenReturn(categoryList)
        `when`(categoryMapperMock.toResponse(categoryList[0])).thenReturn(response)

        val result = categoryService.findAll()

        assertEquals(1, result.size)
        verify(categoryRepositoryMock, times(1)).findAll()
    }

    // --- TEST: UPDATE (CORREGIDO - Donde fallaba antes) ---
    @Test
    fun `SHOULD update a category GIVEN a valid id and request`() {
        val categoryId = 1L
        val request = CategoryRequest(
            slug = "nueva-slug",
            questionsPerGame = 20,
            description = "Nueva Desc",
            shortDescription = "Nueva",
            difficulty = "Hard",
            title = "Nuevo Titulo",
            duration_in_minutes = 30
        )

        val existingCategory = createCategoryDummy(categoryId)
        val updatedCategory = createCategoryDummy(categoryId).apply {
            title = "Nuevo Titulo"
            difficulty = "Hard"
        }

        val expectedResponse = CategoryResponse(
            id = categoryId,
            slug = "nueva-slug",
            questionsPerGame = 20,
            description = "Nueva Desc",
            shortDescription = "Nueva",
            difficulty = "Hard",
            title = "Nuevo Titulo",
            duration_in_minutes = 30
        )

        `when`(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.of(existingCategory))

        // CORRECCIÓN: Quitamos any() y usamos los objetos reales
        // El servicio modifica 'existingCategory' y lo guarda
        `when`(categoryRepositoryMock.save(existingCategory)).thenReturn(updatedCategory)
        `when`(categoryMapperMock.toResponse(updatedCategory)).thenReturn(expectedResponse)

        val result = categoryService.update(categoryId, request)

        assertEquals("Nuevo Titulo", result.title)
    }

    // --- TEST: UPDATE (Error) ---
    @Test
    fun `SHOULD throw CategoryNotFoundException when updating non existing category`() {
        val categoryId = 99L
        val request = CategoryRequest("s", 1, "d", "sd", "d", "t", 10)

        `when`(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.empty())

        assertThrows(CategoryNotFoundException::class.java) {
            categoryService.update(categoryId, request)
        }

        // Verificamos que nunca intente guardar nada
        verify(categoryRepositoryMock, never()).save(any())
    }

    // --- TEST: DELETE ---
    @Test
    fun `SHOULD delete a category GIVEN a valid id`() {
        val categoryId = 1L
        `when`(categoryRepositoryMock.existsById(categoryId)).thenReturn(true)

        categoryService.deleteById(categoryId)

        verify(categoryRepositoryMock, times(1)).deleteById(categoryId)
    }

    // --- TEST: DELETE (Error) ---
    @Test
    fun `SHOULD throw CategoryNotFoundException when deleting non existing id`() {
        val categoryId = 99L
        `when`(categoryRepositoryMock.existsById(categoryId)).thenReturn(false)

        assertThrows(CategoryNotFoundException::class.java) {
            categoryService.deleteById(categoryId)
        }

        verify(categoryRepositoryMock, never()).deleteById(anyLong())
    }
}