import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.Before;

public class BaseUriParentClass {
    @Before
    @Step("Ввод урла для ручки")
    public void setUp(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

}
