package io.microsamples.gatlingrunner.load

data class RunLoadRequest(
    val baseUrl: String,
    val endpoint: String,
    val payload: String,
    val httpMethod: HttpMethod,

    val constantUsersPerSecond: Int,
    val constantUsersPerSecondDuration: Int,

    val rampUsersPerSecondMinimum: Int,
    val rampUsersPerSecondMaximum: Int,
    val rampUsersPerSecondDuration: Int
)
