import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;

public class AuthCourierApiTest extends BaseUriParentClass {

    @Step("Авторизация курьером")
    public void authCourierPositive(){
        AuthCourier authCourier = new AuthCourier("Angry333", "1234");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(authCourier)
                        .when()
                        .post("/api/v1/courier/login");
        System.out.println("Тело переменной response метода authCourier " + response.asString());
        response.then().statusCode(200).assertThat().body("id", not(empty()));
        System.out.println("Метод authCourierPositive() выполнен успешно");
    }

    @Step("Авторизация курьером с левым логином")
    public void authCourierWrongLogin(){
        AuthCourier courierWrongLogin = new AuthCourier("Ang", "1234");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(courierWrongLogin)
                        .when()
                        .post("/api/v1/courier/login");
        System.out.println("Тело переменной response метода authCourierWrongLogin()r " + response.asString());
        response.then().statusCode(404).assertThat().body("message", equalTo("Учетная запись не найдена"));
        System.out.println("Метод authCourierWrongLogin() выполнен успешно");

    }

    @Step("Авторизация курьером с левым паролем")
    public void authCourierWrongPass(){
        AuthCourier courierWrongPass = new AuthCourier("Angry", "12");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(courierWrongPass)
                        .when()
                        .post("/api/v1/courier/login");
        System.out.println("Тело переменной response метода authCourierWrongPass() " + response.asString());
        response.then().statusCode(404).assertThat().body("message", equalTo("Учетная запись не найдена"));
        System.out.println("Метод authCourierWrongPass() выполнен успешно");
    }

    @Step("Авторизация курьером с пустым полем ввода логина")
    public void authCourierEmptyLogin(){
        AuthCourier courierEmptyLogin = new AuthCourier("", "1234");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(courierEmptyLogin)
                        .when()
                        .post("/api/v1/courier/login");
        System.out.println("Тело переменной response метода authCourierEmptyLogin() " + response.asString());
        response.then().statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для входа"));
        System.out.println("Метод authCourierEmptyLogin() выполнен успешно");
    }

    @Step("Авторизация курьером с пустым полем ввода пароля")
    public void authCourierEmptyPass(){
        AuthCourier courierEmptyPass = new AuthCourier("Angry", "");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(courierEmptyPass)
                        .when()
                        .post("/api/v1/courier/login");
        System.out.println("Тело переменной response метода authCourierEmptyPass() " + response.asString());
        response.then().statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для входа"));
        System.out.println("Метод authCourierEmptyPass() выполнен успешно");
    }

    @Test
    @DisplayName("Авторизация курьером")
    @Description("Позитивные и негативные проверки на поля авторизации")
    public void authCourier(){
        AuthCourierApiTest authCourier = new AuthCourierApiTest();
        authCourier.authCourierPositive();
        authCourier.authCourierWrongLogin();
        authCourier.authCourierWrongPass();
        authCourier.authCourierEmptyLogin();
        authCourier.authCourierEmptyPass();
    }
}
