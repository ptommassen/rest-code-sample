package software.uniqore.storedemo.rest

import io.restassured.RestAssured
import io.restassured.RestAssured.DEFAULT_PORT
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.hasItem
import org.junit.Before
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
internal class StoresRestAPITest {

    @Before
    fun setup() {
        RestAssured.port = DEFAULT_PORT
    }

    @Test
    fun `does anything`() {
        given().get("/stores").then().statusCode(200).and().body("name", hasItem("Goudglans' Schroefjesgigant"))
    }
}