package com.restomizer.restomizerback.restaurant.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class Restaurant(@JsonProperty("name") val name: String) {
    
    @JsonProperty
    private var id: String = "default-id"

    fun generateId() {
        id = "${name}-id";
    }
    
    fun getId(): String {
        return id;
    }
}
