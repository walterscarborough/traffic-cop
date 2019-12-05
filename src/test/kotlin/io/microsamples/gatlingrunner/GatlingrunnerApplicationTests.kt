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
class GatlingrunnerApplicationTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    lateinit var wireMockServer: WireMockServer

    @BeforeEach
    internal fun setUp() {
        wireMockServer = WireMockServer(options().port(9090))
        wireMockServer.start()
    }

    @AfterEach
    internal fun tearDown() {
        wireMockServer.stop()
    }

    @Test
    fun `POST requests works for constant users per second`() {
        wireMockServer.stubFor(
            WireMock.post(WireMock.urlEqualTo("/chachkies"))
                .willReturn(
                    ok()
                )
        )

        val constantUsersPerSecond = 5
        val constantUsersPerSecondDuration = 5

        mockMvc.perform(
            post("/run-load-test")
                .content(
                    // language=json
                    """
					{
                      "rampUsersPerSecondMinimum": 0, 
                      "rampUsersPerSecondMaximum": 0, 
                      "rampUsersPerSecondDuration": 0, 
                      "constantUsersPerSecond": ${constantUsersPerSecond}, 
                      "constantUsersPerSecondDuration": ${constantUsersPerSecondDuration}, 
                      "payload": "{\"name\": \"my fancy chachkie\"}", 
                      "baseUrl": "http://localhost:9090", 
                      "endpoint": "/chachkies"
                    }
					""".trimIndent()
                )
                .header("Content-Type", MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)

        Thread.sleep(10000)

        val expectedTotalMessages = constantUsersPerSecond * constantUsersPerSecondDuration
        wireMockServer.verify(
            expectedTotalMessages,
            WireMock.postRequestedFor(WireMock.urlEqualTo("/chachkies"))
        )
    }
}
