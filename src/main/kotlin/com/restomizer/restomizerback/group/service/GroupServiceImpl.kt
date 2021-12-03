package com.restomizer.restomizerback.group.service

import com.restomizer.restomizerback.group.model.User
import org.springframework.stereotype.Service

@Service
class GroupServiceImpl : GroupService {

    override suspend fun subscribe(user: User): User {
        return User("id", user.name)
    }
}
