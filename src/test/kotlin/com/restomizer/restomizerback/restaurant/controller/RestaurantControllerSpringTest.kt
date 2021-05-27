package com.restomizer.restomizerback.restaurant.controller

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [RestaurantController::class])
internal class RestaurantControllerSpringTest(@Autowired val client : WebTestClient) {
    
    @Test
    fun `should get the restaurants`() {
        client.get().uri("/restomizer/v1/restaurant/").exchange()
            .expectStatus().isOk
            .expectBody<String>().isEqualTo("Hello World")
    }
}
