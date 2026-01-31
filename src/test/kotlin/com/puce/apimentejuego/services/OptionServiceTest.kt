package com.puce.apimentejuego.services

import com.puce.apimentejuego.exceptions.OptionIdNotFoundException
import com.puce.apimentejuego.mappers.OptionMapper
import com.puce.apimentejuego.models.entities.Option
import com.puce.apimentejuego.models.requests.OptionRequest
import com.puce.apimentejuego.models.responses.OptionResponse
import com.puce.apimentejuego.repositories.OptionRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.Optional

class OptionServiceTest {

    // 1. Mocks
    private lateinit var optionRepositoryMock: OptionRepository
    private lateinit var optionMapperMock: OptionMapper

    // 2. Servicio
    private lateinit var optionService: OptionService

    @BeforeEach
    fun init() {
        optionRepositoryMock = mock(OptionRepository::class.java)
        optionMapperMock = mock(OptionMapper::class.java)

        optionService = OptionService(
            optionRepositoryMock,
            optionMapperMock
        )
    }

    // --- HELPER METHOD ---
    private fun createOptionDummy(id: Long): Option {
        return Option(
            possibleAnswer = "Respuesta de prueba $id"
        ).apply { this.id = id }
    }

    // --- TEST: CREATE ---
    @Test
    fun `SHOULD save an option GIVEN a valid request`() {
        val request = OptionRequest("Respuesta Correcta")

        val optionEntity = createOptionDummy(0L) // Sin ID
        val savedOption = createOptionDummy(1L)  // Con ID

        val expectedResponse = OptionResponse(1L, "Respuesta Correcta")

        `when`(optionMapperMock.toEntity(request)).thenReturn(optionEntity)
        `when`(optionRepositoryMock.save(optionEntity)).thenReturn(savedOption)
        `when`(optionMapperMock.toResponse(savedOption)).thenReturn(expectedResponse)

        val result = optionService.save(request)

        assertEquals(1L, result.id)
        verify(optionRepositoryMock, times(1)).save(optionEntity)
    }

    // --- TEST: FIND BY ID ---
    @Test
    fun `SHOULD return an option response GIVEN a valid id`() {
        val optionId = 1L
        val optionEntity = createOptionDummy(optionId)
        val expectedResponse = OptionResponse(optionId, "Respuesta de prueba 1")

        `when`(optionRepositoryMock.findById(optionId)).thenReturn(Optional.of(optionEntity))
        `when`(optionMapperMock.toResponse(optionEntity)).thenReturn(expectedResponse)

        val result = optionService.findById(optionId)

        assertEquals(1L, result.id)
    }

    // --- TEST: FIND BY ID (Error) ---
    @Test
    fun `SHOULD throw OptionIdNotFoundException GIVEN a non existing id`() {
        val optionId = 99L
        `when`(optionRepositoryMock.findById(optionId)).thenReturn(Optional.empty())

        assertThrows(OptionIdNotFoundException::class.java) {
            optionService.findById(optionId)
        }
    }

    // --- TEST: FIND ALL ---
    @Test
    fun `SHOULD return a list of options`() {
        val list = listOf(createOptionDummy(1L))
        val response = OptionResponse(1L, "R")

        `when`(optionRepositoryMock.findAll()).thenReturn(list)
        `when`(optionMapperMock.toResponse(list[0])).thenReturn(response)

        val result = optionService.findAll()

        assertFalse(result.isEmpty())
        assertEquals(1, result.size)
    }

    // --- TEST: UPDATE ---
    @Test
    fun `SHOULD update an option GIVEN a valid id and request`() {
        val optionId = 1L
        val request = OptionRequest("Respuesta Actualizada")

        val existingOption = createOptionDummy(optionId)
        // El servicio modifica el objeto existente en memoria, así que esperamos ese mismo objeto

        val updatedOption = createOptionDummy(optionId).apply {
            possibleAnswer = "Respuesta Actualizada"
        }

        val expectedResponse = OptionResponse(optionId, "Respuesta Actualizada")

        `when`(optionRepositoryMock.findById(optionId)).thenReturn(Optional.of(existingOption))
        `when`(optionRepositoryMock.save(existingOption)).thenReturn(updatedOption)
        `when`(optionMapperMock.toResponse(updatedOption)).thenReturn(expectedResponse)

        val result = optionService.update(optionId, request)

        assertEquals("Respuesta Actualizada", result.possibleAnswer)
    }

    // --- TEST: UPDATE (Error) ---
    @Test
    fun `SHOULD throw OptionIdNotFoundException when updating non existing option`() {
        val optionId = 99L
        val request = OptionRequest("R")

        `when`(optionRepositoryMock.findById(optionId)).thenReturn(Optional.empty())

        assertThrows(OptionIdNotFoundException::class.java) {
            optionService.update(optionId, request)
        }
    }

    // --- TEST: DELETE ---
    @Test
    fun `SHOULD delete an option GIVEN a valid id`() {
        val optionId = 1L
        `when`(optionRepositoryMock.existsById(optionId)).thenReturn(true)

        optionService.deleteById(optionId)

        verify(optionRepositoryMock).deleteById(optionId)
    }

    // --- TEST: DELETE (Error) ---
    @Test
    fun `SHOULD throw OptionIdNotFoundException when deleting non existing option`() {
        val optionId = 99L
        `when`(optionRepositoryMock.existsById(optionId)).thenReturn(false)

        assertThrows(OptionIdNotFoundException::class.java) {
            optionService.deleteById(optionId)
        }

        verify(optionRepositoryMock, never()).deleteById(anyLong())
    }
}