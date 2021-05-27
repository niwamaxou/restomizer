package com.restomizer.restomizerback.restaurant.controller

import com.restomizer.restomizerback.restaurant.model.Restaurant
import com.restomizer.restomizerback.restaurant.repository.RestaurantHashMapRepository
import com.restomizer.restomizerback.restaurant.repository.RestaurantHashMapRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/restomizer/v1/restaurants")
class RestaurantController @Autowired constructor(val restaurantHashMapRepository: RestaurantHashMapRepositoryImpl) {

    @GetMapping("")
    fun findAll(): Flow<List<Restaurant>> = this.restaurantHashMapRepository.findAll()

    @PostMapping("")
    fun save(@RequestBody restaurant : Restaurant) = this.restaurantHashMapRepository.save(restaurant)
    
}
