package com.restomizer.restomizerback.restaurant.controller

import com.restomizer.restomizerback.restaurant.exception.RestomizerNotFoundException
import com.restomizer.restomizerback.restaurant.model.Restaurant
import com.restomizer.restomizerback.restaurant.repository.RestaurantRepository
import com.restomizer.restomizerback.restaurant.service.RandomizerService
import com.restomizer.restomizerback.restaurant.service.RestaurantService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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
        runBlocking {
            restaurants.collect { lr ->
                assertThat(lr).extracting("name")
                    .contains("test-1", "test-2", "test-3")
            }
        }
    }

    @Test
    fun `should get one restaurant`() {
        val stubRestaurantRepositoryImpl = StubRestaurantRepositoryImpl()
        val restaurantController = RestaurantController(RestaurantService(stubRestaurantRepositoryImpl, StubRandomizerServiceImpl()))
        val flowRestaurant = restaurantController.findOne("test-1-id")
        runBlocking {
            flowRestaurant.collect { r ->
                assertThat(r.name).isEqualTo("test-1")
                assertThat(r.getId()).isEqualTo("test-1-id")
            }
        }
    }

    @Test
    fun `should throw an exception when restaurant isn't in base`() {
        val restaurantController = RestaurantController(RestaurantService(StubRestaurantRepositoryImpl(), StubRandomizerServiceImpl()))
        val flowRestaurant = restaurantController.findOne("invalid-id")
        runBlocking {
            flowRestaurant.catch { e ->
                assertThat(e)
                    .isInstanceOf(RestomizerNotFoundException::class.java)
                    .hasMessage("No restaurant found with id [invalid-id]")
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
        val flowRestaurantExpected = restaurantController.getOneRandomRestaurant()
        runBlocking {
            flowRestaurantExpected.collect { r ->
                assertThat(r.name).isEqualTo("test-2")
                assertThat(r.getId()).isEqualTo("test-2-id")
            }
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

        override fun findAll(): Flow<List<Restaurant>> {
            return flow {
                emit(restaurantSortedMap.values.toList())
            }
        }

        override fun findOne(id: String): Flow<Restaurant> {
            return flow {
                val restaurant = restaurantSortedMap[id] ?: throw RestomizerNotFoundException("No restaurant found with id [$id]")
                emit(restaurant)
            }
        }

        override fun save(restaurant: Restaurant): Restaurant {
            return restaurant
        }
    }
}
