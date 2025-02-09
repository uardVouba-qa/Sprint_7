import client.ScooterServiceClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import modal.Courier;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTest {
    private Courier courier;
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private final ScooterServiceClient client = new ScooterServiceClient(BASE_URI);
    // Тестовые данные
    public String login = "VoubaTest";
    public String password = "VoubaPassword";
    public String firstName = "VoubaTest";


    @Test
    @DisplayName("Успешное создание курьера")
    @Description("Проверка состояния кода и значений для полей /api/v1/courier")
    public void createCourier_ok() {
        courier = new Courier(login, password, firstName);
        ValidatableResponse response = client.createCourier(courier);
        response.assertThat().statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка состояния кода и сообщения при создании курьера без логина")
    public void createCourierWithoutLogin() {
        courier = new Courier("", password, firstName);
        ValidatableResponse response = client.createCourier(courier);
        response.assertThat().statusCode(400)
                .body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка состояния кода и сообщения при создании курьера без пароля")
    public void createCourierWithoutPassword() {
        courier = new Courier(login, "", firstName);
        ValidatableResponse response = client.createCourier(courier);
        response.assertThat().statusCode(400)
                .body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание копии курьера")
    @Description("Проверка состояния кода и сообщения при создании копии курьера")
    public void createSecondIdenticalCourier() {
        courier = new Courier(login, password, firstName);
        ValidatableResponse response = client.createCourier(courier);
        response.assertThat().statusCode(201)
                .body("ok", equalTo(true));

        //Создаем дубликат
        ValidatableResponse duplicate = client.createCourier(courier);
        duplicate.assertThat().statusCode(409)
                .body("message", Matchers.is("Этот логин уже используется. Попробуйте другой."));
    }


    @After
    public void after() {
        if (courier != null && courier.getLogin() != null && !courier.getLogin().isEmpty()) {
            // Получаем ID курьера
            String id = client.getIdCourier(courier);
            if (id != null) {
                // Удаляем курьера
                client.deleteCourier(id);
            }
        }
    }
}

