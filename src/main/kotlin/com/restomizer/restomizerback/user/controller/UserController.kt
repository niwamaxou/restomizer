package com.restomizer.restomizerback.user.controller

import com.restomizer.restomizerback.user.model.User
import com.restomizer.restomizerback.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/restomizer/v1")
class UserController @Autowired constructor(
    val groupService: UserService
) {
    
    @PostMapping("/user")
    suspend fun subscribe(@RequestBody user : User): ResponseEntity<User> {

        val subscribedUser = groupService.subscribe(user)
        return ResponseEntity.ok(subscribedUser)
    }

}
