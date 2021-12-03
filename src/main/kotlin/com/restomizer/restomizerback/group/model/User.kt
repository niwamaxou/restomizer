package com.restomizer.restomizerback.group.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class User(@JsonProperty("name") val name: String)
