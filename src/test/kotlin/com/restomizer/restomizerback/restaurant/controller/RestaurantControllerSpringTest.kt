package com.restomizer.restomizerback.restaurant.controller

import com.restomizer.restomizerback.restaurant.model.Restaurant
import com.restomizer.restomizerback.restaurant.service.RandomizerServiceImpl
import com.restomizer.restomizerback.restaurant.service.RestaurantService
import com.restomizer.restomizerback.restaurant.utils.MockRestaurantSpringRepositoryImpl
import kotlinx.coroutines.reactive.collect
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [RestaurantController::class])
@Import(
    value = [
        RestaurantService::class,
        RandomizerServiceImpl::class,
        MockRestaurantSpringRepositoryImpl::class
    ]
)
@ActiveProfiles("test")
internal class RestaurantControllerSpringTest @Autowired constructor(
    private val client: WebTestClient,
    private val mockRestaurantSpringRepositoryImpl: MockRestaurantSpringRepositoryImpl
) {

    @Test
    fun `should save 3 restaurants, get them and get one randomly`() {
        
        client.post().uri("/restomizer/v1/restaurants/")
            .body(Mono.just(Restaurant(name = "test-1")), Restaurant::class.java)
            .exchange()
            .expectStatus().isCreated
            .expectHeader().location("/restomizer/v1/restaurants/test-1-id")
        client.post().uri("/restomizer/v1/restaurants/")
            .body(Mono.just(Restaurant(name = "test-2")), Restaurant::class.java)
            .exchange()
            .expectStatus().isCreated
            .expectHeader().location("/restomizer/v1/restaurants/test-2-id")
        client.post().uri("/restomizer/v1/restaurants/")
            .body(Mono.just(Restaurant(name = "test-3")), Restaurant::class.java)
            .exchange()
            .expectStatus().isCreated
            .expectHeader().location("/restomizer/v1/restaurants/test-3-id")
        client.get().uri("/restomizer/v1/restaurants/").exchange()
            .expectBody()
            .jsonPath("$..name").value(containsInAnyOrder("test-1", "test-2", "test-3"))
            .jsonPath("$..id").value(containsInAnyOrder("test-1-id", "test-2-id", "test-3-id"))
        client.get().uri("/restomizer/v1/restaurants/test-2-id").exchange()
            .expectBody()
            .jsonPath("$.name").isEqualTo("test-2")
            .jsonPath("$.id").isEqualTo("test-2-id")
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
    fun `should have http status 404 when restaurant doesn't exist`() {
        client.get().uri("/restomizer/v1/restaurants/invalid-id").exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should have http status 400 when any unexcepted exception occurs`() {
        mockRestaurantSpringRepositoryImpl.doThrowAnExceptionWhenSave()
        client.post().uri("/restomizer/v1/restaurants/")
            .body(Mono.just(Restaurant(name = "test-1")), Restaurant::class.java)
            .exchange().expectStatus().isBadRequest
    }
}
