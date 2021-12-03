package com.restomizer.restomizerback.user.repository

import com.restomizer.restomizerback.user.model.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface UserRepository : ReactiveMongoRepository<User, String>