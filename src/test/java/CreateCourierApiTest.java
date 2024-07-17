import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierApiTest extends BaseUriParentClass {

    CreateCourier courier = new CreateCourier("Angry999", "1234", "birds999");

    @Step("Создание курьера")
    public void createFirstPositive(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
        System.out.println("Тело переменной response метода createFirstPositive() " + response.asString());
        response.then().statusCode(201).assertThat().body("ok", equalTo(true));
        System.out.println("Метод createDEmptyCourier() выполнен успешно");
    }

    @Step("Создание курьера с уже существующим в системе логином")
    public void createExistCourier(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
        System.out.println("Тело переменной response метода createExistCourier() " + response.asString());
        response.then().statusCode(409).assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
        System.out.println("Метод createDEmptyCourier() выполнен успешно");

    }

    @Step("Создание курьера с незаполненными полями логин/пароль/фамилия")
    public void createDEmptyCourier(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .post("/api/v1/courier");
        System.out.println("Тело переменной response метода createDEmptyCourier() " + response.asString());
        response.then().statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
        System.out.println("Метод createDEmptyCourier() выполнен успешно");
    }

    @Test
    @DisplayName("Набор тестов на создание курьера")
    @Description("Позитивные и негативные проверки при создании курьера")
    public void createCourier(){
        CreateCourierApiTest courier = new CreateCourierApiTest();
        courier.createFirstPositive();
        courier.createExistCourier();
        courier.createDEmptyCourier();
    }

    @After
    @Step("Удаление созданного курьера - очистка данных")
    public void clearCourier(){
        AuthCourier authCourier = new AuthCourier("Angry999", "1234");
        //проходим авторизацию созданным ранее курьером и записываем в переменную getIdCourier его id
        Response getIdCourier =
                given()
                        .header("Content-type", "application/json")
                        .body(authCourier)
                        .when()
                        .post("/api/v1/courier/login");
        System.out.println("Результат выполнения авторизации клиентом в методе clearCourier " + getIdCourier.asString());
        Integer id = getIdCourier.path("id");
        //в переменную Integer id впихнули результат выполнения из переменной Response getIdCourier
        //и по пути path("id") внесли в переменную Integer id его значение. И заодно привели его сразу
        //к типу Integer
        System.out.println("Тело переменной id, приведенной к типу int в методе clearCourier "+ id);
        //записываем в переменную response результат удаления
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .delete("/api/v1/courier/" + id);
        response.then().statusCode(200);
        System.out.println("Метод clearCourier() по удалению курьера выполнен успешно");
    }
}
