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
    private final String color;

    // конструктор
    public CreateOrderTest(String color) {
        this.color = color;
    }

    // Параметризованные данные для тестов
    @Parameterized.Parameters
    public static Object[] getTestData() {
        return new Object[]{
                "BLACK",
                "GREY",
                "BLACK,GREY",
                ""
        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка создания заказа с различными цветами")
    public void createOrderSucsefull() {
        // Создаём объект заказа внутри теста, передавая только цвет
        CreateOrder createOrder = new CreateOrder(
                "Уард",
                "Тест",
                "Москва",
                "Комсомольская",
                "89995001021",
                3, // Количество дней
                "2025-04-15", // Дата доставки
                "Комментарий",
                color.isEmpty() ? null : color.split(",") // Преобразуем строку цвета в массив
        );

        ValidatableResponse response = scooterServiceClient.createOrder(createOrder);
        response.assertThat()
                .statusCode(201)
                .body("track", Matchers.allOf(
                        Matchers.notNullValue(),
                        Matchers.instanceOf(Integer.class),
                        Matchers.greaterThan(0)
                ));

    }
}
