package com.restomizer.restomizerback.restaurant.repository

import com.restomizer.restomizerback.restaurant.model.Restaurant
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

@Profile("!test")
interface RestaurantSpringRepository : ReactiveMongoRepository<Restaurant, String>
