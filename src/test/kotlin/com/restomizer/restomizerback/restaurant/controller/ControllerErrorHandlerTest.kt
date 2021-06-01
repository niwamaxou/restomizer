package com.restomizer.restomizerback.restaurant.controller

import com.restomizer.restomizerback.restaurant.exception.RestomizerException
import com.restomizer.restomizerback.restaurant.exception.RestomizerNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

internal class ControllerErrorHandlerTest {

    @Test
    fun `should return with not found`() {
        val controllerErrorHandler = ControllerErrorHandler()
        val error = controllerErrorHandler.handleError(RestomizerNotFoundException("test-1"))
        assertThat(error.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(error.body).isEqualTo("not found")
    }

    @Test
    fun `should return default error`() {
        val controllerErrorHandler = ControllerErrorHandler()
        val error = controllerErrorHandler.handleError(RestomizerException("test-2"))
        assertThat(error.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(error.body).isEqualTo("exception")
    }
}

