import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class DeleteCourierApiTest extends BaseUriParentClass{

    @Step("Удаление курьера с несуществующим id")
    public void deleteWrongCourierId(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .delete("/api/v1/courier/0");
        System.out.println("Тело переменной response метода deleteWrongCourierId() " + response.asString());
        response.then().statusCode(404).assertThat().body("message", equalTo("Курьера с таким id нет."));
        System.out.println("Метод deleteWrongCourierId() выполнен успешно");
    }

    @Step("Удаление курьера без id")
    public void deleteCourierEmptyId(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .delete("/api/v1/courier/");
        System.out.println("Тело переменной response метода deleteCourierEmptyId() " + response.asString());
        response.then().statusCode(404).assertThat().body("message", equalTo("Not Found."));
        System.out.println("Метод deleteWrongCourierId() выполнен успешно");
    }

    @Test
    @DisplayName("Метод удаления курьеров")
    @Description("Удаление с левым id и удаление с пустым id - без него")
    public void deleteCourier(){
        DeleteCourierApiTest deleteCourierApiTest = new DeleteCourierApiTest();
        deleteCourierApiTest.deleteWrongCourierId();
        deleteCourierApiTest.deleteCourierEmptyId();
    }
}
