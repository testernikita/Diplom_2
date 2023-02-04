package site.nomoreparties.stellarburgers;


import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.models.User;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserCreationTests {
    private UserClient userClient;
    private User user;
    private boolean userStatus;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandom();
    }

    @Test
    @DisplayName("Создание пользователя: api/auth/register/")
    @Description("Cоздание уникального пользователя с валидными данными: email, name, password")
    public void userCreationWithValidData() {
        ValidatableResponse userCreationResponse = userClient.create(user);

        userCreationResponse.statusCode(200);
        userStatus = userCreationResponse.extract().path("success");

        assertTrue("Пользователь не создан", userStatus);
    }

    @Test
    @DisplayName("Cоздание пользователя, который уже зарегистрирован: api/auth/register/")
    @Description("Создание двух неуникальных пользователей невозможно")
    public void creationOfTwoNotUniqueUsers() {
        userClient.create(user);

        ValidatableResponse anotherUserCreationResponse = userClient.create(user);

        anotherUserCreationResponse.statusCode(403);
        userStatus = anotherUserCreationResponse.extract().path("success");
        assertFalse("Пользователь создан повторно", userStatus);
        anotherUserCreationResponse.assertThat().body("message", equalTo("User already exists"));
    }
}