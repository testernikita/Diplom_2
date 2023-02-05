package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.models.User;
import site.nomoreparties.stellarburgers.utils.UserCredentials;

import static org.junit.Assert.assertFalse;

public class UserChangingTests {

    private UserClient userClient;
    private User user;
    private String userToken;
    private boolean userStatus;

    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
        userClient.create(user);
    }

    @After
    public void tearDown() {
        userClient.delete(userToken);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации пользователя: api/auth/user/")
    @Description("Обновление данных пользователя без авторизации невозможно")
    public void changeUserWithoutAuthorizationToken() {
        userToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");

        ValidatableResponse response = userClient.change(UserCredentials.changeUser(user), " ");

        response.statusCode(401);
        userStatus = response.extract().path("success");

        assertFalse("Произошло изменение данных без авторизации", userStatus);
    }
}