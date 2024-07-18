import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierApiTest extends BaseUriParentClass {

    String login = "Анна123";
    String password = "1234";
    String firstName = "Анновна123";

    CreateCourier courier = new CreateCourier(login, password, firstName);
    CreateCourier courierExist = new CreateCourier("Гал67", "1234", "гал67");

    @Test
    @DisplayName("Создание курьера с нуля")
    public void createFirstPositive() {
        CreateCourierApiTest firstRega = new CreateCourierApiTest();
        Response response = firstRega.authWithDataCourier(courier);
        firstRega.assertThatPositiveFirstRegistration(response);
    }

    @Test
    @DisplayName("Создание курьера с уже существующим в системе логином")
    public void createExistCourier() {
        CreateCourierApiTest existCourier = new CreateCourierApiTest();
        Response response = existCourier.authWithDataCourier(courierExist);
        existCourier.assertThatExistDataCourier(response);
    }

    @Test
    @DisplayName("Создание курьера с незаполненными полями логин/пароль/фамилия")
    public void createdEmptyCourier() {
        CreateCourierApiTest emptyDataCourier = new CreateCourierApiTest();
        Response response = emptyDataCourier.authWithoutBodyApi();
        emptyDataCourier.assertThatWithoutDataCourier(response);
    }

    @Step("Авторизация по ручке - без body, пустой логин/пароль/фио")
    public Response authWithoutBodyApi() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .post("/api/v1/courier");
        System.out.println("Тело переменной response метода createdEmptyCourier() " + response.asString());
        return response;
    }

    @Step("Авторизация по ручке - с body и данными курьера")
    public Response authWithDataCourier(CreateCourier courier) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
        return response;
    }

    @Step("Сравнение тела ответа на проверку авторизации без данных")
    public void assertThatWithoutDataCourier(Response response) {
        response.then().statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
        System.out.println("Метод createdEmptyCourier() выполнен успешно");
    }

    @Step("Сравнение тела ответа на проверку сквозной регистрации - логин+пароль+фамилия")
    public void assertThatPositiveFirstRegistration(Response response) {
        response.then().statusCode(201).assertThat().body("ok", equalTo(true));
        System.out.println("Метод createFirstPositive выполнен успешно");
    }

    @Step("Сравнение тела ответа на проверку регистрации с уже существующими данными в БД")
    public void assertThatExistDataCourier(Response response) {
        response.then().statusCode(409).assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
        System.out.println("Метод createExistCourier выполнен успешно");
    }

    @After
    public void clearCourier() {
            AuthCourier authCourier = new AuthCourier(login, password);
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
            System.out.println("Тело переменной id, приведенной к типу int в методе clearCourier " + id);
            if(id != null) {
                //записываем в переменную response результат удаления
                Response response =
                        given()
                                .header("Content-type", "application/json")
                                .when()
                                .delete("/api/v1/courier/" + id);
                response.then().statusCode(200);
                System.out.println("Метод clearCourier() по удалению курьера выполнен успешно");
            } else {
                System.out.println("Удалять пока нечего");
            }
        }
}