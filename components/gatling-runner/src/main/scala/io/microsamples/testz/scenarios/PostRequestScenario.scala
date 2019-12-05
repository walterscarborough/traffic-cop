package io.microsamples.testz.scenarios

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import io.microsamples.gatlingrunner.load.GatlingContext

object PostRequestScenario {
  val postRequestScenarioCommand: HttpRequestBuilder = http("create post request")
    .post(GatlingContext.INSTANCE.getEndpoint())
    .body(StringBody(GatlingContext.INSTANCE.getPayload))
    .check(status is 200)

  val postRequestScenario: ScenarioBuilder = scenario("execute post request")
    .exec(postRequestScenarioCommand)
}
