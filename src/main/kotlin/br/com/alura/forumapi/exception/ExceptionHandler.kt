package br.com.alura.forumapi.exception

import br.com.alura.forumapi.domain.dto.error.GetErrorDto
import br.com.alura.forumapi.exception.classes.NotFoundException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequestError(
        exception: MethodArgumentNotValidException,
        request: HttpServletRequest,
    ): GetErrorDto {
        val errorMessage = exception.bindingResult.fieldErrors.associate { error ->
            Pair(error.field, error.defaultMessage)
        }

        return GetErrorDto(
            statusCode = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.name,
            message = errorMessage.toString(),
            path = request.servletPath,
        )
    }

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundError(
        exception: NotFoundException,
        request: HttpServletRequest,
    ): GetErrorDto {
        return GetErrorDto(
            statusCode = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.name,
            message = exception.message,
            path = request.servletPath,
        )
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleInternalServerError(
        exception: NotFoundException,
        request: HttpServletRequest,
    ): GetErrorDto {
        return GetErrorDto(
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = HttpStatus.INTERNAL_SERVER_ERROR.name,
            message = exception.message,
            path = request.servletPath,
        )
    }
}
