package com.restomizer.restomizerback.restaurant.service

import com.restomizer.restomizerback.restaurant.model.Restaurant
import com.restomizer.restomizerback.restaurant.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.transform
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RestaurantService @Autowired constructor(
    val restaurantRepository: RestaurantRepository,
    val randomizerService: RandomizerService
) {

    suspend fun getOneRandomRestaurant(): Restaurant {
        val allRestaurants = restaurantRepository.findAll().toList()
        return allRestaurants[randomizerService.getRandomNumberUntil(allRestaurants.size)]
    }

    fun findAll(): Flow<Restaurant> {
        return restaurantRepository.findAll()
    }

    suspend fun findById(id: String): Restaurant {
        return restaurantRepository.findById(id)
    }

    suspend fun save(restaurant: Restaurant): Restaurant {
        return restaurantRepository.save(restaurant)
    }
}
