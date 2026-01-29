package com.puce.apimentejuego.services

import com.puce.apimentejuego.mappers.QuestionMapper
import com.puce.apimentejuego.models.entities.Category
import com.puce.apimentejuego.models.entities.Question
import com.puce.apimentejuego.models.requests.QuestionRequest
import com.puce.apimentejuego.models.responses.QuestionResponse
import com.puce.apimentejuego.repositories.CategoryRepository
import com.puce.apimentejuego.repositories.QuestionRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.Optional

class QuestionServiceTest {

    // 1. Mocks
    private lateinit var questionRepositoryMock: QuestionRepository
    private lateinit var categoryRepositoryMock: CategoryRepository
    private lateinit var questionMapperMock: QuestionMapper

    // 2. Servicio
    private lateinit var questionService: QuestionService

    @BeforeEach
    fun init() {
        questionRepositoryMock = mock(QuestionRepository::class.java)
        categoryRepositoryMock = mock(CategoryRepository::class.java)
        questionMapperMock = mock(QuestionMapper::class.java)

        questionService = QuestionService(
            questionRepositoryMock,
            questionMapperMock,
            categoryRepositoryMock
        )
    }

    // --- HELPER METHODS ---

    private fun createCategoryDummy(id: Long): Category {
        return Category(
            title = "General",
            description = "Desc",
            shortDescription = "Short",
            slug = "slug",
            difficulty = "Easy",
            duration_in_minutes = 10,
            questionsPerGame = 5
        ).apply { this.id = id }
    }

    private fun createQuestionDummy(id: Long, category: Category): Question {
        return Question(
            question = "¿Es esto una prueba?",
            category = category,
            explanation = "Sí, es un test unitario",
            isActive = true
        ).apply { this.id = id }
    }

    // --- TEST: CREATE ---
    @Test
    fun `SHOULD save a question GIVEN a valid request`() {
        val categoryId = 1L
        val request = QuestionRequest(categoryId, "¿Pregunta?", "Explicación", true)

        val category = createCategoryDummy(categoryId)
        val questionEntity = createQuestionDummy(0L, category) // Sin ID
        val savedEntity = createQuestionDummy(10L, category)   // Con ID

        val expectedResponse = QuestionResponse(10L, categoryId, "¿Pregunta?", "Explicación", true)

        `when`(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.of(category))
        `when`(questionMapperMock.toEntity(request, category)).thenReturn(questionEntity)
        `when`(questionRepositoryMock.save(questionEntity)).thenReturn(savedEntity)
        `when`(questionMapperMock.toResponse(savedEntity)).thenReturn(expectedResponse)

        val result = questionService.save(request)

        assertEquals(10L, result.id)
        verify(questionRepositoryMock, times(1)).save(questionEntity)
    }

    // --- TEST: CREATE (Error) ---
    @Test
    fun `SHOULD throw NoSuchElementException when saving with non-existent category`() {
        val request = QuestionRequest(99L, "?", "Exp", true)

        `when`(categoryRepositoryMock.findById(99L)).thenReturn(Optional.empty())

        assertThrows(NoSuchElementException::class.java) {
            questionService.save(request)
        }
    }

    // --- TEST: FIND BY ID ---
    @Test
    fun `SHOULD return a question response GIVEN a valid id`() {
        val questionId = 10L
        val category = createCategoryDummy(1L)
        val questionEntity = createQuestionDummy(questionId, category)

        val expectedResponse = QuestionResponse(questionId, 1L, "Q", "E", true)

        `when`(questionRepositoryMock.findById(questionId)).thenReturn(Optional.of(questionEntity))
        `when`(questionMapperMock.toResponse(questionEntity)).thenReturn(expectedResponse)

        val result = questionService.findById(questionId)

        assertEquals(questionId, result.id)
    }

    // --- TEST: UPDATE (CORREGIDO - Sin any()) ---
    @Test
    fun `SHOULD update question fields GIVEN valid request`() {
        val questionId = 10L
        val categoryId = 1L

        val request = QuestionRequest(categoryId, "Nueva Pregunta", "Nueva Exp", false)

        val category = createCategoryDummy(categoryId)
        val existingQuestion = createQuestionDummy(questionId, category)

        // El servicio modifica existingQuestion en memoria
        val updatedQuestion = createQuestionDummy(questionId, category).apply {
            question = "Nueva Pregunta"
            explanation = "Nueva Exp"
            isActive = false
        }

        val expectedResponse = QuestionResponse(questionId, categoryId, "Nueva Pregunta", "Nueva Exp", false)

        `when`(questionRepositoryMock.findById(questionId)).thenReturn(Optional.of(existingQuestion))

        // CORRECCIÓN: Usamos 'existingQuestion' que es el objeto que se está modificando
        `when`(questionRepositoryMock.save(existingQuestion)).thenReturn(updatedQuestion)
        `when`(questionMapperMock.toResponse(updatedQuestion)).thenReturn(expectedResponse)

        val result = questionService.update(questionId, request)

        assertEquals("Nueva Pregunta", result.question)
        verify(categoryRepositoryMock, never()).findById(anyLong())
    }

    // --- TEST: UPDATE (CORREGIDO - Sin any()) ---
    @Test
    fun `SHOULD update category GIVEN request with different category id`() {
        val questionId = 10L
        val oldCategoryId = 1L
        val newCategoryId = 2L

        val request = QuestionRequest(newCategoryId, "Q", "E", true)

        val oldCategory = createCategoryDummy(oldCategoryId)
        val newCategory = createCategoryDummy(newCategoryId)

        val existingQuestion = createQuestionDummy(questionId, oldCategory)

        `when`(questionRepositoryMock.findById(questionId)).thenReturn(Optional.of(existingQuestion))
        `when`(categoryRepositoryMock.findById(newCategoryId)).thenReturn(Optional.of(newCategory))

        // CORRECCIÓN: Usamos 'existingQuestion' explícitamente
        `when`(questionRepositoryMock.save(existingQuestion)).thenReturn(existingQuestion)

        val response = QuestionResponse(questionId, newCategoryId, "Q", "E", true)
        // CORRECCIÓN: Usamos 'existingQuestion'
        `when`(questionMapperMock.toResponse(existingQuestion)).thenReturn(response)

        questionService.update(questionId, request)

        // Verificamos que buscó la nueva categoría
        verify(categoryRepositoryMock).findById(newCategoryId)
    }

    // --- TEST: DELETE ---
    @Test
    fun `SHOULD delete a question GIVEN a valid id`() {
        val questionId = 10L
        `when`(questionRepositoryMock.existsById(questionId)).thenReturn(true)

        questionService.deleteById(questionId)

        verify(questionRepositoryMock).deleteById(questionId)
    }

    // --- TEST: DELETE (Error) ---
    @Test
    fun `SHOULD throw exception when deleting non existing question`() {
        val questionId = 99L
        `when`(questionRepositoryMock.existsById(questionId)).thenReturn(false)

        assertThrows(NoSuchElementException::class.java) {
            questionService.deleteById(questionId)
        }
    }

    // --- TEST: FIND ALL (CORREGIDO - Sin any()) ---
    @Test
    fun `SHOULD return list of questions`() {
        val category = createCategoryDummy(1L)
        val list = listOf(createQuestionDummy(1L, category))
        val response = QuestionResponse(1L, 1L, "Q", "E", true)

        `when`(questionRepositoryMock.findAll()).thenReturn(list)

        // CORRECCIÓN: Usamos list[0] en vez de any()
        `when`(questionMapperMock.toResponse(list[0])).thenReturn(response)

        val result = questionService.findAll()

        assertEquals(1, result.size)
    }
}