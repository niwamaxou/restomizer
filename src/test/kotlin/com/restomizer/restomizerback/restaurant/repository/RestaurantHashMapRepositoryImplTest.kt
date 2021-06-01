package com.restomizer.restomizerback.restaurant.repository

import com.restomizer.restomizerback.restaurant.exception.RestomizerNotFoundException
import com.restomizer.restomizerback.restaurant.model.Restaurant
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RestaurantHashMapRepositoryImplTest {

    @Test
    fun `should save a restaurant and find it`() {
        val restaurantHashMapRepository = RestaurantHashMapRepositoryImpl()
        val restaurant = Restaurant(name = "test-1")
        runBlocking {
            val savedRestaurant = restaurantHashMapRepository.save(restaurant)
            val flowRestaurantExpected = restaurantHashMapRepository.findById("test-1-id")
            assertThat(flowRestaurantExpected).isEqualTo(savedRestaurant)
        }
    }

    @Test
    fun `should throw notFoundException if no restaurant is found`() {
        val restaurantHashMapRepository = RestaurantHashMapRepositoryImpl()
        val restaurant = Restaurant(name = "test-1")
        runBlocking {
            restaurantHashMapRepository.save(restaurant)
            try {
                restaurantHashMapRepository.findById("invalid-id")
            } catch (restomizerNotFoundException: RestomizerNotFoundException) {
                assertThat(restomizerNotFoundException.message).isEqualTo("No restaurant found with id [invalid-id]")
            }
        }
    }

    @Test
    fun `should find all restaurant`() {
        val restaurantHashMapRepository = RestaurantHashMapRepositoryImpl()
        val restaurant1 = Restaurant(name = "test-1")
        val restaurant2 = Restaurant(name = "test-2")
        val restaurant3 = Restaurant(name = "test-3")
        runBlocking {
            restaurantHashMapRepository.save(restaurant1)
            restaurantHashMapRepository.save(restaurant2)
            restaurantHashMapRepository.save(restaurant3)
            val flowRestaurantsExpected = restaurantHashMapRepository.findAll().toList()
            assertThat(flowRestaurantsExpected).extracting("name").containsExactlyInAnyOrder("test-1", "test-2", "test-3")
        }
    }
}
