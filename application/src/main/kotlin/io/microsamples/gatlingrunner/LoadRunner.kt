package io.microsamples.gatlingrunner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class LoadRunner

fun main(args: Array<String>) {
	runApplication<LoadRunner>(*args)
}
