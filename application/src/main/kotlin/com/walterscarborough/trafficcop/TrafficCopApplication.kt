package com.walterscarborough.trafficcop

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class TrafficCopApplication

fun main(args: Array<String>) {
	runApplication<TrafficCopApplication>(*args)
}
