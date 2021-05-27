package com.restomizer.restomizerback.restaurant.model

import java.util.*

data class Restaurant(val name: String) {

    val id: String = UUID.randomUUID().toString()

}

