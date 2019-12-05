package io.microsamples.testz.scenarios

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import io.microsamples.gatlingrunner.load.GatlingContext

object GetRequestScenario {
  val getRequestScenarioCommand: HttpRequestBuilder = http("create get request")
    .get(GatlingContext.INSTANCE.endpoint)
    .check(status is 200)

  val getRequestScenario: ScenarioBuilder = scenario("execute get request")
    .exec(getRequestScenarioCommand)
}
