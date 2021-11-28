package com.restomizer.restomizerback.group.controller

import com.restomizer.restomizerback.group.model.Group
import com.restomizer.restomizerback.group.service.GroupService
import com.restomizer.restomizerback.restaurant.service.RestaurantService
import kotlinx.coroutines.flow.Flow
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
    
    @PostMapping("/groups")
    suspend fun getOrCreate(@RequestBody group : Group): ResponseEntity<Group> {

        if (groupService.isGroupAlreadyCreated(group.name)) {
            return ResponseEntity.ok(group)
        }

        val savedGroup = groupService.save(group)
        return ResponseEntity.created(URI("/restomizer/v1/groups/${savedGroup.id}")).body(savedGroup)
    }

    @GetMapping("/groups/{id}")
    suspend fun findById(id: String): Group {
        return groupService.findById(id)
    }

}
