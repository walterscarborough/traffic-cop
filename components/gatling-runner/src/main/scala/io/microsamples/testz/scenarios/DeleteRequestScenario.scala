package io.microsamples.testz.scenarios

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import io.microsamples.gatlingrunner.load.GatlingContext

object DeleteRequestScenario {
  val deleteRequestScenarioCommand: HttpRequestBuilder = http("create delete request")
    .delete(GatlingContext.INSTANCE.endpoint)
    .check(status is 200)

  val deleteRequestScenario: ScenarioBuilder = scenario("execute delete request")
    .exec(deleteRequestScenarioCommand)
}
