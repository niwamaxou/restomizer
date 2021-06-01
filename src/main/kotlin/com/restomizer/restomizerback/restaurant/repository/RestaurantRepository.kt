package com.restomizer.restomizerback.restaurant.repository

import com.restomizer.restomizerback.restaurant.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {

    fun findAll(): Flow<Restaurant>
    suspend fun findById(id: String): Restaurant
    suspend fun save(restaurant: Restaurant): Restaurant
}
