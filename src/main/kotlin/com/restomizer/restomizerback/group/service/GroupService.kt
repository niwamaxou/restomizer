package com.restomizer.restomizerback.group.service

import com.restomizer.restomizerback.group.model.User
import com.restomizer.restomizerback.group.repository.GroupRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GroupService @Autowired constructor(private val groupRepository: GroupRepository) {

    suspend fun subscribe(user: User): User {
        return groupRepository.save(user).awaitSingle()
    }
}
