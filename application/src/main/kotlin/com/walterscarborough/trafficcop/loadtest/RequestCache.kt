package com.walterscarborough.trafficcop.loadtest

import com.walterscarborough.trafficcop.context.GatlingContext

fun cacheRunLoadRequest(runLoadRequest: RunLoadRequest) {
    GatlingContext.INSTANCE.httpMethod = runLoadRequest.httpMethod
    GatlingContext.INSTANCE.baseUrl = runLoadRequest.baseUrl
    GatlingContext.INSTANCE.endpoint = runLoadRequest.endpoint
    GatlingContext.INSTANCE.payload = runLoadRequest.payload

    GatlingContext.INSTANCE.constantUsersPerSecond = runLoadRequest.constantUsersPerSecond
    GatlingContext.INSTANCE.constantUsersPerSecondDuration = runLoadRequest.constantUsersPerSecondDuration

    GatlingContext.INSTANCE.rampUsersPerSecondMinimum = runLoadRequest.rampUsersPerSecondMinimum
    GatlingContext.INSTANCE.rampUsersPerSecondMaximum = runLoadRequest.rampUsersPerSecondMaximum
    GatlingContext.INSTANCE.rampUsersPerSecondDuration = runLoadRequest.rampUsersPerSecondDuration
}
