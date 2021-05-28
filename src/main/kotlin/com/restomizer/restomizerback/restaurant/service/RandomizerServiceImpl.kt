package com.restomizer.restomizerback.restaurant.service

import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class RandomizerServiceImpl : RandomizerService {

    override fun getRandomNumberUntil(limit: Int): Int {

        return Random.nextInt(limit);
    }
}
