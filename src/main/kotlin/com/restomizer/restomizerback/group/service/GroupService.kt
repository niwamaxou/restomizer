package com.restomizer.restomizerback.group.service

import com.restomizer.restomizerback.group.model.Group
import com.restomizer.restomizerback.group.model.User
import com.restomizer.restomizerback.group.model.Vote

class GroupService {
    
    fun isGroupAlreadyCreated(name: String): Boolean {
        
        if (name == "name")
            return true
        
        return false
    }

    fun save(group: Group): Group {
        
        return group
    }

    fun findById(id: String): Group {
        return Group(id, "name", listOf(User("name1", null, Vote(null, false))))
    }
}
