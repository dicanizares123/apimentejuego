package com.puce.apimentejuego.exceptions.handlers

import com.puce.apimentejuego.exceptions.*
import com.puce.apimentejuego.models.responses.ErrorResponse
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
           ErrorResponse(ex.message ?: "User not found"),
           HttpStatus.NOT_FOUND
       )
    }

    @ExceptionHandler(QuestionIdNotFoundException::class)
    fun handleQuestionIdNotFoundException(
        ex: QuestionIdNotFoundException
    ): ResponseEntity<ErrorResponse>{
       return ResponseEntity(
           ErrorResponse(ex.message ?: "Question ID not found"),
           HttpStatus.NOT_FOUND
       )
    }

    @ExceptionHandler(OptionIdNotFoundException::class)
    fun handleOptionIdNotFoundException(
        ex: OptionIdNotFoundException
    ): ResponseEntity<ErrorResponse>{
       return ResponseEntity(
           ErrorResponse(ex.message ?: "Option ID not found"),
           HttpStatus.NOT_FOUND
       )
    }

    @ExceptionHandler(CategoryNotFoundException::class)
    fun handleCategoryNotFoundException(
        ex: CategoryNotFoundException
    ): ResponseEntity<ErrorResponse>{
       return ResponseEntity(
           ErrorResponse(ex.message ?: "Category not found"),
           HttpStatus.NOT_FOUND
       )
    }

    @ExceptionHandler(QuestionOptionNotFoundException::class)
    fun handleQuestionOptionNotFoundException(
        ex: QuestionOptionNotFoundException
    ): ResponseEntity<ErrorResponse>{
       return ResponseEntity(
           ErrorResponse(ex.message ?: "Question Option not found"),
           HttpStatus.NOT_FOUND
       )
    }

    @ExceptionHandler(GameNotFoundException::class)
    fun handleGameNotFoundException(
        ex: GameNotFoundException
    ): ResponseEntity<ErrorResponse>{
       return ResponseEntity(
           ErrorResponse(ex.message ?: "Game not found"),
           HttpStatus.NOT_FOUND
       )
    }

    @ExceptionHandler(MissingParameterException::class)
    fun handleMissingParameterException(
        ex: MissingParameterException
    ): ResponseEntity<ErrorResponse>{
       return ResponseEntity(
           ErrorResponse(ex.message ?: "Missing required parameter"),
           HttpStatus.BAD_REQUEST
       )
    }

    @ExceptionHandler(DuplicateResourceException::class)
    fun handleDuplicateResourceException(
        ex: DuplicateResourceException
    ): ResponseEntity<ErrorResponse>{
       return ResponseEntity(
           ErrorResponse(ex.message ?: "Resource already exists"),
           HttpStatus.CONFLICT
       )
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(
        ex: NoSuchElementException
    ): ResponseEntity<ErrorResponse>{
       return ResponseEntity(
           ErrorResponse(ex.message ?: "Resource not found"),
           HttpStatus.NOT_FOUND
       )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        ex: IllegalArgumentException
    ): ResponseEntity<ErrorResponse>{
       return ResponseEntity(
           ErrorResponse(ex.message ?: "Invalid argument"),
           HttpStatus.BAD_REQUEST
       )
    }
}