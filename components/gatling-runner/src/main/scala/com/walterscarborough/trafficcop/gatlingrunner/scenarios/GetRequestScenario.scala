package com.walterscarborough.trafficcop.gatlingrunner.scenarios

import com.walterscarborough.trafficcop.context.GatlingContext
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

object GetRequestScenario {
  val getRequestScenarioCommand: HttpRequestBuilder = http("create get request")
    .get(GatlingContext.INSTANCE.getEndpoint())
    .check(status is 200)

  val getRequestScenario: ScenarioBuilder = scenario("execute get request")
    .exec(getRequestScenarioCommand)
}
