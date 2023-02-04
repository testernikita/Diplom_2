package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.models.User;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class UserCreationParameterizedTests {
    private UserClient userClient = new UserClient();
    private final User user;
    private final int expectedStatus;
    private final String expectedErrorMessage;

    public UserCreationParameterizedTests(User user, int expectedStatus, String expectedErrorMessage) {
        this.user = user;
        this.expectedStatus = expectedStatus;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0}. status: {1}. message: {2}")
    public static Object[][] getCourierData() {
        return new Object[][] {
                {User.createUserWithoutEmail(), 403, "Email, password and name are required fields"},
                {User.createUserWithoutName(), 403, "Email, password and name are required fields"},
                {User.createUserWithoutPassword(), 403, "Email, password and name are required fields"},
        };
    }

    @Test
    @DisplayName("Создание пользователя и не заполнение одного из обязательных полей: api/auth/register/")
    @Description("Создание пользователя: без email; без имени; без пароля")
    public void cannotCreateCourierWithInvalidParameters() {

        ValidatableResponse response = userClient.create(user);

        response.assertThat().statusCode(expectedStatus);
        response.assertThat().body("message", equalTo(expectedErrorMessage));
    }
}
