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
            .body(Mono.just(Restaurant("test 2")), Restaurant::class.java)
            .exchange().expectStatus().isOk
        restaurantRepository.checkSavedRestaurant("test 2")
    }

    @Test
    fun `should find a restaurant`() {
        val restaurant = Restaurant("test 42")
        restaurantRepository.doReturnWhenFindOne(restaurant)
        client.get().uri("/restomizer/v1/restaurants/${restaurant.id}").exchange()
            .expectBody()
            .jsonPath("$.length()").isEqualTo(1)
            .jsonPath("$[0].name").isEqualTo("test 42")
            .jsonPath("$[0].id").isEqualTo(restaurant.id)
    }

    @Test
    fun `should find all restaurants`() {
        val restaurant1 = Restaurant("test 1")
        val restaurant2 = Restaurant("test 2")
        val restaurant3 = Restaurant("test 3")
        val restaurants = listOf<Restaurant>(
            restaurant1,
            restaurant2,
            restaurant3,
        )
        restaurantRepository.prepareListOfRestaurants(restaurants)
        client.get().uri("/restomizer/v1/restaurants/").exchange()
            .expectBody()
            .jsonPath("$[0][0].name").isEqualTo("test 1")
            .jsonPath("$[0][0].id").isEqualTo(restaurant1.id)
            .jsonPath("$[0][1].name").isEqualTo("test 2")
            .jsonPath("$[0][1].id").isEqualTo(restaurant2.id)
            .jsonPath("$[0][2].name").isEqualTo("test 3")
            .jsonPath("$[0][2].id").isEqualTo(restaurant3.id)
    }

    @Test
    fun `should find a random restaurant`() {
        val restaurant1 = Restaurant("test42")
        val restaurant2 = Restaurant("test43")
        val restaurant3 = Restaurant("test44")
        val restaurants = listOf<Restaurant>(
            restaurant1,
            restaurant2,
            restaurant3,
        )
        restaurantRepository.prepareListOfRestaurants(restaurants)
        val returnResult = client.get().uri("/restomizer/v1/random/restaurants").exchange()
            .expectStatus().isOk
            .returnResult(Restaurant::class.java)
        runBlocking {
            returnResult.responseBody.collect { r ->
                assertThat(r.name.split(" ")).containsAnyOf("test42", "test43", "test44")    
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
        var restaurants: List<Restaurant> = listOf()

        fun doReturnWhenFindOne(restaurant: Restaurant) {
            this.restaurant = restaurant
        }

        fun checkSavedRestaurant(name: String) {
            assertThat(restaurant.name).isEqualTo(name)
        }

        fun prepareListOfRestaurants(restaurants: List<Restaurant>) {
            this.restaurants = restaurants
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
                if (restaurant.id == id) {
                    emit(restaurant)
                }
            }
        }

        override fun save(restaurant: Restaurant) {
            this.restaurant = restaurant
        }
    }
}
