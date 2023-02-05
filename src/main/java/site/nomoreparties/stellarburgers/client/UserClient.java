package site.nomoreparties.stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.RestAssuredClient;
import site.nomoreparties.stellarburgers.models.User;
import site.nomoreparties.stellarburgers.utils.UserCredentials;

import static io.restassured.RestAssured.given;

public class UserClient extends RestAssuredClient {
    private static final String AUTH_PATH = "api/auth/";

    @Step("Send POST request to api/auth/register/")
    public ValidatableResponse create(User client) {
        return given()
                .spec(getBaseSpec())
                .body(client)
                .when()
                .post(AUTH_PATH + "register/")
                .then();
    }

    @Step("Send POST request to api/auth/login/")
    public ValidatableResponse login(UserCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(AUTH_PATH + "login/")
                .then();
    }
}