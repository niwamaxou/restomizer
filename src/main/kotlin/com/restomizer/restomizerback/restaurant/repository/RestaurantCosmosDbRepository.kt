package com.restomizer.restomizerback.restaurant.repository

import com.restomizer.restomizerback.restaurant.model.Restaurant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.stereotype.Service

@Service
@Profile("!test")
class RestaurantCosmosDbRepository @Autowired constructor(val operations: ReactiveMongoOperations): RestaurantRepository {
    override fun findAll(): Flow<Restaurant> {
        TODO("Not yet implemented")
    }

    override suspend fun findById(id: String): Restaurant {
        TODO("Not yet implemented")
    }

    override suspend fun save(restaurant: Restaurant): Restaurant {
        return operations.insert(restaurant).awaitSingle()
    }
}
