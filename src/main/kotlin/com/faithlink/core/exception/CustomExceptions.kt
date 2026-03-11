package com.faithlink.core.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.CONFLICT)
class DuplicateResourceException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidRequestException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class UnauthorizedException(message: String = "Unauthorized access") : RuntimeException(message)

@ResponseStatus(HttpStatus.FORBIDDEN)
class ForbiddenException(message: String = "Access denied") : RuntimeException(message)
