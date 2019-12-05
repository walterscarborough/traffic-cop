package io.microsamples.gatlingrunner.load

data class RunLoadRequest(
    val baseUrl: String,
    val endpoint: String,
    val httpMethod: HttpMethod,

    val payload: String?,

    val constantUsersPerSecond: Int,
    val constantUsersPerSecondDuration: Int,

    val rampUsersPerSecondMinimum: Int,
    val rampUsersPerSecondMaximum: Int,
    val rampUsersPerSecondDuration: Int
)
