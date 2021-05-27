package com.restomizer.restomizerback.restaurant.repository

import com.restomizer.restomizerback.restaurant.exception.RestomizerException
import com.restomizer.restomizerback.restaurant.exception.RestomizerNotFoundException
import com.restomizer.restomizerback.restaurant.model.Restaurant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.stereotype.Service

@Service
class RestaurantHashMapRepositoryImpl : RestaurantHashMapRepository {

    private val restaurantMap: HashMap<String, Restaurant> = HashMap<String, Restaurant>()

    override fun findAll(): Flow<List<Restaurant>> {
        return flow {
            emit(restaurantMap.values.toList())
        }
    }

    override fun findOne(id: String): Flow<Restaurant> {
        return flow {
            val restaurant = restaurantMap[id] ?: throw RestomizerNotFoundException("No restaurant found with id [$id]")
            emit(restaurant)
        }
    }

    override fun save(restaurant: Restaurant) {
        restaurantMap[restaurant.id] = restaurant
    }
}
