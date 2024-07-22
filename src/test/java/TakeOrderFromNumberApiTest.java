import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TakeOrderFromNumberApiTest extends BaseUriParentClass{

    String idExistOrder =  "937232";
    String idWtfOrder = "0";

    @Test
    @DisplayName("Получить заказ по его номеру")
    public void takeOrderFromNumberPositive(){
        TakeOrderFromNumberApiTest numberOrder = new TakeOrderFromNumberApiTest();
        Response response = numberOrder.apiGetPositive();
        numberOrder.assertThatPositive(response);
    }

    @Test
    @DisplayName("Получение заказа без номера")
    public void takeOrderWithoutNumber(){
        TakeOrderFromNumberApiTest withoutOrder = new TakeOrderFromNumberApiTest();
        Response response = withoutOrder.apiGetWithoutNumber();
        withoutOrder.assertThatGetWithoutOrder(response);
    }

    @Test
    @DisplayName("Получение заказа с левым номером")
    public void takeOrderNotExistNumber(){
        TakeOrderFromNumberApiTest notExistOrder = new TakeOrderFromNumberApiTest();
        Response response = notExistOrder.apiGetWtfNumber();
        notExistOrder.assertThatWtfNumberOrder(response);
    }

    @Step("Отправка запроса на сквозное получение заказа с существующим номером")
    public Response apiGetPositive(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .queryParam("t", idExistOrder)
                        .when()
                        .get("/api/v1/orders/track");
        System.out.println("Тело переменной response метода takeOrderFromNumberPositive()" + response.asString());
        return response;
    }

    @Step("Отправка запроса на получение заказа без номера")
    public Response apiGetWithoutNumber(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get("/api/v1/orders/track");
        System.out.println("Тело переменной response метода  takeOrderWithoutNumber" + response.asString());
        return response;
    }

    @Step("Отправка запроса на получение заказа с левым номером")
    public Response apiGetWtfNumber(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .queryParam("t", idWtfOrder)
                        .when()
                        .get("/api/v1/orders/track");
        System.out.println("Тело переменной response метода takeOrderNotExistNumber() " + response.asString());
        return response;
    }

    @Step("Сравнение ОР и ФР после отправки запроса на получение заказа по его номеру")
    public void assertThatPositive(Response response){
        response.then().statusCode(200).body("order", notNullValue());
        System.out.println("Метод takeOrderFromNumberPositive() выполнен успешно");
    }

    @Step("Сравнение ОР и ФР после отправки запроса на получение заказа без номера")
    public void assertThatGetWithoutOrder(Response response){
        response.then().statusCode(400).body("message", equalTo("Недостаточно данных для поиска"));
        System.out.println("Метод takeOrderWithoutNumber() выполнен успешно");
    }

    @Step("Сравнение ОР и ФР после отпрвки запроса на получение заказа по левому номеру")
    public void assertThatWtfNumberOrder(Response response){
        response.then().statusCode(404).body("message", equalTo("Заказ не найден"));
        System.out.println("Метод takeOrderNotExistNumber() выполнен успешно " + response.asString());
    }
}
