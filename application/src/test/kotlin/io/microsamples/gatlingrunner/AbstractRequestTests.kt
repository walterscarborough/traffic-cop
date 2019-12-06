package io.microsamples.gatlingrunner

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
abstract class AbstractRequestTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    lateinit var wireMockServer: WireMockServer

    protected abstract fun setupWireMockStubs()

    protected abstract fun getRequestJson(
        constantUsersPerSecond: Int = 0,
        constantUsersPerSecondDuration: Int = 0,
        rampUsersPerSecondMinimum: Int = 0,
        rampUsersPerSecondMaximum: Int = 0,
        rampUsersPerSecondDuration: Int = 0
    ): String

    protected abstract fun verifyWireMockRequests(expectedTotalMessages: Int)

    @BeforeEach
    internal fun setUp() {
        wireMockServer = WireMockServer(options().dynamicPort())
        wireMockServer.start()

        setupWireMockStubs()
    }

    @AfterEach
    internal fun tearDown() {
        wireMockServer.stop()
    }

    @Test
    fun `load test works for constant users per second`() {
        val constantUsersPerSecond = 5
        val constantUsersPerSecondDuration = 5

        mockMvc.perform(
            MockMvcRequestBuilders.post("/run-load-test")
                .content(
                    getRequestJson(
                        constantUsersPerSecond = constantUsersPerSecond,
                        constantUsersPerSecondDuration = constantUsersPerSecondDuration
                    )
                )
                .header("Content-Type", MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)

        Thread.sleep(10000)

        val expectedTotalMessages = calculateExpectedTotalConstantUsersPerSecond(constantUsersPerSecond, constantUsersPerSecondDuration)
        verifyWireMockRequests(expectedTotalMessages)
    }

    @Test
    fun `load test works for ramp users per second`() {
        val rampUsersPerSecondMinimum = 1
        val rampUsersPerSecondMaximum = 5
        val rampUsersPerSecondDuration = 5

        mockMvc.perform(
            MockMvcRequestBuilders.post("/run-load-test")
                .content(
                    getRequestJson(
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
        verifyWireMockRequests(expectedTotalMessages)
    }

    @Test
    fun `load test works for constant and ramp users per second`() {
        val constantUsersPerSecond = 1
        val constantUsersPerSecondDuration = 5

        val rampUsersPerSecondMinimum = 1
        val rampUsersPerSecondMaximum = 5
        val rampUsersPerSecondDuration = 5

        mockMvc.perform(
            MockMvcRequestBuilders.post("/run-load-test")
                .content(
                    getRequestJson(
                        constantUsersPerSecond = constantUsersPerSecond,
                        constantUsersPerSecondDuration = constantUsersPerSecondDuration,
                        rampUsersPerSecondMinimum = rampUsersPerSecondMinimum,
                        rampUsersPerSecondMaximum = rampUsersPerSecondMaximum,
                        rampUsersPerSecondDuration = rampUsersPerSecondDuration
                    )
                )
                .header("Content-Type", MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "run-load-test",
                    requestFields(
                        fieldWithPath("rampUsersPerSecondMinimum").type(JsonFieldType.NUMBER).optional().description("Minimum number of users to simulate per second"),
                        fieldWithPath("rampUsersPerSecondMaximum").type(JsonFieldType.NUMBER).optional().description("Maximum number of users to simulate per second"),
                        fieldWithPath("rampUsersPerSecondDuration").type(JsonFieldType.NUMBER).optional().description("Number of seconds to ramp up users during a simulation"),
                        fieldWithPath("constantUsersPerSecond").type(JsonFieldType.NUMBER).optional().description("Constant number of users to simulate per second"),
                        fieldWithPath("constantUsersPerSecondDuration").type(JsonFieldType.NUMBER).optional().description("Number of seconds to simulate constant users"),
                        fieldWithPath("payload").type(JsonFieldType.STRING).optional().description("Payload to send with a POST or PUT request"),
                        fieldWithPath("baseUrl").type(JsonFieldType.STRING).description("Base url of the resource to run load test against"),
                        fieldWithPath("endpoint").type(JsonFieldType.STRING).description("Endpoint associated with base url of the resource to run load test against"),
                        fieldWithPath("httpMethod").type(JsonFieldType.STRING).description("HTTP method to use when sending load test requests")
                    )
                )
            )

        Thread.sleep(20000)

        val expectedTotalRampUserPerSecond = calculateExpectedTotalRampUserPerSecond(
            rampUsersPerSecondMinimum,
            rampUsersPerSecondMaximum
        )

        val expectedTotalConstantUsersPerSecond = calculateExpectedTotalConstantUsersPerSecond(constantUsersPerSecond, constantUsersPerSecondDuration)

        val expectedTotalMessages = expectedTotalRampUserPerSecond + expectedTotalConstantUsersPerSecond

        verifyWireMockRequests(expectedTotalMessages)
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
}
