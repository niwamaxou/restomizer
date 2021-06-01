package com.restomizer.restomizerback.restaurant.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.restomizer.restomizerback.restaurant.model.Restaurant
import com.restomizer.restomizerback.restaurant.repository.RestaurantHashMapRepositoryImpl
import com.restomizer.restomizerback.restaurant.service.RandomizerServiceImpl
import com.restomizer.restomizerback.restaurant.service.RestaurantService
import kotlinx.coroutines.reactive.collect
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [RestaurantController::class])
@Import(
    value = [
        RestaurantService::class,
        RandomizerServiceImpl::class,
        RestaurantHashMapRepositoryImpl::class
    ]
)
internal class RestaurantControllerSpringTest(
    @Autowired val client: WebTestClient
) {

    @Test
    fun `should save a restaurant`() {

        val objectMapper = ObjectMapper()
        val restaurant1Returned = client.post().uri("/restomizer/v1/restaurants/")
            .body(Mono.just(Restaurant("test-1")), Restaurant::class.java)
            .exchange().expectStatus().isOk.returnResult(ResponseEntity::class.java)
        val restaurant2Returned = client.post().uri("/restomizer/v1/restaurants/")
            .body(Mono.just(Restaurant("test-2")), Restaurant::class.java)
            .exchange().expectStatus().isOk.returnResult(Restaurant::class.java)
        val restaurant3Returned = client.post().uri("/restomizer/v1/restaurants/")
            .body(Mono.just(Restaurant("test-3")), Restaurant::class.java)
            .exchange().expectStatus().isOk.returnResult(Restaurant::class.java)
        val restaurant1 = objectMapper.readValue(restaurant1Returned.responseBodyContent, Restaurant::class.java)
        val restaurant2 = objectMapper.readValue(restaurant2Returned.responseBodyContent, Restaurant::class.java)
        val restaurant3 = objectMapper.readValue(restaurant3Returned.responseBodyContent, Restaurant::class.java)
        client.get().uri("/restomizer/v1/restaurants/").exchange()
            .expectBody()
            .jsonPath("$..name").value(containsInAnyOrder("test-1", "test-2", "test-3"))
            .jsonPath("$..id").value(containsInAnyOrder(restaurant1.getId(), restaurant2.getId(), restaurant3.getId()))
        client.get().uri("/restomizer/v1/restaurants/${restaurant2.getId()}").exchange()
            .expectBody()
            .jsonPath("$.length()").isEqualTo(1)
            .jsonPath("$[0].name").isEqualTo("test-2")
            .jsonPath("$[0].id").isEqualTo("test-2-id")
        val returnResult = client.get().uri("/restomizer/v1/random/restaurants").exchange()
            .expectStatus().isOk
            .returnResult(Restaurant::class.java)
        runBlocking {
            returnResult.responseBody.collect { r ->
                assertThat(r.name.split(" ")).containsAnyOf("test-1", "test-2", "test-3")
            }
        }
    }

    @Test
    fun `should be a Bad Request when restaurant doesn't exist`() {
        client.get().uri("/restomizer/v1/restaurants/invalid-id").exchange()
            .expectStatus().isNotFound
    }
}
