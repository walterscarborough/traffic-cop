package io.microsamples.gatlingrunner.load

data class RunLoadPostRequest(
    val baseUrl: String,
    val endpoint: String,
    val payload: String,
    val constantUsersPerSecond: Int,
    val constantUsersPerSecondDuration: Int
)
