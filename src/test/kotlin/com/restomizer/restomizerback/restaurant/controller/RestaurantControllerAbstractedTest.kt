package com.restomizer.restomizerback.restaurant.controller

import com.restomizer.restomizerback.restaurant.exception.RestomizerNotFoundException
import com.restomizer.restomizerback.restaurant.model.Restaurant
import com.restomizer.restomizerback.restaurant.repository.RestaurantRepository
import com.restomizer.restomizerback.restaurant.service.RandomizerService
import com.restomizer.restomizerback.restaurant.service.RestaurantService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

internal class RestaurantControllerAbstractedTest {

    @Test
    fun `should get the restaurants`() {
        val restaurantController = RestaurantController(RestaurantService(StubRestaurantRepositoryImpl(), StubRandomizerServiceImpl()))
        val restaurants = restaurantController.findAll()
        var i = 1
        runBlocking {
            restaurants.collect { r ->
                assertThat(r.name).isEqualTo("test-${i++}")
            }
        }
    }

    @Test
    fun `should get one restaurant`() {
        val stubRestaurantRepositoryImpl = StubRestaurantRepositoryImpl()
        val restaurantController = RestaurantController(RestaurantService(stubRestaurantRepositoryImpl, StubRandomizerServiceImpl()))
        runBlocking {
            val restaurant = restaurantController.findById("test-1-id")
            assertThat(restaurant.name).isEqualTo("test-1")
            assertThat(restaurant.getId()).isEqualTo("test-1-id")
        }
    }

    @Test
    fun `should throw an exception when restaurant isn't in base`() {
        val restaurantController = RestaurantController(RestaurantService(StubRestaurantRepositoryImpl(), StubRandomizerServiceImpl()))
        runBlocking {
            try {
                restaurantController.findById("invalid-id")
            } catch (restomizerNotFoundException: RestomizerNotFoundException) {
                assertThat(restomizerNotFoundException.message).isEqualTo("No restaurant found with id [invalid-id]")
            }
        }
    }

    @Test
    fun `should save a restaurant`() {
        val stubRestaurantRepositoryImpl = StubRestaurantRepositoryImpl()
        val restaurant = Restaurant("test-saved")
        val restaurantController = RestaurantController(RestaurantService(stubRestaurantRepositoryImpl, StubRandomizerServiceImpl()))
        val savedRestaurant = restaurantController.save(restaurant)
        assertThat(savedRestaurant.body!!.name).isEqualTo("test-saved")
    }

    @Test
    fun `should get a random restaurant`() {
        val restaurantController = RestaurantController(RestaurantService(StubRestaurantRepositoryImpl(), StubRandomizerServiceImpl()))
        runBlocking {
            val flowRestaurantExpected = restaurantController.getOneRandomRestaurant()
            assertThat(flowRestaurantExpected.name).isEqualTo("test-2")
            assertThat(flowRestaurantExpected.getId()).isEqualTo("test-2-id")
        }
    }

    class StubRandomizerServiceImpl : RandomizerService {
        override fun getRandomNumberUntil(limit: Int): Int {
            return 1;
        }
    }

    class StubRestaurantRepositoryImpl : RestaurantRepository {

        private val restaurantMap: HashMap<String, Restaurant> = HashMap<String, Restaurant>()
        private val restaurantSortedMap: SortedMap<String, Restaurant>

        init {
            val restaurant1 = Restaurant("test-1")
            restaurant1.generateId()
            val restaurant2 = Restaurant("test-2")
            restaurant2.generateId()
            val restaurant3 = Restaurant("test-3")
            restaurant3.generateId()
            restaurantMap["test-1-id"] = restaurant1
            restaurantMap["test-2-id"] = restaurant2
            restaurantMap["test-3-id"] = restaurant3
            restaurantSortedMap = restaurantMap.toSortedMap()
        }

        override fun findAll(): Flow<Restaurant> {
            return flow {
                for (restaurant in restaurantSortedMap.values)
                    emit(restaurant)
            }
        }

        override suspend fun findById(id: String): Restaurant {
            return restaurantSortedMap[id] ?: throw RestomizerNotFoundException("No restaurant found with id [$id]")
        }

        override fun save(restaurant: Restaurant): Restaurant {
            return restaurant
        }
    }
}
