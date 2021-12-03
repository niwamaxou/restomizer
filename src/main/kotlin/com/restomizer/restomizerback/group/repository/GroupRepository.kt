package com.restomizer.restomizerback.group.repository

import com.restomizer.restomizerback.group.model.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface GroupRepository : ReactiveMongoRepository<User, String>