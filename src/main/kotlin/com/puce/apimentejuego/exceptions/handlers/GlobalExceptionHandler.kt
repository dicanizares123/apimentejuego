package com.puce.apimentejuego.exceptions.handlers
import com.puce.apimentejuego.models.responses.ErrorResponse
import com.puce.apimentejuego.exceptions.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(
        ex: UserNotFoundException
    ): ResponseEntity<ErrorResponse>{
       return ResponseEntity(
           ErrorResponse(ex.message),
           HttpStatus.NOT_FOUND
       )
    }

}