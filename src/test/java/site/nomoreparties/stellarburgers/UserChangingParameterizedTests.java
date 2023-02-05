package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.models.User;
import site.nomoreparties.stellarburgers.utils.UserCredentials;

@RunWith(Parameterized.class)
public class UserChangingParameterizedTests {

    private static UserClient userClient = new UserClient();
    private static User user = User.getRandom();
    private String userToken;
    private final UserCredentials userCredentials;
    private final int expectedStatus;
    private final boolean expectedChangeStatus;

    @After
    public void tearDown() {
        userClient.delete(userToken);
    }

    public UserChangingParameterizedTests(UserCredentials userCredentials, int expectedStatus, boolean expectedChangeStatus) {
        this.userCredentials = userCredentials;
        this.expectedStatus = expectedStatus;
        this.expectedChangeStatus = expectedChangeStatus;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0}. status: {1}. success: {2}")
    public static Object[][] getChangeUserLoginData() {
        return new Object[][] {
                {UserCredentials.changeUserEmailOnly(user), 200, true},
                {UserCredentials.changeUserPasswordOnly(user), 200, true},
                {UserCredentials.changeUserNameOnly(user), 200, true},
                {UserCredentials.changeUser(user), 200, true},
        };
    }

    @Test
    @DisplayName("Изменение данных пользователя: api/auth/user/")
    @Description("Изменение данных пользователя: изменение email; изменение пароля; изменение имени; изменение всех данных одновременно")
    public void successChangeUserData() {
        userClient.create(user);
        userToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");

        ValidatableResponse response = userClient.change(userCredentials, userToken);

        response.assertThat().statusCode(expectedStatus);
        response.extract().path("success").equals(expectedChangeStatus);
    }
}