package com.restomizer.restomizerback

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import java.util.*


@SpringBootApplication
class RestomizerBackApplication

fun main(args: Array<String>) {
    runApplication<RestomizerBackApplication>(*args)
}
