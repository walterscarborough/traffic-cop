package com.walterscarborough.trafficcop.gatlingrunner.scenarios

import com.walterscarborough.trafficcop.context.GatlingContext
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

object PutRequestScenario {
  val putRequestScenarioCommand: HttpRequestBuilder = http("create put request")
    .put(GatlingContext.INSTANCE.getEndpoint())
    .body(StringBody(GatlingContext.INSTANCE.getPayload))
    .check(status is 200)

  val putRequestScenario: ScenarioBuilder = scenario("execute put request")
    .exec(putRequestScenarioCommand)
}
