package com.walterscarborough.trafficcop

import com.walterscarborough.trafficcop.configuration.GatlingConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class TrafficCopApplication

fun main(args: Array<String>) {
	GatlingConfiguration.configureOutputDirectory()

	runApplication<TrafficCopApplication>(*args)
}
