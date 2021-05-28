package com.restomizer.restomizerback.restaurant.service

interface RandomizerService {

    fun getRandomNumberUntil(limit: Int): Int
}
