package com.restomizer.restomizerback.restaurant.controller

import com.restomizer.restomizerback.restaurant.model.Restaurant
import com.restomizer.restomizerback.restaurant.repository.RestaurantHashMapRepositoryImpl
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.collection.IsIterableContainingInAnyOrder
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
    @Autowired val client: WebTestClient,
    @Autowired val restaurantHashMapRepository: RestaurantHashMapRepositoryImpl
) {

    @Test
    fun `should get the restaurants`() {
        val restaurantTest1 = Restaurant("test 1")
        val restaurantTest2 = Restaurant("test 2")
        restaurantHashMapRepository.save(restaurantTest1)
        restaurantHashMapRepository.save(restaurantTest2)
        client.get().uri("/restomizer/v1/restaurants/").exchange()
            .expectBody().jsonPath("$..name").value(containsInAnyOrder("test 1", "test 2"))
        
    }

    @Test
    fun `should save a restaurant`() {
        val restaurant = Restaurant("Auberge des 3 renards")
        client.post().uri("/restomizer/v1/restaurants/")
            .body(Mono.just(restaurant), Restaurant::class.java)
            .exchange().expectStatus().isOk
        val flowRestaurantExpected = restaurantHashMapRepository.findOne(restaurant.id)
        runBlocking {
            flowRestaurantExpected.collect { r ->
                assertThat(r).isEqualTo(restaurant)
            }
        }
    }
}
