package io.microsamples.gatlingrunner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class GatlingrunnerApplication

fun main(args: Array<String>) {
	runApplication<GatlingrunnerApplication>(*args)
}
