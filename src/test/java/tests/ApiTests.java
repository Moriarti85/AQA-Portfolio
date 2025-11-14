package tests;

import io.restassured.http.ContentType;
import pojo.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ApiTests {
    private final static String URL = "https://reqres.in/";

    @Test
    public void getUsersTest() {
        String endPoint = "api/users";
        List<User> users = given()
                .when()
                .header("x-api-key", "reqres-free-v1")
                .contentType(ContentType.JSON)
                .get(URL + endPoint)
                .then()
                .statusCode(200)
                .log().all()
                .extract().body().jsonPath().getList("data", User.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(users.size())
                .as("Пользователи не получены по api: %s%s", URL, endPoint)
                .isNotZero();
        users.forEach(
                user -> {
                    softly.assertThat(user.getAvatar())
                            .as("Имя файла фото пользователя с ID: %s не содержит ID пользователя", user.getId())
                            .contains(String.valueOf(user.getId()));

                    String userEmail = user.getEmail();
                    String expectedEmailDomain = "@reqres.in";
                    softly.assertThat(userEmail)
                            .as("Почта %s не пренадлежит домену %s", userEmail, expectedEmailDomain)
                            .endsWith(expectedEmailDomain);
                });
        softly.assertAll();
    }
}
