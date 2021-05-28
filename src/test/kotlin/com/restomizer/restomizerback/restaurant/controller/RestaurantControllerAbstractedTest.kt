package com.restomizer.restomizerback.restaurant.controller

import com.restomizer.restomizerback.restaurant.exception.RestomizerNotFoundException
import com.restomizer.restomizerback.restaurant.model.Restaurant
import com.restomizer.restomizerback.restaurant.repository.RestaurantHashMapRepositoryImpl
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

internal class RestaurantControllerAbstractedTest {

    @Test
    fun `should get the restaurants`() {
        val restaurantHashMapRepository = RestaurantHashMapRepositoryImpl()
        val restaurantTest1 = Restaurant("test 1")
        val restaurantTest2 = Restaurant("test 2")
        restaurantHashMapRepository.save(restaurantTest1)
        restaurantHashMapRepository.save(restaurantTest2)
        val restaurantController = RestaurantController(RestaurantService(restaurantHashMapRepository, StubRandomizerServiceImpl()))
        val restaurants = restaurantController.findAll()
        runBlocking {
            restaurants.collect { lr ->
                assertThat(lr).extracting("name")
                    .contains("test 1", "test 2")
            }
        }
    }

    @Test
    fun `should get one restaurant`() {
        val restaurantHashMapRepository = RestaurantHashMapRepositoryImpl()
        val restaurantTest1 = Restaurant("test 1")
        restaurantHashMapRepository.save(restaurantTest1)
        val restaurantController = RestaurantController(RestaurantService(restaurantHashMapRepository, StubRandomizerServiceImpl()))
        val flowRestaurant = restaurantController.findOne(restaurantTest1.id)
        runBlocking {
            flowRestaurant.collect { r ->
                assertThat(r).isEqualTo(restaurantTest1)
            }
        }
    }

    @Test
    fun `should throw an exception when restaurant isn't in base`() {
        val restaurantHashMapRepository = RestaurantHashMapRepositoryImpl()
        val restaurantController = RestaurantController(RestaurantService(restaurantHashMapRepository, StubRandomizerServiceImpl()))
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
        val restaurantHashMapRepository = RestaurantHashMapRepositoryImpl()
        val restaurant = Restaurant("test 1")
        val restaurantController = RestaurantController(RestaurantService(restaurantHashMapRepository, StubRandomizerServiceImpl()))
        restaurantController.save(restaurant)
        val flowRestaurantExpected = restaurantHashMapRepository.findOne(restaurant.id)
        runBlocking {
            flowRestaurantExpected.collect { r ->
                assertThat(r).isEqualTo(restaurant)
            }
        }
    }

    @Test
    fun `should get a random restaurant`() {
        val restaurantHashMapRepository = RestaurantHashMapRepositoryImpl()
        val restaurant1 = Restaurant("test 1")
        val restaurant2 = Restaurant("test 2")
        val restaurant3 = Restaurant("test 3")
        val restaurant4 = Restaurant("test 4")
        val restaurant5 = Restaurant("test 5")
        restaurantHashMapRepository.save(restaurant1)
        restaurantHashMapRepository.save(restaurant2)
        restaurantHashMapRepository.save(restaurant3)
        restaurantHashMapRepository.save(restaurant4)
        restaurantHashMapRepository.save(restaurant5)
        val restaurantController = RestaurantController(RestaurantService(StubRestaurantRepositoryImpl(), StubRandomizerServiceImpl()))
        val flowRestaurantExpected = restaurantController.getOneRandomRestaurant()
        runBlocking {
            flowRestaurantExpected.collect { r ->
                assertThat(r).isEqualTo(restaurant2)
            }
        }
    }

    class StubRandomizerServiceImpl : RandomizerService {
        override fun getRandomNumberUntil(limit: Int): Int {
            return 1;
        }
    }

    class StubRestaurantRepositoryImpl : RestaurantRepository {
        override fun findAll(): Flow<List<Restaurant>> {
            val restaurants = listOf<Restaurant>(
                Restaurant("test 1"),
                Restaurant("test 2"),
                Restaurant("test 3"),
                Restaurant("test 4"),
                Restaurant("test 5")
            )
            return flow {
                emit(restaurants)
            }
        }

        override fun findOne(id: String): Flow<Restaurant> {
            TODO("Not yet implemented")
        }

        override fun save(restaurant: Restaurant) {
            TODO("Not yet implemented")
        }
    }
}
