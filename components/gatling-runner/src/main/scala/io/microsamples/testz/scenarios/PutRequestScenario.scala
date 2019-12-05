package io.microsamples.testz.scenarios

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import io.microsamples.gatlingrunner.load.GatlingContext

object PutRequestScenario {
  val putRequestScenarioCommand: HttpRequestBuilder = http("create put request")
    .put(GatlingContext.INSTANCE.endpoint)
    .body(StringBody(GatlingContext.INSTANCE.payload))
    .check(status is 200)

  val putRequestScenario: ScenarioBuilder = scenario("execute put request")
    .exec(putRequestScenarioCommand)
}
