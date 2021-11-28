package com.restomizer.restomizerback.group.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.restomizer.restomizerback.restaurant.model.Restaurant
import org.springframework.data.annotation.Id

data class Vote(
    @JsonProperty("restaurant") val restaurant : Restaurant?,
    @JsonProperty("cast") val cast: Boolean)
