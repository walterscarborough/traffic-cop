package io.microsamples.gatlingrunner

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.ok
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@AutoConfigureMockMvc
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

    override fun buildRequestJsonFoo(
        constantUsersPerSecond: Int,
        constantUsersPerSecondDuration: Int,
        rampUsersPerSecondMinimum: Int,
        rampUsersPerSecondMaximum: Int,
        rampUsersPerSecondDuration: Int
    ): String {
        // language=json
        return """
            {
              "rampUsersPerSecondMinimum": ${rampUsersPerSecondMinimum}, 
              "rampUsersPerSecondMaximum": ${rampUsersPerSecondMaximum}, 
              "rampUsersPerSecondDuration": ${rampUsersPerSecondDuration}, 
              "constantUsersPerSecond": ${constantUsersPerSecond}, 
              "constantUsersPerSecondDuration": ${constantUsersPerSecondDuration}, 
              "baseUrl": "http://localhost:9090", 
              "endpoint": "/chachkies",
              "httpMethod": "GET"
            }
        """.trimIndent()
    }
}