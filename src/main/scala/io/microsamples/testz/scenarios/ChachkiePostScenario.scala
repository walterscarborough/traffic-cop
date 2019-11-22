package io.microsamples.testz.scenarios

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import io.microsamples.gatlingrunner.load.GatlingContext

object ChachkiePostScenario {

  val chachkiesPostScenarioPath: HttpRequestBuilder = http("Post some cool chachkies")
    .post("/remote-chachkies")
    .body(StringBody(GatlingContext.INSTANCE.payload))
    .check(status is 200)

  val chachkiesRoot: ScenarioBuilder = scenario("Post Remote Chachkies")
    .exec(chachkiesPostScenarioPath)
}
