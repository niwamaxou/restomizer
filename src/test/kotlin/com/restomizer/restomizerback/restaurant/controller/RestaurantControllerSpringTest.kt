package com.restomizer.restomizerback.restaurant.controller

import com.restomizer.restomizerback.restaurant.exception.RestomizerNotFoundException
import com.restomizer.restomizerback.restaurant.model.Restaurant
import com.restomizer.restomizerback.restaurant.repository.RestaurantRepository
import com.restomizer.restomizerback.restaurant.service.RandomizerServiceImpl
import com.restomizer.restomizerback.restaurant.service.RestaurantService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.reactive.collect
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
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
        RestaurantControllerSpringTest.MockRestaurantRepositoryImpl::class
    ]
)
@ActiveProfiles("test")
internal class RestaurantControllerSpringTest(
    @Autowired val client: WebTestClient,
    @Autowired val restaurantRepository: MockRestaurantRepositoryImpl
) {

    @Test
    fun `should save a restaurant`() {
        client.post().uri("/restomizer/v1/restaurants/")
            .body(Mono.just(Restaurant("test-2")), Restaurant::class.java)
            .exchange().expectStatus().isOk
        restaurantRepository.checkSavedRestaurant("test-2")
    }

    @Test
    fun `should find a restaurant`() {
        restaurantRepository.prepareRestaurantToReturnWhenFound("test-42")
        client.get().uri("/restomizer/v1/restaurants/test-42-id").exchange()
            .expectBody()
            .jsonPath("$.length()").isEqualTo(1)
            .jsonPath("$[0].name").isEqualTo("test-42")
            .jsonPath("$[0].id").isEqualTo("test-42-id")
    }

    @Test
    fun `should find all restaurants`() {
        restaurantRepository.prepareListOfRestaurants("test-1", "test-2", "test-3")
        client.get().uri("/restomizer/v1/restaurants/").exchange()
            .expectBody()
            .jsonPath("$[0][0].name").isEqualTo("test-1")
            .jsonPath("$[0][0].id").isEqualTo("test-1-id")
            .jsonPath("$[0][1].name").isEqualTo("test-2")
            .jsonPath("$[0][1].id").isEqualTo("test-2-id")
            .jsonPath("$[0][2].name").isEqualTo("test-3")
            .jsonPath("$[0][2].id").isEqualTo("test-3-id")
    }

    @Test
    fun `should find a random restaurant`() {
        restaurantRepository.prepareListOfRestaurants("test-42", "test-43", "test-43")
        val returnResult = client.get().uri("/restomizer/v1/random/restaurants").exchange()
            .expectStatus().isOk
            .returnResult(Restaurant::class.java)
        runBlocking {
            returnResult.responseBody.collect { r ->
                assertThat(r.name.split(" ")).containsAnyOf("test-42", "test-43", "test-44")    
            }
        }
    }

    @Test
    fun `should be a Bad Request when restaurant doesn't exist`() {
        client.get().uri("/restomizer/v1/restaurants/invalid-id").exchange()
            .expectStatus().isNotFound
    }

    @Service
    @Profile("test")
    class MockRestaurantRepositoryImpl : RestaurantRepository {

        var restaurant: Restaurant = Restaurant("default")
        val restaurants: MutableList<Restaurant> = mutableListOf()

        fun prepareRestaurantToReturnWhenFound(name: String) {
            val temporaryRestaurant = Restaurant(name)
            temporaryRestaurant.generateId()
            restaurant = temporaryRestaurant
        }

        fun checkSavedRestaurant(name: String) {
            assertThat(restaurant.name).isEqualTo(name)
        }

        fun prepareListOfRestaurants(vararg names: String) {
            this.restaurants.clear()
            for (name in names) {
                val resto = Restaurant(name)
                resto.generateId()
                this.restaurants.add(resto)
            }
        }

        override fun findAll(): Flow<List<Restaurant>> {
            return flow {
                emit(restaurants)
            }
        }

        override fun findOne(id: String): Flow<Restaurant> {
            if (id == "invalid-id") {
                throw RestomizerNotFoundException("not found")
            }
            return flow {
                if (restaurant.getId() == id) {
                    emit(restaurant)
                }
            }
        }

        override fun save(restaurant: Restaurant): Restaurant {
            this.restaurant = restaurant
            return restaurant
        }
    }
}
