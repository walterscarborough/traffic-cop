package io.microsamples.gatlingrunner

import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class LoadControllerTest {

    private lateinit var mockMvc: MockMvc
    private val easyRandom = EasyRandom()

    @BeforeEach
    internal fun setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(LoadController(AsyncService()))
                .build()
    }

    @Test
    fun `run-test-post should work with a normal chachkie`() {
        val chachkie = easyRandom.nextObject(Chachkie::class.java)

        val chachkieJson = JacksonConfiguration().getObjectMapperWithJsr().writeValueAsString(chachkie)

        mockMvc.perform(
                post("/run-test-post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(chachkieJson)
        ).andExpect(status().is2xxSuccessful)
    }
}
