package ms.info.ms;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "address=http://localhost:8090/greek" })
public class MyController2Test {

    @LocalServerPort
    private int port;

    WireMockServer wireMockServer;

    @BeforeEach
    public void setup () {
        wireMockServer = new WireMockServer(8090);
        wireMockServer.start();
    }

    @AfterEach
    public void teardown () {
        wireMockServer.stop();
    }

    @Test
    void should_return_gods() {

        //Given
        wireMockServer.stubFor(get(urlEqualTo("/greek"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBodyFile("greek.json")));

        var expectedGodList = List.of(
                "Zeus",
                "Hera",
                "Poseidon",
                "Demeter",
                "Ares",
                "Athena",
                "Apollo",
                "Artemis",
                "Hephaestus",
                "Aphrodite",
                "Hermes",
                "Dionysus",
                "Hades",
                "Hypnos",
                "Nike",
                "Janus",
                "Nemesis",
                "Iris",
                "Hecate",
                "Tyche");

        //When
        //Then
        var address = "http://localhost:" + port + "/api/v1/webclient";
        RestAssured
            .given().log().all()
            .when().get(address)
            .then()
            .statusCode(200)
            .assertThat()
            .body("size()", is(20));

        List<String> gods = RestAssured
                .given()
                .when().get(address).as(new TypeRef<>() {});

        assertThat(gods).isEqualTo(expectedGodList);
    }

}
