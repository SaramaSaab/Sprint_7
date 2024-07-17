import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class TakeOrderFromCourierApiTest extends BaseUriParentClass {

    String idOrder = "176490";

    @Step("Успешное присвоение заказа курьеру")
    public void takeSuccessesOrder() {
        Response response =
                given()
                        .queryParam("courierId", "347477")
                        .when()
                        .put("/api/v1/orders/accept/" + idOrder);
        System.out.println("тело переменной из метода takeSuccessesOrder - ответ от сервака: " + response.asString());
        response.then().statusCode(200).assertThat().body("ok", equalTo(true));
        System.out.println("Метод takeSuccessesOrder() выполнен успешно");
    }

    @Step("Запрос без номера заказа")
    public void requestWithoutOrderId() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .put("/api/v1/orders/accept/courierId=347477");
        System.out.println("значение переменной ресурс номера без заказа " + response.asString());
        response.then().statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для поиска"));
        System.out.println("Метод requestWithoutOrderId() выполнен успешно");
    }

    @Step("Запрос с левым номером заказа")
    public void requestWtfOrderId() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .queryParam("courierId", 347477)
                        .put("/api/v1/orders/accept/" + "000");
        System.out.println("Результат переменной ресурс c левым номером заказа" + response.asString());
        response.then().statusCode(404).assertThat().body("message", equalTo("Заказа с таким id не существует"));
        System.out.println("Метод requestWtfOrderId() выполнен успешно");
    }

    @Step("Запрос с левым номером курьера")
    public void requestWtfCourierId() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .queryParam("courierId", 0)
                        .put("/api/v1/orders/accept/" + idOrder);
        System.out.println("Результат переменной ресурс с левым номером курьера" + response.asString());
        response.then().statusCode(404).assertThat().body("message", equalTo("Курьера с таким id не существует"));
        System.out.println("Метод requestWtfCourierId()) выполнен успешно");
    }

    @Step("Запрос с заказом, уже принятым в работу")
    public void requestAssignOrderToCourier() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .queryParam("courierId", 347477)
                        .put("/api/v1/orders/accept/" + idOrder);
        System.out.println("Результат переменной ресурс метода с уже принятым заказаом " + response.asString());
        response.then().statusCode(409).assertThat().body("message", equalTo("Этот заказ уже в работе"));
        System.out.println("Метод requestAssignOrderToCourier() выполнен успешно");
    }

    @Test
    @DisplayName("Присвоение заказа курьеру")
    @Description("Провери на негатив и позитиы при присвоению заказа курьеру")
    public void testAssignOrderToCourier() {
        TakeOrderFromCourierApiTest takeOrderFromCourierApiTest = new TakeOrderFromCourierApiTest();
        takeOrderFromCourierApiTest.takeSuccessesOrder();
        takeOrderFromCourierApiTest.requestWithoutOrderId();
        takeOrderFromCourierApiTest.requestWtfOrderId();
        takeOrderFromCourierApiTest.requestWtfCourierId();
        takeOrderFromCourierApiTest.requestAssignOrderToCourier();
    }
}

