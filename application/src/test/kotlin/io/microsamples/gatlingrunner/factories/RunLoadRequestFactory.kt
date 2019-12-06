package io.microsamples.gatlingrunner.factories

import com.fasterxml.jackson.databind.ObjectMapper
import io.microsamples.gatlingrunner.load.HttpMethod
import io.microsamples.gatlingrunner.load.RunLoadRequest

object RunLoadRequestFactory {
    fun createJson(
        constantUsersPerSecond: Int = 0,
        constantUsersPerSecondDuration: Int = 0,
        rampUsersPerSecondMinimum: Int = 0,
        rampUsersPerSecondMaximum: Int = 0,
        rampUsersPerSecondDuration: Int = 0,
        wireMockServerPort: Int,
        httpMethod: HttpMethod
    ): String {
        val runLoadRequest = RunLoadRequest(
            constantUsersPerSecond = constantUsersPerSecond,
            constantUsersPerSecondDuration = constantUsersPerSecondDuration,
            rampUsersPerSecondMinimum = rampUsersPerSecondMinimum,
            rampUsersPerSecondMaximum = rampUsersPerSecondMaximum,
            rampUsersPerSecondDuration = rampUsersPerSecondDuration,
            payload = "{\"name\": \"my fancy chachkie\"}",
            baseUrl = "http://localhost:${wireMockServerPort}",
            endpoint = "/chachkies",
            httpMethod = httpMethod
        )

        val objectMapper = ObjectMapper()
        return objectMapper.writeValueAsString(runLoadRequest)
    }
}
