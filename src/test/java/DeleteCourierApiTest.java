import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class DeleteCourierApiTest extends BaseUriParentClass{

    String id = "0";

    @Test
    @DisplayName("Удаление курьера с несуществующим id")
    public void deleteWrongCourierId(){
        DeleteCourierApiTest deleteWrongCourierId = new DeleteCourierApiTest();
        Response response = deleteWrongCourierId.deleteRequest();
        deleteWrongCourierId.assertThatEmptyId(response);
    }

    @Test
    @DisplayName("Удаление курьера без id")
    public void deleteCourierEmptyId(){
        DeleteCourierApiTest deleteCourierEmptyId = new DeleteCourierApiTest();
        Response response = deleteCourierEmptyId.deleteWithoutId();
        deleteCourierEmptyId.assertThatWithoutId(response);
    }

@Step("Авторизация по ручке до удаления курьера с несуществующим айди")
    public Response deleteRequest(){
    Response response =
            given()
                    .header("Content-type", "application/json")
                    .when()
                    .delete("/api/v1/courier/" + id);
    System.out.println("Тело переменной response метода deleteCourierEmptyId() " + response.asString());
    return response;
}

@Step("Сравнение ответа на запрос с несуществующим айди")
    public void assertThatEmptyId(Response response){
    response.then().statusCode(404).assertThat().body("message", equalTo("Курьера с таким id нет."));
    System.out.println("Метод deleteWrongCourierId() выполнен успешно");
}

@Step("Авторизация по ручке до удаления курьера без айди")
    public Response deleteWithoutId(){
    Response response =
            given()
                    .header("Content-type", "application/json")
                    .when()
                    .delete("/api/v1/courier/");
    System.out.println("Тело переменной response метода deleteCourierEmptyId() " + response.asString());
    return response;
}
    @Step("Сравнение ответа на запрос без айди")
    public void assertThatWithoutId(Response response){
        response.then().statusCode(404).assertThat().body("message", equalTo("Not Found."));
        System.out.println("Метод deleteWrongCourierId() выполнен успешно");
    }


}
