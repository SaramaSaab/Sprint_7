import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderApiTest extends BaseUriParentClass{

    private final String firstNameApi;
    private final String lastNameApi;
    private final String addressApi;
    private final Number metroStationApi;
    private final String phoneApi;
    private final int rentTimeApi;
    private final String deliveryDateApi;
    private final String commentApi;
    private final String[] colorApi;

    public CreateOrderApiTest(String firstNameApi, String lastNameApi, String addressApi,
                              Number metroStationApi, String phoneApi, int rentTimeApi,
                              String deliveryDateApi, String commentApi, String[] colorApi) {
        this.firstNameApi = firstNameApi;
        this.lastNameApi = lastNameApi;
        this.addressApi = addressApi;
        this.metroStationApi = metroStationApi;
        this.phoneApi = phoneApi;
        this.rentTimeApi = rentTimeApi;
        this.deliveryDateApi = deliveryDateApi;
        this.commentApi = commentApi;
        this.colorApi = colorApi;
    }

    @Parameterized.Parameters
    public static Object[][] postParametersForOrder() {
        return new Object[][]{
                {"Naruto224444", "Uchiha22", "Konoha, 14222 apt.", 4, "+7 800 300 30 35", 5, "2020-06-06", "fjfjfjf", new String[]{"BLACK"}},
                {"Kate55555", "Kate", "Ausrtr, 666 apt.", 4, "8-000-664-33-00", 4, "2020-06-07", "", new String[]{"GRAY"}},
                {"Igor77777", "igor", "Kuraga, 7979797 apt.", 3, "8-000-664-33-00", 7, "2020-06-09", "fjf fjf 45959", new String[]{"BLACK","GREY"}},
                {"jane88888", "Dou", "Uganda, 1 apt.", 2, "8-333-664-33-99", 10, "2020-06-10", " ", new String[]{}}
        };
    }

    @Test
    @DisplayName("Создание заказа с использованием параметризации")
    @Description("Негативные и позитивные проверки ввода разных комбинаций данных в методе")
    public void createOrder(){
        CreateOrder createOrder = new CreateOrder(firstNameApi, lastNameApi, addressApi, metroStationApi,
                phoneApi, rentTimeApi, deliveryDateApi, commentApi, colorApi);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(createOrder)
                        .when()
                        .post("/api/v1/orders");
        System.out.println("Тело переменной response метода createOrder( " + response.asString());
        response.then().statusCode(201).assertThat().body("track", notNullValue());
        System.out.println("Метод createOrder() выполнен успешно ");
    }
}