package com.restomizer.restomizerback.restaurant.controller

import com.restomizer.restomizerback.restaurant.model.Restaurant
import com.restomizer.restomizerback.restaurant.repository.RestaurantHashMapRepositoryImpl
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [RestaurantController::class])
@Import(value = [RestaurantHashMapRepositoryImpl::class])
internal class RestaurantControllerSpringTest(
    @Autowired val client: WebTestClient
) {

    @Test
    fun `should save and get restaurants`() {
        val restaurantTest1 = Restaurant("test 1")
        val restaurantTest2 = Restaurant("test 2")
        val restaurantTest3 = Restaurant("test 3")
        client.post().uri("/restomizer/v1/restaurants/")
            .body(Mono.just(restaurantTest1), Restaurant::class.java)
            .exchange().expectStatus().isOk
        client.post().uri("/restomizer/v1/restaurants/")
            .body(Mono.just(restaurantTest2), Restaurant::class.java)
            .exchange().expectStatus().isOk
        client.post().uri("/restomizer/v1/restaurants/")
            .body(Mono.just(restaurantTest3), Restaurant::class.java)
            .exchange().expectStatus().isOk
        client.get().uri("/restomizer/v1/restaurants/").exchange()
            .expectBody().jsonPath("$..name").value(containsInAnyOrder("test 1", "test 2", "test 3"))
        client.get().uri("/restomizer/v1/restaurants/${restaurantTest3.id}").exchange()
            .expectBody()
            .jsonPath("$..name").isEqualTo("test 3")
            .jsonPath("$..id").isEqualTo(restaurantTest3.id)
    }
    
    @Test
    fun `should be a Bad Request when restaurant doesn't exist`() {
        client.get().uri("/restomizer/v1/restaurants/invalid-id").exchange()
            .expectStatus().isBadRequest
    }
}
