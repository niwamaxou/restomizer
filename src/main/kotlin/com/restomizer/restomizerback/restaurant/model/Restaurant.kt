package com.restomizer.restomizerback.restaurant.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class Restaurant(@JsonProperty("name") val name: String) {

    @JsonProperty("id")
    val id: String = UUID.randomUUID().toString()

}

