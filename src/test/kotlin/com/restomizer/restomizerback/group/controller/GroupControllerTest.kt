package com.restomizer.restomizerback.group.controller

import com.restomizer.restomizerback.group.model.Group
import com.restomizer.restomizerback.group.model.User
import com.restomizer.restomizerback.group.model.Vote
import com.restomizer.restomizerback.group.service.GroupService
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

internal class GroupControllerTest {
    
    @Test
    fun `should get a group`() {
        val groupController = GroupController(GroupService())
        runBlocking {
            val group = groupController.findById("test-1-id")
            assertThat(group.name).isEqualTo("name")
            assertThat(group.id).isEqualTo("test-1-id")
        }
    }
    
    
    @Test
    fun `should create a group`() {
        val groupController = GroupController(GroupService())
        runBlocking {
            val savedRestaurant = groupController.getOrCreate(Group("id1", "name1", listOf(User("user2", null, Vote(null, false)))))
            assertThat(savedRestaurant.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(savedRestaurant.headers["location"]!![0]).isEqualTo("/restomizer/v1/groups/id1")
            assertThat(savedRestaurant.body!!.name).isEqualTo("name1")
            assertThat(savedRestaurant.body!!.id).isEqualTo("id1")
            assertThat(savedRestaurant.body!!.users[0].name).isEqualTo("user2")
            assertThat(savedRestaurant.body!!.users[0].token).isEqualTo("token2")
        }
    }
        
    @Test
    fun `should get a group already created`() {
        val groupController = GroupController(GroupService())
        runBlocking {
            val savedRestaurant = groupController.getOrCreate(Group("id", "name", listOf(User("user1", null, Vote(null, false)))))
            assertThat(savedRestaurant.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(savedRestaurant.headers["location"]).isNull()
            assertThat(savedRestaurant.body!!.name).isEqualTo("name")
            assertThat(savedRestaurant.body!!.id).isEqualTo("id")
            assertThat(savedRestaurant.body!!.users[0].name).isEqualTo("user1")
            assertThat(savedRestaurant.body!!.users[0].token).isEqualTo("token1")
        }
    }
}
