package tests;

import pojo.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import utils.Specifications;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ApiTests {
    private final static String URL = "https://reqres.in/";

    @Test
    public void getUsersTest() {
        String endPoint = "api/users";
        Specifications.setSpec(Specifications.requestSpecification(URL), Specifications.responseSpecification200Ok());
        List<User> users = given()
                .when()
                .get(URL + endPoint)
                .then().log().all()
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
