package com.restomizer.restomizerback.restaurant.repository

import com.restomizer.restomizerback.restaurant.model.Restaurant
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface RestaurantSpringRepository : ReactiveMongoRepository<Restaurant, String>
