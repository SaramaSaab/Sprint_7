import getListOrders.ListOrders;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetOrdersListApiTest extends BaseUriParentClass{

    @Step("Получение списка заказов по ручке")
    public ListOrders getListOrders() {
        ListOrders listOrders =
                given()
                        .header("Content-type", "application/json")
                        .get("/api/v1/orders")
                        .body().as(ListOrders.class);
        return listOrders;
    }

    @Step("Сравнение ОР и ФР")
    public void assertThatResults(ListOrders listOrders){
        assertThat(listOrders, notNullValue());
    }

    @Test
    @DisplayName("Получение полного списка заказов")
    @Description("Список заказов, которые вообще есть в системе")
    public void getOrders(){
        GetOrdersListApiTest getOrdersListApiTest = new GetOrdersListApiTest();
        ListOrders response = getOrdersListApiTest.getListOrders();
        getOrdersListApiTest.assertThatResults(response);
    }
}
