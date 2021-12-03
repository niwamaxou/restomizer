package com.restomizer.restomizerback.group.controller

import com.restomizer.restomizerback.group.model.User
import com.restomizer.restomizerback.group.service.GroupServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/restomizer/v1")
class GroupController @Autowired constructor(
    val groupService: GroupServiceImpl
) {
    
    @PostMapping("/user")
    suspend fun subscribe(@RequestBody user : User): ResponseEntity<User> {

        val subscribedUser = groupService.subscribe(user)
        return ResponseEntity.ok(subscribedUser)
    }

}
