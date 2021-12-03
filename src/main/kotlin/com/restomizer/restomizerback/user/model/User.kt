package com.restomizer.restomizerback.user.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(
    @JsonProperty("id") @Id val id: String? = null,
    @Indexed @JsonProperty("name") val name: String
)
