package com.walterscarborough.trafficcop

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.walterscarborough.trafficcop.context.HttpMethod
import com.walterscarborough.trafficcop.factories.RunLoadRequestFactory

class DeleteRequestTests: AbstractRequestTests() {

    override fun setupWireMockStubs() {
        wireMockServer.stubFor(
            WireMock.delete(WireMock.urlEqualTo("/chachkies"))
                .willReturn(
                    ok()
                )
        )
    }

    override fun verifyWireMockRequests(expectedTotalMessages: Int) {
        wireMockServer.verify(
            expectedTotalMessages,
            WireMock.deleteRequestedFor(WireMock.urlEqualTo("/chachkies"))
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
            httpMethod = HttpMethod.DELETE
        )
    }
}
