package com.walterscarborough.trafficcop.gatlingrunner.scenarios

import com.walterscarborough.trafficcop.context.GatlingContext
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

object DeleteRequestScenario {
  val deleteRequestScenarioCommand: HttpRequestBuilder = http("create delete request")
    .delete(GatlingContext.INSTANCE.getEndpoint())
    .check(status is 200)

  val deleteRequestScenario: ScenarioBuilder = scenario("execute delete request")
    .exec(deleteRequestScenarioCommand)
}
