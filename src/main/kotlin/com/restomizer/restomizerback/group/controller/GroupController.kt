package com.restomizer.restomizerback.group.controller

import com.restomizer.restomizerback.group.model.User
import com.restomizer.restomizerback.group.service.GroupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
@RequestMapping("/restomizer/v1")
class GroupController @Autowired constructor(
    val groupService: GroupService
) {
    
    @PostMapping("/user")
    suspend fun subscribe(@RequestBody user : User): ResponseEntity<Void> {

        return ResponseEntity.ok().build();
    }

}
