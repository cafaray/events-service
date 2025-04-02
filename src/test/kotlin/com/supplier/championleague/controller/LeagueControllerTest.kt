package com.supplier.championleague.controller

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LeagueControllerTest {

    @Test
    fun testGetLeagues() {
        given()
            .`when`()
            .get("/api/leagues")
            .then()
            .statusCode(200)
    }

    // Add more test cases for other league endpoints
}
