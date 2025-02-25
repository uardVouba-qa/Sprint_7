package client;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import modal.Courier;
import modal.CourierCredentials;
import static io.restassured.RestAssured.given;
import io.restassured.response.ValidatableResponse;
import modal.CreateOrder;
import java.util.Map;

public class ScooterServiceClient {
    private final String baseURI;
    public ScooterServiceClient(String baseURI){
        this.baseURI = baseURI;
    }


    @Step("Клиент - создание курьера")
    public ValidatableResponse createCourier(Courier courier){
        return given()
                .filter(new AllureRestAssured())
                .log()
                .all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .log().all();
    }
    @Step("Клиент - логин курьера")
    public ValidatableResponse login (CourierCredentials credentials){
        return given()
                .filter(new AllureRestAssured())
                .log()
                .all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .body(credentials)
                .post("/api/v1/courier/login")
                .then()
                .log().all();
    }

    @Step("Клиент - получение списка заказов")
    public ValidatableResponse getOrders(){
        return given()
                .filter(new AllureRestAssured())
                .log()
                .all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .queryParam("limit", "2")
                .get("/api/v1/orders")
                .then()
                .log().all();
    }

    @Step("Удаление курьера")
    public ValidatableResponse deleteCourier(String id) {
        return given()
                .filter(new AllureRestAssured())
                .log().all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .delete("/api/v1/courier/" + id)
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    @Step("Получение id курьера")
    public String getIdCourier(Courier courier) {
        ValidatableResponse response =
                given()
                        .log().all()
                        .baseUri(baseURI)
                        .header("Content-Type", "application/json")
                        .body(Map.of("login", courier.getLogin(), "password", courier.getPassword())) // предполагаем, что тело запроса должно быть отправлено
                        .post("/api/v1/courier/login") // исправлено "delete" на "post"
                        .then()
                        .log()
                        .all();

        String id = response
                .extract()
                .jsonPath()
                .getString("id");
        return id;
    }

    @Step("Клиент - создание заказа")
    public ValidatableResponse createOrder(CreateOrder createOrder) {
        return given()
                .filter(new AllureRestAssured())
                .log().all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .body(createOrder)
                .post("/api/v1/orders")
                .then()
                .log().all();
    }
}
