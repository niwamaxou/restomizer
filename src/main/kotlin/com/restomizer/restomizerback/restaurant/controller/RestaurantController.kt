package com.restomizer.restomizerback.restaurant.controller

import com.restomizer.restomizerback.restaurant.model.Restaurant
import com.restomizer.restomizerback.restaurant.repository.RestaurantHashMapRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/restomizer/v1/restaurant")
class RestaurantController @Autowired constructor(val restaurantHashMapRepository: RestaurantHashMapRepository) {

    @GetMapping("")
    fun findAll(): Flow<List<Restaurant>> = this.restaurantHashMapRepository.findAll()
}
