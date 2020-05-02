package com.walterscarborough.trafficcop

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.walterscarborough.trafficcop.context.HttpMethod
import com.walterscarborough.trafficcop.factories.RunLoadRequestFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
class ReportsTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    lateinit var wireMockServer: WireMockServer

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

    fun setupWireMockStubs() {
        wireMockServer.stubFor(
            WireMock.get(WireMock.urlEqualTo("/chachkies"))
                .willReturn(
                    WireMock.ok()
                )
        )
    }

    @Test
    fun `report is generated after load test is run`() {
        val initialReportsResult = mockMvc.perform(
            MockMvcRequestBuilders.get("/reports")
        ).andExpect(status().isOk)
            .andDo(document("reports"))
            .andReturn()

        val initialReportLinkCount = initialReportsResult.response.contentAsString.split("<a href=").size - 1

        mockMvc.perform(
            MockMvcRequestBuilders.post("/run-load-test")
                .content(
                    RunLoadRequestFactory.createJson(
                        constantUsersPerSecond = 1,
                        constantUsersPerSecondDuration = 3,
                        wireMockServerPort = wireMockServer.port(),
                        httpMethod = HttpMethod.GET
                    )
                )
                .header("Content-Type", MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)

        Thread.sleep(5000)

        val newReportsResult = mockMvc.perform(
            MockMvcRequestBuilders.get("/reports")
        ).andExpect(status().isOk)
            .andReturn()

        val newReportLinkCount = newReportsResult.response.contentAsString.split("<a href=").size - 1

        assertThat(newReportLinkCount).isGreaterThan(initialReportLinkCount)
    }

}
