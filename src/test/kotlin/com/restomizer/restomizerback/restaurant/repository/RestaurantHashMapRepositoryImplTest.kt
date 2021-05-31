package com.restomizer.restomizerback.restaurant.repository

import com.restomizer.restomizerback.restaurant.exception.RestomizerNotFoundException
import com.restomizer.restomizerback.restaurant.model.Restaurant
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RestaurantHashMapRepositoryImplTest {

    @Test
    fun `should save a restaurant and find it`() {
        val restaurantHashMapRepository = RestaurantHashMapRepositoryImpl()
        val restaurant = Restaurant("test-1")
        restaurantHashMapRepository.save(restaurant)
        val flowRestaurantExpected = restaurantHashMapRepository.findOne("test-1-id")
        runBlocking {
            flowRestaurantExpected.collect { r ->
                assertThat(r).isEqualTo(restaurant)
            }
        }
    }

    @Test
    fun `should throw notFoundException if no restaurant is found`() {
        val restaurantHashMapRepository = RestaurantHashMapRepositoryImpl()
        val restaurant = Restaurant("test-1")
        restaurantHashMapRepository.save(restaurant)
        val flowShouldThrowAnException = restaurantHashMapRepository.findOne("fake-id")
        runBlocking {
            flowShouldThrowAnException
                .catch { e ->
                    assertThat(e)
                        .isInstanceOf(RestomizerNotFoundException::class.java)
                        .hasMessage("No restaurant found with id [fake-id]")
                }
        }
    }

    @Test
    fun `should find all restaurant`() {
        val restaurantHashMapRepository = RestaurantHashMapRepositoryImpl()
        val restaurant1 = Restaurant("test-1")
        val restaurant2 = Restaurant("test-2")
        val restaurant3 = Restaurant("test-3")
        restaurantHashMapRepository.save(restaurant1)
        restaurantHashMapRepository.save(restaurant2)
        restaurantHashMapRepository.save(restaurant3)
        val flowRestaurantsExpected = restaurantHashMapRepository.findAll()
        runBlocking {
            flowRestaurantsExpected.collect { lr ->
                assertThat(lr).extracting("name")
                    .contains("test-1", "test-2", "test-3")
            }
        }
    }
}
