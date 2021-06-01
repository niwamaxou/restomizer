package com.restomizer.restomizerback.restaurant.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class Restaurant(@JsonProperty("name") val name: String) {

    @JsonProperty
    private var id: String = "default-id"

    @JsonIgnore
    fun generateId() {
        id = "${name}-id";
    }

    @JsonIgnore
    fun getId(): String {
        return id;
    }
}
