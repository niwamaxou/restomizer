package com.restomizer.restomizerback.restaurant.controller

import com.restomizer.restomizerback.restaurant.exception.RestomizerNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerErrorHandler {

    @ExceptionHandler
    fun handleError(exception: Exception): ResponseEntity<String> {

        if (exception is RestomizerNotFoundException) {
            return ResponseEntity("not found", HttpStatus.NOT_FOUND)
        }

        return ResponseEntity("exception", HttpStatus.BAD_REQUEST)
    }

}
