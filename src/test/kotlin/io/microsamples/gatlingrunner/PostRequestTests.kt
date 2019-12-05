package io.microsamples.gatlingrunner

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class PostRequestTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    lateinit var wireMockServer: WireMockServer

    @BeforeEach
    internal fun setUp() {
        wireMockServer = WireMockServer(options().port(9090))
        wireMockServer.start()

        wireMockServer.stubFor(
            WireMock.post(WireMock.urlEqualTo("/chachkies"))
                .willReturn(
                    ok()
                )
        )
    }

    @AfterEach
    internal fun tearDown() {
        wireMockServer.stop()
    }

    @Test
    fun `POST requests works for constant users per second`() {
        val constantUsersPerSecond = 5
        val constantUsersPerSecondDuration = 5

        mockMvc.perform(
            post("/run-load-test")
                .content(
                    buildRequestJson(
                        constantUsersPerSecond = constantUsersPerSecond,
                        constantUsersPerSecondDuration = constantUsersPerSecondDuration
                    )
                )
                .header("Content-Type", MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)

        Thread.sleep(10000)

        val expectedTotalMessages = calculateExpectedTotalConstantUsersPerSecond(constantUsersPerSecond, constantUsersPerSecondDuration)
        wireMockServer.verify(
            expectedTotalMessages,
            WireMock.postRequestedFor(WireMock.urlEqualTo("/chachkies"))
        )
    }

    @Test
    fun `POST requests works for ramp users per second`() {
        val rampUsersPerSecondMinimum = 1
        val rampUsersPerSecondMaximum = 5
        val rampUsersPerSecondDuration = 5

        mockMvc.perform(
            post("/run-load-test")
                .content(
                    buildRequestJson(
                        rampUsersPerSecondMinimum = rampUsersPerSecondMinimum,
                        rampUsersPerSecondMaximum = rampUsersPerSecondMaximum,
                        rampUsersPerSecondDuration = rampUsersPerSecondDuration
                    )
                )
                .header("Content-Type", MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)

        Thread.sleep(10000)

        val expectedTotalMessages = calculateExpectedTotalRampUserPerSecond(
            rampUsersPerSecondMinimum,
            rampUsersPerSecondMaximum
        )
        wireMockServer.verify(
            expectedTotalMessages,
            WireMock.postRequestedFor(WireMock.urlEqualTo("/chachkies"))
        )
    }

    @Test
    fun `POST requests works for constant and ramp users per second`() {
        val constantUsersPerSecond = 1
        val constantUsersPerSecondDuration = 5

        val rampUsersPerSecondMinimum = 1
        val rampUsersPerSecondMaximum = 5
        val rampUsersPerSecondDuration = 5

        mockMvc.perform(
            post("/run-load-test")
                .content(
                    buildRequestJson(
                        constantUsersPerSecond = constantUsersPerSecond,
                        constantUsersPerSecondDuration = constantUsersPerSecondDuration,
                        rampUsersPerSecondMinimum = rampUsersPerSecondMinimum,
                        rampUsersPerSecondMaximum = rampUsersPerSecondMaximum,
                        rampUsersPerSecondDuration = rampUsersPerSecondDuration
                    )
                )
                .header("Content-Type", MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)

        Thread.sleep(20000)

        val expectedTotalRampUserPerSecond = calculateExpectedTotalRampUserPerSecond(
            rampUsersPerSecondMinimum,
            rampUsersPerSecondMaximum
        )

        val expectedTotalConstantUsersPerSecond = calculateExpectedTotalConstantUsersPerSecond(constantUsersPerSecond, constantUsersPerSecondDuration)

        val expectedTotalMessages = expectedTotalRampUserPerSecond + expectedTotalConstantUsersPerSecond

        wireMockServer.verify(
            expectedTotalMessages,
            WireMock.postRequestedFor(WireMock.urlEqualTo("/chachkies"))
        )
    }

    private fun calculateExpectedTotalConstantUsersPerSecond(constantUsersPerSecond: Int, constantUsersPerSecondDuration: Int): Int {
        return constantUsersPerSecond * constantUsersPerSecondDuration
    }

    private fun calculateExpectedTotalRampUserPerSecond(rampUsersPerSecondMinimum: Int, rampUsersPerSecondMaximum: Int): Int {
        var counter = rampUsersPerSecondMaximum
        var accumulator = 0
        while (counter >= rampUsersPerSecondMinimum) {
            accumulator += counter
            counter -= 1
        }

        return accumulator
    }

    private fun buildRequestJson(
        constantUsersPerSecond: Int = 0,
        constantUsersPerSecondDuration: Int = 0,
        rampUsersPerSecondMinimum: Int = 0,
        rampUsersPerSecondMaximum: Int = 0,
        rampUsersPerSecondDuration: Int = 0
    ): String {
        // language=json
        return """
            {
              "rampUsersPerSecondMinimum": ${rampUsersPerSecondMinimum}, 
              "rampUsersPerSecondMaximum": ${rampUsersPerSecondMaximum}, 
              "rampUsersPerSecondDuration": ${rampUsersPerSecondDuration}, 
              "constantUsersPerSecond": ${constantUsersPerSecond}, 
              "constantUsersPerSecondDuration": ${constantUsersPerSecondDuration}, 
              "payload": "{\"name\": \"my fancy chachkie\"}", 
              "baseUrl": "http://localhost:9090", 
              "endpoint": "/chachkies"
            }
        """.trimIndent()
    }
}
