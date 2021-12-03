package com.restomizer.restomizerback.group.service

import com.restomizer.restomizerback.group.model.User

interface GroupService {

    suspend fun subscribe(user: User) : User
}
