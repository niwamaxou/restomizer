package com.restomizer.restomizerback.restaurant.repository

import com.restomizer.restomizerback.restaurant.exception.RestomizerException
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
        val restaurant = Restaurant("Restaurant du Junit")
        restaurantHashMapRepository.save(restaurant)
        val flowRestaurantExpected = restaurantHashMapRepository.findOne(restaurant.id)
        runBlocking {
            flowRestaurantExpected.collect { r ->
                assertThat(r).isEqualTo(restaurant)
            }
        }
    }

    @Test
    fun `should throw notFoundException if no restaurant is found`() {
        val restaurantHashMapRepository = RestaurantHashMapRepositoryImpl()
        val restaurant = Restaurant("Restaurant du Junit")
        restaurantHashMapRepository.save(restaurant)
        val flowShouldThrowAnException = restaurantHashMapRepository.findOne("fake-id")
        runBlocking {
            flowShouldThrowAnException
                .catch { e ->
                    assertThat(e)
                        .isInstanceOf(RestomizerException::class.java)
                        .hasMessage("No restaurant found with id [fake-id]")
                }
        }
    }

    @Test
    fun `should find all restaurant`() {
        val restaurantHashMapRepository = RestaurantHashMapRepositoryImpl()
        val restaurant = Restaurant("Restaurant du Junit")
        val auberge = Restaurant("Auberge du Test")
        val inn = Restaurant("Assertions Inn")
        restaurantHashMapRepository.save(restaurant)
        restaurantHashMapRepository.save(auberge)
        restaurantHashMapRepository.save(inn)
        val flowRestaurantsExpected = restaurantHashMapRepository.findAll()
        runBlocking {
            flowRestaurantsExpected.collect { lr ->
                assertThat(lr).extracting("name")
                    .contains("Restaurant du Junit", "Auberge du Test", "Assertions Inn")
            }
        }
    }
}
