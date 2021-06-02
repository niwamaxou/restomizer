package com.restomizer.restomizerback.restaurant.repository

import com.restomizer.restomizerback.restaurant.exception.RestomizerNotFoundException
import com.restomizer.restomizerback.restaurant.model.Restaurant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.stereotype.Service


@Service
class RestaurantHashMapRepositoryImpl : RestaurantRepository {

    private val restaurantMap: HashMap<String, Restaurant> = HashMap()

    override fun findAll(): Flow<Restaurant> {
        return flow {
            for (restaurant in restaurantMap.values) {
                emit(restaurant)
            }
        }
    }

    override suspend fun findById(id: String): Restaurant {
        return restaurantMap[id] ?: throw RestomizerNotFoundException("No restaurant found with id [$id]")
    }

    override suspend fun save(restaurant: Restaurant): Restaurant {
        val toSaveRestaurant = Restaurant("${restaurant.name}-id", restaurant.name)
        restaurantMap[toSaveRestaurant.id!!] = toSaveRestaurant
        return toSaveRestaurant
    }
}
