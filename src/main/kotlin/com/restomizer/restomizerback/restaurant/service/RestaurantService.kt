package com.restomizer.restomizerback.restaurant.service

import com.restomizer.restomizerback.restaurant.model.Restaurant
import com.restomizer.restomizerback.restaurant.repository.RestaurantSpringRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RestaurantService @Autowired constructor(
    val restaurantRepository: RestaurantSpringRepository,
    val randomizerService: RandomizerService
) {

    suspend fun getOneRandomRestaurant(): Restaurant {
        val allRestaurants = restaurantRepository.findAll().asFlow().toList()
        return allRestaurants[randomizerService.getRandomNumberUntil(allRestaurants.size)]
    }

    fun findAll(): Flow<Restaurant> {
        return restaurantRepository.findAll().asFlow()
    }

    suspend fun findById(id: String): Restaurant {
        return restaurantRepository.findById(id).awaitSingle()
    }

    suspend fun save(restaurant: Restaurant): Restaurant {
        return restaurantRepository.save(restaurant).awaitSingle()
    }
}
