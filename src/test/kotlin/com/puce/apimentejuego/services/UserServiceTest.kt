package com.puce.apimentejuego.services

import com.puce.apimentejuego.exceptions.UserNotFoundException
import com.puce.apimentejuego.mappers.UserMapper
import com.puce.apimentejuego.models.entities.User
import com.puce.apimentejuego.models.requests.UserRequest
import com.puce.apimentejuego.models.responses.UserResponse
import com.puce.apimentejuego.repositories.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.Optional
import java.time.LocalDateTime

class UserServiceTest {

    private lateinit var userRepositoryMock: UserRepository
    private lateinit var userMapperMock: UserMapper
    private lateinit var passwordEncoderMock: PasswordEncoder
    private lateinit var userService: UserService

    @BeforeEach
    fun init() {
        userRepositoryMock = mock(UserRepository::class.java)
        userMapperMock = mock(UserMapper::class.java)
        passwordEncoderMock = mock(PasswordEncoder::class.java)

        userService = UserService(
            userRepository = userRepositoryMock,
            userMapper = userMapperMock,
            passwordEncoder = passwordEncoderMock
        )
    }

    // --- HELPER METHOD ---
    private fun createUserDummy(id: Long): User {
        return User(
            firstName = "Test",
            lastName = "User",
            email = "test@mail.com",
            password = "123",
            username = "testuser"
        ).apply { this.id = id }
    }

    // --- TEST: CREATE ---
    @Test
    fun `SHOULD save a user GIVEN a valid request`() {
        val request = UserRequest("Ariel", "Test", "ariel123", "a@a.com", "123")

        // Usamos datos dummy completos para evitar errores
        val userEntity = createUserDummy(0L)
        val savedUser = createUserDummy(1L)

        val expectedResponse = UserResponse(
            id = 1L,
            username = "testuser",
            email = "test@mail.com",
            createdAt = LocalDateTime.now(),
            firstName = "Test",
            lastName = "User"
        )

        // Mock para validaciones de duplicados
        `when`(userRepositoryMock.existsByUsername(request.username!!)).thenReturn(false)
        `when`(userRepositoryMock.existsByEmail(request.email!!)).thenReturn(false)

        // Mock para passwordEncoder
        `when`(passwordEncoderMock.encode(request.password)).thenReturn("hashedPassword123")

        `when`(userMapperMock.toEntity(request)).thenReturn(userEntity)
        `when`(userRepositoryMock.save(userEntity)).thenReturn(savedUser)
        `when`(userMapperMock.toResponse(savedUser)).thenReturn(expectedResponse)

        val result = userService.save(request)

        assertEquals(1L, result.id)
        verify(userRepositoryMock, times(1)).save(userEntity)
    }

    // --- TEST: FIND BY ID ---
    @Test
    fun `SHOULD return a user response GIVEN a valid user id`() {
        val userId = 1L
        val userEntity = createUserDummy(userId)

        val expectedResponse = UserResponse(
            id = userId,
            username = "testuser",
            email = "test@mail.com",
            createdAt = LocalDateTime.now(),
            firstName = "Test",
            lastName = "User"
        )

        `when`(userRepositoryMock.findById(userId)).thenReturn(Optional.of(userEntity))
        `when`(userMapperMock.toResponse(userEntity)).thenReturn(expectedResponse)

        val result = userService.findById(userId)

        assertEquals(userId, result.id)
    }

    // --- TEST: FIND BY ID (Error) ---
    @Test
    fun `SHOULD throw UserNotFoundException GIVEN a non existing user id`() {
        val userId = 99L
        `when`(userRepositoryMock.findById(userId)).thenReturn(Optional.empty())

        assertThrows(UserNotFoundException::class.java) {
            userService.findById(userId)
        }
    }

    // --- TEST: UPDATE (CORREGIDO) ---
    @Test
    fun `SHOULD update a user GIVEN a valid id and request`() {
        val userId = 1L
        val request = UserRequest("Nuevo", "Nombre", "newuser", "new@mail.com", "pass")

        val existingUser = createUserDummy(userId)
        // Simulamos el usuario que devuelve el save()
        val updatedUser = createUserDummy(userId).apply { username = "newuser" }

        val expectedResponse = UserResponse(
            id = userId,
            username = "newuser",
            email = "new@mail.com",
            createdAt = LocalDateTime.now(),
            firstName = "Nuevo",
            lastName = "Nombre"
        )

        `when`(userRepositoryMock.findById(userId)).thenReturn(Optional.of(existingUser))

        // Mock para validaciones de duplicados (simulamos que no hay duplicados)
        `when`(userRepositoryMock.existsByUsername(request.username!!)).thenReturn(false)
        `when`(userRepositoryMock.existsByEmail(request.email!!)).thenReturn(false)

        // Mock para passwordEncoder
        `when`(passwordEncoderMock.encode(request.password)).thenReturn("hashedPassword123")

        `when`(userRepositoryMock.save(existingUser)).thenReturn(updatedUser)
        `when`(userMapperMock.toResponse(updatedUser)).thenReturn(expectedResponse)

        val result = userService.update(userId, request)

        assertEquals("newuser", result.username)
    }

    // --- TEST: DELETE ---
    @Test
    fun `SHOULD delete a user GIVEN a valid user id`() {
        val userId = 1L
        `when`(userRepositoryMock.existsById(userId)).thenReturn(true)

        userService.deleteById(userId)

        verify(userRepositoryMock, times(1)).deleteById(userId)
    }

    // --- TEST: DELETE (Error) ---
    @Test
    fun `SHOULD throw exception when deleting GIVEN a non existing user id`() {
        val userId = 99L
        `when`(userRepositoryMock.existsById(userId)).thenReturn(false)

        assertThrows(UserNotFoundException::class.java) {
            userService.deleteById(userId)
        }

        verify(userRepositoryMock, never()).deleteById(anyLong())
    }

    // --- TEST: FIND ALL ---
    @Test
    fun `SHOULD return a list of users`() {
        val userList = listOf(createUserDummy(1L))

        val responseList = listOf(UserResponse(
            id = 1L,
            username = "testuser",
            email = "test@mail.com",
            createdAt = LocalDateTime.now(),
            firstName = "Test",
            lastName = "User"
        ))

        `when`(userRepositoryMock.findAll()).thenReturn(userList)
        `when`(userMapperMock.toResponse(userList[0])).thenReturn(responseList[0])

        val result = userService.findAll()

        assertFalse(result.isEmpty())
        assertEquals(1, result.size)
    }
}