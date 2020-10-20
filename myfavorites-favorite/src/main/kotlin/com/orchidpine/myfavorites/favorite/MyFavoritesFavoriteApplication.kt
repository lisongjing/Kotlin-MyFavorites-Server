package com.orchidpine.myfavorites.favorite

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableCaching
class MyFavoritesFavoriteApplication

fun main(args: Array<String>) {
    runApplication<MyFavoritesFavoriteApplication>(*args)
}
