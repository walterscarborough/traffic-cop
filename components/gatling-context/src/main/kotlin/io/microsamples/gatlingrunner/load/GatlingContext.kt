package io.microsamples.gatlingrunner.load

enum class GatlingContext {
    INSTANCE;

    var baseUrl: String? = null
    var endpoint: String? = null
    var httpMethod: HttpMethod? = null
    var payload: String? = null

    var constantUsersPerSecond = 0
    var constantUsersPerSecondDuration = 0
    var rampUsersPerSecondMinimum = 0
    var rampUsersPerSecondMaximum = 0
    var rampUsersPerSecondDuration = 0
}
