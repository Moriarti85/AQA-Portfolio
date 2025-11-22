package tests;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import pojo.SuccessRegisterData;
import pojo.UnSuccessRegisterData;
import pojo.User;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Slf4j
public class ApiTests extends APIBaseTest {

    @Test
    public void getUsersTest() {
        String endPoint = "api/users";

        log.info("Отпарвляем запрос на {} и получаем ответ", URL + endPoint);
        List<User> users = given()
                .when()
                .get(URL + endPoint)
                .then().log().all()
                .extract().body().jsonPath().getList("data", User.class);

        log.info("Выполняем проверки");
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

    @Test
    public void successRegisterTest() {
        int expectedID = 4;
        String expectedToken = "QpwL5tke4Pnpja7X4";

        SuccessRegisterData successRegisterData = given()
                .when()
                .body(new User("eve.holt@reqres.in", "pistol"))
                .post("api/register")
                .then().log().all()
                .extract().as(SuccessRegisterData.class);

        assertAll(
                () -> assertThat(successRegisterData.getId()).isEqualTo(expectedID),
                () -> assertThat(successRegisterData.getToken()).isEqualTo(expectedToken)
        );
    }

    @Test
    public void unSuccessRegister() {
        String expectedMessage = "Missing password";
        setupSpecifications(400);

        UnSuccessRegisterData unSuccessRegisterData = given()
                .when()
                .body(new User("sydney@fife"))
                .post("api/register")
                .then().log().all()
                .extract().as(UnSuccessRegisterData.class);

        assertThat(unSuccessRegisterData.getError()).as("Сообщение об ошибке не корректно").isEqualTo(expectedMessage);
    }
}
