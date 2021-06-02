package com.restomizer.restomizerback.restaurant.controller

import com.restomizer.restomizerback.restaurant.exception.RestomizerNotFoundException
import com.restomizer.restomizerback.restaurant.model.Restaurant
import com.restomizer.restomizerback.restaurant.repository.RestaurantSpringRepository
import com.restomizer.restomizerback.restaurant.service.RandomizerService
import com.restomizer.restomizerback.restaurant.service.RestaurantService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.reactivestreams.Publisher
import org.springframework.data.domain.Example
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
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
            assertThat(restaurant.id).isEqualTo("test-1-id")
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
        val restaurant = Restaurant("test-saved-id", "test-saved")
        val restaurantController = RestaurantController(RestaurantService(stubRestaurantRepositoryImpl, StubRandomizerServiceImpl()))
        runBlocking {
            val savedRestaurant = restaurantController.save(restaurant)
            assertThat(savedRestaurant.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(savedRestaurant.headers["location"]!![0]).isEqualTo("/restomizer/v1/restaurants/test-saved-id")
            assertThat(savedRestaurant.body!!.name).isEqualTo("test-saved")
            assertThat(savedRestaurant.body!!.id).isEqualTo("test-saved-id")
        }
    }

    @Test
    fun `should get a random restaurant`() {
        val restaurantController = RestaurantController(RestaurantService(StubRestaurantRepositoryImpl(), StubRandomizerServiceImpl()))
        runBlocking {
            val flowRestaurantExpected = restaurantController.getOneRandomRestaurant()
            assertThat(flowRestaurantExpected.name).isEqualTo("test-2")
            assertThat(flowRestaurantExpected.id).isEqualTo("test-2-id")
        }
    }

    class StubRandomizerServiceImpl : RandomizerService {
        override fun getRandomNumberUntil(limit: Int): Int {
            return 1;
        }
    }

    class StubRestaurantRepositoryImpl : RestaurantSpringRepository {

        private val restaurantMap: HashMap<String, Restaurant> = HashMap<String, Restaurant>()
        private val restaurantSortedMap: SortedMap<String, Restaurant>

        init {
            val restaurant1 = Restaurant("test-1-id", "test-1")
            val restaurant2 = Restaurant("test-2-id", "test-2")
            val restaurant3 = Restaurant("test-3-id", "test-3")
            restaurantMap["test-1-id"] = restaurant1
            restaurantMap["test-2-id"] = restaurant2
            restaurantMap["test-3-id"] = restaurant3
            restaurantSortedMap = restaurantMap.toSortedMap()
        }

        override fun <S : Restaurant?> save(p0: S): Mono<S> {
            return Mono.just(p0)
        }

        override fun <S : Restaurant?> saveAll(p0: MutableIterable<S>): Flux<S> {
            TODO("Not yet implemented")
        }

        override fun <S : Restaurant?> saveAll(p0: Publisher<S>): Flux<S> {
            TODO("Not yet implemented")
        }

        override fun findById(p0: String): Mono<Restaurant> {
            return Mono.just(restaurantSortedMap[p0] ?: throw RestomizerNotFoundException("No restaurant found with id [$p0]"))
        }

        override fun findById(p0: Publisher<String>): Mono<Restaurant> {
            TODO("Not yet implemented")
        }

        override fun existsById(p0: String): Mono<Boolean> {
            TODO("Not yet implemented")
        }

        override fun existsById(p0: Publisher<String>): Mono<Boolean> {
            TODO("Not yet implemented")
        }

        override fun <S : Restaurant?> findAll(example: Example<S>): Flux<S> {
            TODO("Not yet implemented")
        }

        override fun <S : Restaurant?> findAll(example: Example<S>, sort: Sort): Flux<S> {
            TODO("Not yet implemented")
        }

        override fun findAll(p0: Sort): Flux<Restaurant> {
            TODO("Not yet implemented")
        }

        override fun findAll(): Flux<Restaurant> {
            return Flux.fromIterable(restaurantSortedMap.values)
        }

        override fun findAllById(p0: MutableIterable<String>): Flux<Restaurant> {
            TODO("Not yet implemented")
        }

        override fun findAllById(p0: Publisher<String>): Flux<Restaurant> {
            TODO("Not yet implemented")
        }

        override fun count(): Mono<Long> {
            TODO("Not yet implemented")
        }

        override fun <S : Restaurant?> count(p0: Example<S>): Mono<Long> {
            TODO("Not yet implemented")
        }

        override fun deleteById(p0: String): Mono<Void> {
            TODO("Not yet implemented")
        }

        override fun deleteById(p0: Publisher<String>): Mono<Void> {
            TODO("Not yet implemented")
        }

        override fun delete(p0: Restaurant): Mono<Void> {
            TODO("Not yet implemented")
        }

        override fun deleteAllById(p0: MutableIterable<String>): Mono<Void> {
            TODO("Not yet implemented")
        }

        override fun deleteAll(p0: MutableIterable<Restaurant>): Mono<Void> {
            TODO("Not yet implemented")
        }

        override fun deleteAll(p0: Publisher<out Restaurant>): Mono<Void> {
            TODO("Not yet implemented")
        }

        override fun deleteAll(): Mono<Void> {
            TODO("Not yet implemented")
        }

        override fun <S : Restaurant?> findOne(p0: Example<S>): Mono<S> {
            TODO("Not yet implemented")
        }

        override fun <S : Restaurant?> exists(p0: Example<S>): Mono<Boolean> {
            TODO("Not yet implemented")
        }

        override fun <S : Restaurant?> insert(entity: S): Mono<S> {
            TODO("Not yet implemented")
        }

        override fun <S : Restaurant?> insert(entities: MutableIterable<S>): Flux<S> {
            TODO("Not yet implemented")
        }

        override fun <S : Restaurant?> insert(entities: Publisher<S>): Flux<S> {
            TODO("Not yet implemented")
        }
    }
}
