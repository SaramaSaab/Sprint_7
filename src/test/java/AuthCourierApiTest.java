import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;

public class AuthCourierApiTestPositive extends BaseUriParentClass {

    @Test
    @DisplayName("Авторизация курьером")
    @Description("Позитивная сквозная проверка на авторизацию")
    public void postApiLogin(){
        AuthCourier authCourier = new AuthCourier("Angry333", "1234");
        //создали переменную класса AuthCourier для данных авторизации
        AuthCourierApiTestPositive getApi = new AuthCourierApiTestPositive();
        //создали переменную класса AuthCourierApiTestPositive чтобы вызывать методы шагов
        Response response = getApi.apiPost(authCourier);
        //создали переменную, в которой будет храниться результат вызова шага apiPost(authCourier);
        getApi.assertThatApiPositive(response);
        //вызвали метод шага сравнения, положив в него данные из переменной response
    }

    @Test
    @DisplayName("Негативная проверка - логин")
    @Description("Авторизация с левым логином")
    public void authCourierWrongLogin(){
        AuthCourier courierWrongLogin = new AuthCourier("Ang", "1234");
        AuthCourierApiTestPositive authCourierApiTestPositive = new AuthCourierApiTestPositive();
        Response response = authCourierApiTestPositive.apiPost(courierWrongLogin);
        authCourierApiTestPositive.assertThatApiWtfLoginAndPassword(response);

    }

    @Test
    @DisplayName("Негативная проверка - пароль")
    @Description("Авторизация с левым паролем")
    public void authCourierWrongPass(){
        AuthCourier courierWrongPass = new AuthCourier("Angry", "12");
        AuthCourierApiTestPositive authCourierApiTestPositive = new AuthCourierApiTestPositive();
        Response response = authCourierApiTestPositive.apiPost(courierWrongPass);
        authCourierApiTestPositive.assertThatApiWtfLoginAndPassword(response);
    }

    @Test
    @DisplayName("Негативная проверка - пустой логин")
    @Description("Авторизация с пустым полем ввода логина")
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

    @Test
    @DisplayName("Негативная проверка - пустой пароль")
    @Description("Авторизация курьером с пустым полем ввода пароля")
    public void authCourierEmptyPass(){
        AuthCourier courierEmptyPass = new AuthCourier("Angry", "");
        AuthCourierApiTestPositive getApi = new AuthCourierApiTestPositive();
        Response response = getApi.apiPost(courierEmptyPass);
        getApi.assertThatApiEmptyPass(response);
    }

    @Step("Авторизация курьером - обращение к ручке")
    public Response apiPost(AuthCourier courier){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .when()
                        .post("/api/v1/courier/login");
        System.out.println("Тело переменной response " + response.asString());
        return response;
    }

    @Step("Сравнение ответа от сервера при авторизации с пустым паролем")
    public void assertThatApiEmptyPass(Response response){
        response.then().statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для входа"));
        System.out.println("Метод authCourierEmptyPass() выполнен успешно");
    }

    @Step("Сравнение ответа от сервера при авторизации с заполненными паролем и логином")
    public void assertThatApiPositive(Response response){
        response.then().statusCode(200).assertThat().body("id", not(empty()));
        System.out.println("Метод authCourierPositive() выполнен успешно");
    }

    @Step("Сравнение ответа от сервера при авторизации с левым логином")
    public void assertThatApiWtfLoginAndPassword(Response response){
        response.then().statusCode(404).assertThat().body("message", equalTo("Учетная запись не найдена"));
        System.out.println("Метод authCourierWrongLogin() выполнен успешно");
    }
}
