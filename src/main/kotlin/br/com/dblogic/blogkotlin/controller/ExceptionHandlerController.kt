package br.com.dblogic.blogkotlin.controller

import br.com.dblogic.blogkotlin.exception.DeleteTagException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionHandlerController : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = arrayOf(Exception::class))
    fun handleException() : ResponseEntity<HttpStatus> {
        return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(value = arrayOf(DeleteTagException ::class))
    fun handleDeleteTagException() : ResponseEntity<HttpStatus> {
        return ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY)
    }

}