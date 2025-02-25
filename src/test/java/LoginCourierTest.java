import client.ScooterServiceClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import modal.Courier;
import modal.CourierCredentials;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;

public class LoginCourierTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private Courier courier;
    private ScooterServiceClient scooterServiceClient;

    // Тестовые данные редактировать здесь
    public String login = "VoubaTest";
    public String password = "VoubaPassword";
    public String firstName = "VoubaTest";


    @Before
    public void before() {
        scooterServiceClient = new ScooterServiceClient(BASE_URI);
        ScooterServiceClient client = new ScooterServiceClient(BASE_URI);
        courier = new Courier(login, password, firstName);
        ValidatableResponse response = client.createCourier(courier);
        Assume.assumeTrue(response.extract().statusCode() == 201);
    }

    @Test
    @DisplayName("Авторизация курьера")
    @Description("Проверка состояния кода и сообщения при авторизации курьера")
    public void login_ok() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        ValidatableResponse response = scooterServiceClient.login(credentials);
        response.assertThat()
                .statusCode(200)
                .body("id", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Авторизация без логина")
    @Description("Проверка состояния кода и сообщения при авторизации без логина")
    public void loginWithEmptyUserName() {
        CourierCredentials credentials = new CourierCredentials("", courier.getPassword());
        ValidatableResponse response = scooterServiceClient.login(credentials);
        response.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без без пароля")
    @Description("Проверка состояния кода и сообщения при авторизации без пароля")
    public void loginWithEmptyPassword() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), "");
        ValidatableResponse response = scooterServiceClient.login(credentials);
        response.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера с несуществующими кредами")
    @Description("Проверка состояния кода и сообщения при отсутствии курьера в БД")
    public void loginNonExistCourier() {
        CourierCredentials credentials = new CourierCredentials(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
        ValidatableResponse response = scooterServiceClient.login(credentials);
        response.assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void after() {
        // Получаем ID курьера
        String id = scooterServiceClient.getIdCourier(courier);
        assertNotNull(id, "ID курьера не должен быть null");

        // Удаляем курьера
        scooterServiceClient.deleteCourier(id);
    }
}



