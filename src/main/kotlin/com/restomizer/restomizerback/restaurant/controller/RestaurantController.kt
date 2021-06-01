package com.restomizer.restomizerback.restaurant.controller

import com.restomizer.restomizerback.restaurant.model.Restaurant
import com.restomizer.restomizerback.restaurant.service.RestaurantService
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/restomizer/v1")
class RestaurantController @Autowired constructor(
    val restaurantService: RestaurantService
) {

    @GetMapping("/restaurants")
    fun findAll(): Flow<List<Restaurant>> = restaurantService.findAll()

    @GetMapping("/restaurants/{id}")
    fun findOne(@PathVariable id: String): Flow<Restaurant> = restaurantService.findOne(id)

    @PostMapping("/restaurants")
    fun save(@RequestBody restaurant: Restaurant): ResponseEntity<Restaurant> {
        return ResponseEntity.ok(restaurantService.save(restaurant))
    }

    @GetMapping("/random/restaurants")
    fun getOneRandomRestaurant(): Flow<Restaurant> = restaurantService.getOneRandomRestaurant()
}
