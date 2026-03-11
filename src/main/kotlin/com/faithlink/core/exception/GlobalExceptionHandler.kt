package com.faithlink.core.exception

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val message: String,
    val path: String? = null,
    val details: Map<String, String>? = null
)

@RestControllerAdvice
class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(
        ex: ResourceNotFoundException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = "Resource Not Found",
            message = ex.message ?: "Resource not found",
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }
    
    @ExceptionHandler(DuplicateResourceException::class)
    fun handleDuplicateResourceException(
        ex: DuplicateResourceException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            error = "Duplicate Resource",
            message = ex.message ?: "Resource already exists",
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse)
    }
    
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        ex: IllegalArgumentException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = ex.message ?: "Invalid argument provided",
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }
    
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
        ex: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val details = mutableMapOf<String, String>()
        ex.bindingResult.fieldErrors.forEach { error ->
            details[error.field] = error.defaultMessage ?: "Invalid value"
        }
        
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation Error",
            message = "Request validation failed",
            path = request.getDescription(false).replace("uri=", ""),
            details = details
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }
    
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        ex: ConstraintViolationException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val details = mutableMapOf<String, String>()
        ex.constraintViolations.forEach { violation ->
            val fieldName = violation.propertyPath.toString()
            details[fieldName] = violation.message ?: "Invalid value"
        }
        
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation Error",
            message = "Request validation failed",
            path = request.getDescription(false).replace("uri=", ""),
            details = details
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }
    
    @ExceptionHandler(Exception::class)
    fun handleGenericException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Internal Server Error",
            message = "An unexpected error occurred: ${ex.message}",
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}
