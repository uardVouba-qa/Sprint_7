import client.ScooterServiceClient;
import java.util.List;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import modal.Order;
import org.junit.Test;

public class GetOrdersTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка полученного списка заказов")
    public void getOrders_ok() {
        ScooterServiceClient client = new ScooterServiceClient(BASE_URI);
        List<Order> orders = client.getOrders().extract().jsonPath().getList("orders", Order.class);

    }
}
