package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.models.User;
import site.nomoreparties.stellarburgers.utils.UserCredentials;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class UserLoginParameterizedTests {
    private static UserClient userClient = new UserClient();
    private static User user = User.getRandom();
    private final UserCredentials userCredentials;
    private final int expectedStatus;
    private final String expectedErrorMessage;

    public UserLoginParameterizedTests(UserCredentials userCredentials, int expectedStatus, String expectedErrorMessage) {
        this.userCredentials = userCredentials;
        this.expectedStatus = expectedStatus;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0}. status: {1}. message: {2}")
    public static Object[][] getUserLoginData() {
        return new Object[][] {
                {UserCredentials.getWithRandomEmail(user), 401, "email or password are incorrect"},
                {UserCredentials.getWithRandomPassword(user), 401, "email or password are incorrect"},
                {UserCredentials.getWithEmailOnly(user), 401, "email or password are incorrect"},
                {UserCredentials.getWithPasswordOnly(user), 401, "email or password are incorrect"},
        };
    }

    @Test
    @DisplayName("Логин с неверными авторизационнами данными пользователя / без обязательных полей: api/auth/login/")
    @Description("Логин пользователя с невалидными данными / без обязательных полей: невалидный email; невалидный пароль; без почты; без пароля")
    public void invalidRequestInNotAllowed() {
        userClient.create(user);

        ValidatableResponse response = userClient.login(userCredentials);

        response.assertThat().statusCode(expectedStatus);
        response.assertThat().body("message", equalTo(expectedErrorMessage));
    }
}