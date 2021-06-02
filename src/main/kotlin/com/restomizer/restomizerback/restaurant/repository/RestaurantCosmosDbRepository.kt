package com.restomizer.restomizerback.restaurant.repository

import com.restomizer.restomizerback.restaurant.model.Restaurant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.findAll
import org.springframework.stereotype.Service

@Service
class RestaurantCosmosDbRepository @Autowired constructor(val operations: ReactiveMongoOperations) : RestaurantRepository {

    override fun findAll(): Flow<Restaurant> {
        return operations.findAll<Restaurant>().asFlow()
    }

    override suspend fun findById(id: String): Restaurant {
        return operations.findById(id, Restaurant::class.java).awaitSingle()
    }

    override suspend fun save(restaurant: Restaurant): Restaurant {
        return operations.insert(restaurant).awaitSingle()
    }
}
