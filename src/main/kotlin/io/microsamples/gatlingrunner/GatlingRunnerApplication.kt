package io.microsamples.gatlingrunner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GatlingrunnerApplication

fun main(args: Array<String>) {
	runApplication<GatlingrunnerApplication>(*args)
}
