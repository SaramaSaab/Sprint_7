import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TakeOrderFromNumberApiTest extends BaseUriParentClass{

    @Step("Получить заказ по его номеру")
    public void takeOrderFromNumberPositive(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .queryParam("t", "937232")
                        .when()
                        .get("/api/v1/orders/track");
        System.out.println("Тело переменной response метода takeOrderFromNumberPositive()" + response.asString());
        response.then().statusCode(200).body("order", notNullValue());
        System.out.println("Метод takeOrderFromNumberPositive() выполнен успешно");
    }

    @Step("Получение заказа без номера")
    public void takeOrderWithoutNumber(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get("/api/v1/orders/track");
        System.out.println("Тело переменной response метода  takeOrderWithoutNumber" + response.asString());
        response.then().statusCode(400).body("message", equalTo("Недостаточно данных для поиска"));
        System.out.println("Метод takeOrderWithoutNumber() выполнен успешно");
    }

    @Step("Получение заказа с левым номером")
    public void takeOrderNotExistNumber(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .queryParam("t", "0")
                        .when()
                        .get("/api/v1/orders/track");
        System.out.println("Тело переменной response метода takeOrderNotExistNumber() " + response.asString());
        response.then().statusCode(404).body("message", equalTo("Заказ не найден"));
        System.out.println("Метод takeOrderNotExistNumber() выполнен успешно " + response.asString());
    }

    @Test
    @DisplayName("Получение заказа но его номеру")
    @Description("Негативные и позитивные проверки отправки ручки с разными вводными")
    public void takeOrderFromNumber(){
        TakeOrderFromNumberApiTest takeOrderFromNumberApiTest = new TakeOrderFromNumberApiTest();
        takeOrderFromNumberApiTest.takeOrderFromNumberPositive();
        takeOrderFromNumberApiTest.takeOrderWithoutNumber();
        takeOrderFromNumberApiTest.takeOrderNotExistNumber();
    }
}
