package com.restomizer.restomizerback.restaurant.controller

import com.restomizer.restomizerback.restaurant.model.Restaurant
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [RestaurantController::class])
internal class RestaurantControllerSpringTest(private val client : WebTestClient) {
    
    @Test
    fun `should get the restaurants`() {
        val expected = listOf(Restaurant("test 1"))
        client.get().uri("/restomizer/v1/restaurant/").exchange()
            .expectStatus().isOk
            .expectBody<List<Restaurant>>().isEqualTo(expected)
    }
}
