package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.client.IngredientsClient;
import site.nomoreparties.stellarburgers.client.OrdersClient;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.models.Order;
import site.nomoreparties.stellarburgers.models.User;
import site.nomoreparties.stellarburgers.utils.UserCredentials;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OrderCreationTests {

    private OrdersClient ordersClient;
    private Order orderWithOneIngredient;
    private IngredientsClient ingredientsClient;
    private boolean ordersStatus;
    private UserClient userClient;
    private User user;
    private String userToken;
    public List<String> ingredients = new ArrayList<>();

    @Before
    public void setUp() {
        ordersClient = new OrdersClient();
        ingredientsClient = new IngredientsClient();
        user = User.getRandom();
        userClient = new UserClient();
        userClient.create(user);
        orderWithOneIngredient  = new Order();
    }

    @After
    public void tearDown() {
        int statusCode = userClient.login(UserCredentials.from(user)).extract().statusCode();
        if(statusCode == 200) {
            userToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
            userClient.delete(userToken);
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией: api/orders/")
    @Description("Создание заказа с ингридентами для авторизованного пользователя")
    public void createOrdersWithAuthorizationToken() {
        ValidatableResponse ingredientsResponse = ingredientsClient.getIngredients();
        ingredients = ingredientsResponse.extract().body().path("data._id");
        userToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
        ValidatableResponse response = ordersClient.createOrders(orderWithOneIngredient.setIngredient(ingredients.get(0)), userToken);

        response.statusCode(200);
        ordersStatus = response.extract().path("success");

        assertTrue(ordersStatus);
    }

    @Test
    @DisplayName("Создание заказа без авторизации: api/orders/")
    @Description("Создание заказа для неавторизованного пользователя вернёт ошибку")
    public void getUserOrdersWithoutAuthorizationToken() {
        ValidatableResponse ingredientsResponse = ingredientsClient.getIngredients();
        ingredients = ingredientsResponse.extract().body().path("data._id");
        userToken = "";
        ValidatableResponse response = ordersClient.createOrders(orderWithOneIngredient.setIngredient(ingredients.get(0)), userToken);

        response.statusCode(401);
        ordersStatus = response.extract().path("success");

        assertFalse(ordersStatus);
    }

    @Test
    @DisplayName("Создание заказа без ингридиентов для авторизованного пользователя: api/orders/")
    @Description("Безуспешное создание заказа без ингредиентов для авторизованного пользователя")
    public void createOrdersWithAuthorizationTokenWithoutIngredients() {
        userToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
        ValidatableResponse response = ordersClient.createOrders(orderWithOneIngredient.setIngredient(null), userToken);

        response.statusCode(400);
        ordersStatus = response.extract().path("success");

        assertFalse(ordersStatus);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов: api/orders/")
    @Description("Безуспешное создание заказа авторизованного пользователя с неверным id ингредиента")
    public void createOrdersWithIncorrectIngredientsIDs() {
        userToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
        ValidatableResponse response = ordersClient.createOrders(orderWithOneIngredient.createRandomIDOrderIngredient(), userToken);

        response.statusCode(400);
        ordersStatus = response.extract().path("success");

        assertFalse(ordersStatus);
    }
}