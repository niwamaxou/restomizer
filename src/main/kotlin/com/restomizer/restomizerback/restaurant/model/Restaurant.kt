package com.restomizer.restomizerback.restaurant.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Restaurant(@JsonProperty("id") @Id val id: String? = null, @JsonProperty("name") val name: String)
