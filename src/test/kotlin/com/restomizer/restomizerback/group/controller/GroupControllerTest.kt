package com.restomizer.restomizerback.group.controller

import com.restomizer.restomizerback.group.model.User
import com.restomizer.restomizerback.group.service.GroupService
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class GroupControllerTest {
    
    @Test
    fun `should add a user to the default group`() {
        val groupController = GroupController(GroupService())
        runBlocking {
            val responseEntity = groupController.subscribe(User("user"))
            assertThat(responseEntity.statusCodeValue).isEqualTo(200)
        }
    }
}
