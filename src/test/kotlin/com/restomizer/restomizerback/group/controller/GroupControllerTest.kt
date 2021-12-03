package com.restomizer.restomizerback.group.controller

import com.restomizer.restomizerback.group.model.User
import com.restomizer.restomizerback.group.service.GroupServiceImpl
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class GroupControllerTest {
    
    @Test
    fun `should add a user to the default group`() {
        val groupController = GroupController(GroupServiceImpl())
        runBlocking {
            val responseEntity = groupController.subscribe(User(null, "username"))
            assertThat(responseEntity.statusCodeValue).isEqualTo(200)
            assertThat(responseEntity.body).isEqualTo(User("id", "username"))
        }
    }
}
