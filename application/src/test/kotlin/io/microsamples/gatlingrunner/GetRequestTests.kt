package io.microsamples.gatlingrunner

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.ok
import io.microsamples.gatlingrunner.factories.RunLoadRequestFactory
import io.microsamples.gatlingrunner.load.HttpMethod

class GetRequestTests: AbstractRequestTests() {

    override fun setupWireMockStubs() {
        wireMockServer.stubFor(
            WireMock.get(WireMock.urlEqualTo("/chachkies"))
                .willReturn(
                    ok()
                )
        )
    }

    override fun verifyWireMockRequests(expectedTotalMessages: Int) {
        wireMockServer.verify(
            expectedTotalMessages,
            WireMock.getRequestedFor(WireMock.urlEqualTo("/chachkies"))
        )
    }

    override fun getRequestJson(
        constantUsersPerSecond: Int,
        constantUsersPerSecondDuration: Int,
        rampUsersPerSecondMinimum: Int,
        rampUsersPerSecondMaximum: Int,
        rampUsersPerSecondDuration: Int
    ): String {
        return RunLoadRequestFactory.createJson(
            constantUsersPerSecond = constantUsersPerSecond,
            constantUsersPerSecondDuration = constantUsersPerSecondDuration,
            rampUsersPerSecondMinimum = rampUsersPerSecondMinimum,
            rampUsersPerSecondMaximum = rampUsersPerSecondMaximum,
            rampUsersPerSecondDuration = rampUsersPerSecondDuration,
            wireMockServerPort = wireMockServer.port(),
            httpMethod = HttpMethod.GET
        )
    }
}
