import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import modal.CreateOrder;
import client.ScooterServiceClient;


@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final ScooterServiceClient scooterServiceClient = new ScooterServiceClient(BASE_URI);
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private final CreateOrder createOrder;

    // Параметризованный конструктор
    public CreateOrderTest(CreateOrder createOrder) {
        this.createOrder = createOrder;
    }

    // Параметризованные данные для тестов
    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {new CreateOrder("Енгар", "Котик", "ул.Котиков 10", "Комсомольская", "89995001020", 3, "2025-04-12", "У меня лапки", new String[]{"BLACK"})},
                {new CreateOrder("Изюм", "Шпиц", "ул.Собачек 11", "Комсомольская", "89995001021", 5, "2025-04-14", "Хороший мальчик ждет самокат!", new String[]{"GREY"})},
                {new CreateOrder("Дарина", "Тимергалина", "ул.Комсомольская 11", "Комсомольская", "89995001023", 7, "2025-04-11", "", new String[]{"BLACK", "GREY"})},
                {new CreateOrder("Аврора", "Фролова", "ул.Проспект Октября 23", "Спортивная", "89995001025", 3, "2025-04-11", "", new String[]{})}
        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка создания заказа с различными данными")
    public void createOrder_ok() {
        ValidatableResponse response = scooterServiceClient.createOrder(createOrder);
        response.assertThat()
                .statusCode(201)
                .body("track", Matchers.notNullValue());
    }
}
