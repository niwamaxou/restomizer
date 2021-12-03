package com.restomizer.restomizerback.user.service

import com.restomizer.restomizerback.user.model.User
import com.restomizer.restomizerback.user.repository.UserRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(private val userRepository: UserRepository) {

    suspend fun subscribe(user: User): User {
        return userRepository.save(user).awaitSingle()
    }
}
