package com.restomizer.restomizerback.restaurant.exception

import org.springframework.http.HttpStatus

class RestomizerNotFoundException(message: String?) : RestomizerException(message) {

    val httpStatus = HttpStatus.BAD_REQUEST
}
