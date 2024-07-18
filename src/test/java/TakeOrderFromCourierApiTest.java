import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class TakeOrderFromCourierApiTest extends BaseUriParentClass {

    String idOrderNew = "293884";
    String idOrderRepeat = "176490";
    String courierId = "347477";

    @Test
    @DisplayName("Успешное присвоение заказа курьеру")
    public void takeSuccessesOrder() {
        TakeOrderFromCourierApiTest takeSuccessesOrder = new TakeOrderFromCourierApiTest();
        Response response = takeSuccessesOrder.apiPutNew();
        takeSuccessesOrder.assertThatPositive(response);
    }

    @Test
    @DisplayName("Запрос без номера заказа")
    public void requestWithoutOrderId() {
        TakeOrderFromCourierApiTest requestWithoutOrderId = new TakeOrderFromCourierApiTest();
        Response response = requestWithoutOrderId.apiPutWithoutNumberOrder();
        requestWithoutOrderId.assertThatWithoutNumberOrder(response);
    }

    @Test
    @DisplayName("Запрос с левым номером заказа")
    public void requestWtfOrderId() {
        TakeOrderFromCourierApiTest requestWithoutOrderId = new TakeOrderFromCourierApiTest();
        Response response = requestWithoutOrderId.apiPutWtfNumberOrder();
        requestWithoutOrderId.assertThatWtfNumberOrder(response);
    }

    @Test
    @DisplayName("Запрос с левым номером курьера")
    public void requestWtfCourierId() {
        TakeOrderFromCourierApiTest requestWithoutOrderId = new TakeOrderFromCourierApiTest();
        Response response = requestWithoutOrderId.apiPutWtfNumberCourier();
        requestWithoutOrderId.assertThatWtfNumberCourier(response);
    }

    @Test
    @DisplayName("Запрос с заказом, уже принятым в работу")
    public void requestAssignOrderToCourier() {
        TakeOrderFromCourierApiTest requestAssignOrderToCourier = new TakeOrderFromCourierApiTest();
        Response response = requestAssignOrderToCourier.apiPutRepeat();
        requestAssignOrderToCourier.assertThatExistOrder(response);
    }

    @Step("Отправка запроса на повторное присвоение заказа курьером")
    public Response apiPutRepeat(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .queryParam("courierId", courierId)
                        .put("/api/v1/orders/accept/" + idOrderRepeat);
        System.out.println("Результат переменной ресурс метода с уже принятым заказаом " + response.asString());
        return response;
    }

    @Step("Отправка запроса на первое принятие заказа")
    public Response apiPutNew(){
        Response response =
                given()
                        .queryParam("courierId", courierId)
                        .when()
                        .put("/api/v1/orders/accept/" + idOrderNew);
        System.out.println("тело переменной из метода takeSuccessesOrder - ответ от сервака: " + response.asString());
        return response;
    }

    @Step("Отпрвка запроса без номера заказа")
    public Response apiPutWithoutNumberOrder(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .put("/api/v1/orders/accept/courierId=347477");
        System.out.println("значение переменной ресурс номера без заказа " + response.asString());
        return response;
    }

    @Step("Отправка запроса с левым номером заказа")
    public Response apiPutWtfNumberOrder(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .queryParam("courierId", courierId)
                        .put("/api/v1/orders/accept/" + "456879");
        System.out.println("Результат переменной ресурс c левым номером заказа" + response.asString());
        return response;
    }

    @Step("Отправка запроса с левым номером курьера")
    public Response apiPutWtfNumberCourier(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .queryParam("courierId", 0)
                        .put("/api/v1/orders/accept/" + idOrderNew);
        System.out.println("Результат переменной ресурс с левым номером курьера" + response.asString());
        return response;
    }

    @Step("Сравнение тела ответа при успешном сквозном запросе")
    public void assertThatPositive(Response response){
        response.then().statusCode(200).assertThat().body("ok", equalTo(true));
        System.out.println("Метод takeSuccessesOrder() выполнен успешно");
    }

    @Step("Сравнение тела ответа при запросе без номера заказа")
    public void assertThatWithoutNumberOrder(Response response){
        response.then().statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для поиска"));
        System.out.println("Метод requestWithoutOrderId() выполнен успешно");
    }

    @Step("Сравнение тела ответа при запросе с левым номером заказа")
    public void assertThatWtfNumberOrder(Response response){
        response.then().statusCode(404).assertThat().body("message", equalTo("Заказа с таким id не существует"));
        System.out.println("Метод requestWtfOrderId() выполнен успешно");
    }

    @Step("Сравнение тела ответа при запросе с левым номером курьера")
    public void assertThatWtfNumberCourier(Response response){
        response.then().statusCode(404).assertThat().body("message", equalTo("Курьера с таким id не существует"));
        System.out.println("Метод requestWtfCourierId()) выполнен успешно");
    }

    @Step("Сравнение тела ответа при запросе с номером заказа, уже принятым в работу")
    public void assertThatExistOrder(Response response){
        response.then().statusCode(409).assertThat().body("message", equalTo("Этот заказ уже в работе"));
        System.out.println("Метод requestAssignOrderToCourier() выполнен успешно");
    }
}

