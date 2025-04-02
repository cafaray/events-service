package com.supplier.championleague.service

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import main.kotlin.com.supplier.championleague.service.MatchService

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MatchServiceTest {

    @Inject
    lateinit var matchService: MatchService

    @Test
    fun testServiceInitialization() {
        assertNotNull(matchService, "MatchService should be injected")
    }

    // Add more test cases for match service methods
}
