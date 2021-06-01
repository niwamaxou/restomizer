package com.restomizer.restomizerback.restaurant.service

import com.restomizer.restomizerback.restaurant.model.Restaurant
import com.restomizer.restomizerback.restaurant.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RestaurantService @Autowired constructor(
    val restaurantRepository: RestaurantRepository,
    val randomizerService: RandomizerService
) {

    fun getOneRandomRestaurant(): Flow<Restaurant> {
        val allRestaurants = restaurantRepository.findAll()
        return allRestaurants.transform { ar ->
            emit(ar[randomizerService.getRandomNumberUntil(ar.size)])
        }
    }

    fun findAll(): Flow<List<Restaurant>> {
        return restaurantRepository.findAll()
    }

    fun findOne(id: String): Flow<Restaurant> {
        return restaurantRepository.findOne(id)
    }

    fun save(restaurant: Restaurant): Restaurant {
        return restaurantRepository.save(restaurant)
    }
}
