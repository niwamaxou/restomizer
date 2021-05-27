package com.restomizer.restomizerback.restaurant.controller

import com.restomizer.restomizerback.restaurant.exception.RestomizerException
import com.restomizer.restomizerback.restaurant.exception.RestomizerNotFoundException
import com.restomizer.restomizerback.restaurant.model.Restaurant
import com.restomizer.restomizerback.restaurant.repository.RestaurantHashMapRepositoryImpl
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RestaurantControllerAbstractedTest {

    @Test
    fun `should get the restaurants`() {
        val restaurantHashMapRepository = RestaurantHashMapRepositoryImpl()
        val restaurantTest1 = Restaurant("test 1")
        val restaurantTest2 = Restaurant("test 2")
        restaurantHashMapRepository.save(restaurantTest1)
        restaurantHashMapRepository.save(restaurantTest2)
        val restaurantController = RestaurantController(restaurantHashMapRepository)
        val restaurants = restaurantController.findAll()
        runBlocking {
            restaurants.collect { lr ->
                assertThat(lr).extracting("name")
                    .contains("test 1", "test 2")
            }
        }
    }

    @Test
    fun `should get one restaurant`() {
        val restaurantHashMapRepository = RestaurantHashMapRepositoryImpl()
        val restaurantTest1 = Restaurant("test 1")
        restaurantHashMapRepository.save(restaurantTest1)
        val restaurantController = RestaurantController(restaurantHashMapRepository)
        val flowRestaurant = restaurantController.findOne(restaurantTest1.id)
        runBlocking {
            flowRestaurant.collect { r ->
                assertThat(r).isEqualTo(restaurantTest1)
            }
        }
    }

    @Test
    fun `should throw an exception when restaurant isn't in base`() {
        val restaurantHashMapRepository = RestaurantHashMapRepositoryImpl()
        val restaurantController = RestaurantController(restaurantHashMapRepository)
        val flowRestaurant = restaurantController.findOne("invalid-id")
        runBlocking {
            flowRestaurant.catch { e ->
                assertThat(e)
                    .isInstanceOf(RestomizerNotFoundException::class.java)
                    .hasMessage("No restaurant found with id [invalid-id]")
            }
        }
    }

    @Test
    fun `should save a restaurant`() {
        val restaurantHashMapRepository = RestaurantHashMapRepositoryImpl()
        val restaurant = Restaurant("Auberge des 3 renards")
        val restaurantController = RestaurantController(restaurantHashMapRepository)
        restaurantController.save(restaurant)
        val flowRestaurantExpected = restaurantHashMapRepository.findOne(restaurant.id)
        runBlocking {
            flowRestaurantExpected.collect { r ->
                assertThat(r).isEqualTo(restaurant)
            }
        }
    }
}
