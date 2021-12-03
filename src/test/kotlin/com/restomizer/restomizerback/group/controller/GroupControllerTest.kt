package com.restomizer.restomizerback.group.controller

import com.restomizer.restomizerback.group.model.User
import com.restomizer.restomizerback.group.repository.GroupRepository
import com.restomizer.restomizerback.group.service.GroupService
import com.restomizer.restomizerback.restaurant.repository.RestaurantSpringRepository
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.reactivestreams.Publisher
import org.springframework.data.domain.Example
import org.springframework.data.domain.Sort
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

internal class GroupControllerTest {
    
    @Test
    fun `should add a user to the default group`() {
        val groupController = GroupController(GroupService(StubGroupRepositoryImpl()))
        runBlocking {
            val responseEntity = groupController.subscribe(User(null, "username"))
            assertThat(responseEntity.statusCodeValue).isEqualTo(200)
            assertThat(responseEntity.body).isEqualTo(User("id", "username"))
        }
    }

    class StubGroupRepositoryImpl : GroupRepository {
        override fun <S : User?> save(entity: S): Mono<S> {

            val user = User("id", entity!!.name) as S
            return Mono.just(user!!)
        }

        override fun <S : User?> saveAll(entities: MutableIterable<S>): Flux<S> {
            TODO("Not yet implemented")
        }

        override fun <S : User?> saveAll(entityStream: Publisher<S>): Flux<S> {
            TODO("Not yet implemented")
        }

        override fun findById(id: String): Mono<User> {
            TODO("Not yet implemented")
        }

        override fun findById(id: Publisher<String>): Mono<User> {
            TODO("Not yet implemented")
        }

        override fun existsById(id: String): Mono<Boolean> {
            TODO("Not yet implemented")
        }

        override fun existsById(id: Publisher<String>): Mono<Boolean> {
            TODO("Not yet implemented")
        }

        override fun <S : User?> findAll(example: Example<S>): Flux<S> {
            TODO("Not yet implemented")
        }

        override fun <S : User?> findAll(example: Example<S>, sort: Sort): Flux<S> {
            TODO("Not yet implemented")
        }

        override fun findAll(sort: Sort): Flux<User> {
            TODO("Not yet implemented")
        }

        override fun findAll(): Flux<User> {
            TODO("Not yet implemented")
        }

        override fun findAllById(ids: MutableIterable<String>): Flux<User> {
            TODO("Not yet implemented")
        }

        override fun findAllById(idStream: Publisher<String>): Flux<User> {
            TODO("Not yet implemented")
        }

        override fun count(): Mono<Long> {
            TODO("Not yet implemented")
        }

        override fun <S : User?> count(example: Example<S>): Mono<Long> {
            TODO("Not yet implemented")
        }

        override fun deleteById(id: String): Mono<Void> {
            TODO("Not yet implemented")
        }

        override fun deleteById(id: Publisher<String>): Mono<Void> {
            TODO("Not yet implemented")
        }

        override fun delete(entity: User): Mono<Void> {
            TODO("Not yet implemented")
        }

        override fun deleteAllById(ids: MutableIterable<String>): Mono<Void> {
            TODO("Not yet implemented")
        }

        override fun deleteAll(entities: MutableIterable<User>): Mono<Void> {
            TODO("Not yet implemented")
        }

        override fun deleteAll(entityStream: Publisher<out User>): Mono<Void> {
            TODO("Not yet implemented")
        }

        override fun deleteAll(): Mono<Void> {
            TODO("Not yet implemented")
        }

        override fun <S : User?> findOne(example: Example<S>): Mono<S> {
            TODO("Not yet implemented")
        }

        override fun <S : User?> exists(example: Example<S>): Mono<Boolean> {
            TODO("Not yet implemented")
        }

        override fun <S : User?> insert(entity: S): Mono<S> {
            TODO("Not yet implemented")
        }

        override fun <S : User?> insert(entities: MutableIterable<S>): Flux<S> {
            TODO("Not yet implemented")
        }

        override fun <S : User?> insert(entities: Publisher<S>): Flux<S> {
            TODO("Not yet implemented")
        }
    }
}
