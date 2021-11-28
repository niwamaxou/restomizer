package com.restomizer.restomizerback.group.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class Group(
    @JsonProperty("id") @Id val id: String? = null,
    @JsonProperty("name") val name: String,
    @JsonProperty("users") val users: List<User>)
