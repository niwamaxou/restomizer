package com.restomizer.restomizerback.restaurant.controller

import com.restomizer.restomizerback.restaurant.repository.RestaurantHashMapRepositoryImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

internal class RestaurantControllerAbstractedTest {
    
    @Test
    fun `should get the restaurants`() {
        val restaurantController = RestaurantController()
        val restaurants = restaurantController.findAll()
        runBlocking {
            restaurants.collect {
                    r -> assertThat(r).isEqualTo("Hello World") 
            }
        }
    }
}
