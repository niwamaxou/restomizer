package com.restomizer.restomizerback.restaurant.utils

import com.restomizer.restomizerback.restaurant.exception.RestomizerNotFoundException
import com.restomizer.restomizerback.restaurant.model.Restaurant
import com.restomizer.restomizerback.restaurant.repository.RestaurantSpringRepository
import org.reactivestreams.Publisher
import org.springframework.context.annotation.Profile
import org.springframework.data.domain.Example
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
@Profile("test")
class MockRestaurantSpringRepositoryImpl : RestaurantSpringRepository {

    private val restaurantMap: HashMap<String, Restaurant> = HashMap()
    private var doThrowException: Boolean = false

    fun doThrowAnExceptionWhenSave() {
        doThrowException = true
    }

    override fun <S : Restaurant?> save(p0: S): Mono<S> {
        if (doThrowException) {
            doThrowException = false
            throw Exception()
        }
        val toSaveRestaurant = Restaurant("${p0!!.name}-id", p0.name)
        restaurantMap[toSaveRestaurant.id!!] = toSaveRestaurant
        return Mono.just(toSaveRestaurant as S)
    }

    override fun <S : Restaurant?> saveAll(p0: MutableIterable<S>): Flux<S> {
        TODO("Not yet implemented")
    }

    override fun <S : Restaurant?> saveAll(p0: Publisher<S>): Flux<S> {
        TODO("Not yet implemented")
    }

    override fun findById(p0: String): Mono<Restaurant> {
        return Mono.just(restaurantMap[p0] ?: throw RestomizerNotFoundException("No restaurant found with id [$p0]"))
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
        return Flux.fromIterable(restaurantMap.values)
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
