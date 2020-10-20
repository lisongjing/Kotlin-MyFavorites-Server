package com.orchidpine.myfavorites.account

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableDiscoveryClient
@EnableCaching
class MyFavoritesAccountApplication

fun main(args: Array<String>) {
	runApplication<MyFavoritesAccountApplication>(*args)
}
