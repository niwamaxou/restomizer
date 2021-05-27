package com.restomizer.restomizerback.restaurant.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerErrorHandler {

    @ExceptionHandler()
    fun handleError(exception : Exception) : ResponseEntity<String> {
        
        return ResponseEntity("exception", HttpStatus.BAD_REQUEST)
    }
    
}
