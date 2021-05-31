package com.restomizer.restomizerback.restaurant.repository

import com.restomizer.restomizerback.restaurant.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {

    fun findAll(): Flow<List<Restaurant>>
    fun findOne(id: String): Flow<Restaurant>
    fun save(restaurant: Restaurant): Restaurant
}
