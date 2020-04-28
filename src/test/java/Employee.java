import demo.EmployeeRequest;
import demo.EmployeeResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class Employee {

    @Test
    public void getEmployee() {
        Response response = RestAssured
                .given()                                         // BDD Given
                .baseUri("http://dummy.restapiexample.com")      // tempat base URL
                .basePath("/api")
                .log()
                .all()
                .header("Content-type", "application/json")
                .get("/v1/employees");

        response.getBody().prettyPrint();
        System.out.println(response.getStatusCode());       // Print status code response

        // Validate
        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertThat("Lama sob", response.getTime(), Matchers.lessThan(3000L));

        // Validate response body using single path
        Assert.assertEquals("success", response.path("status"));
        Assert.assertEquals("Tiger Nixon", response.path("data[0].employee_name"));

        // Validate using Deserializer
        EmployeeResponse employeeResponse = response.as(EmployeeResponse.class);
        System.out.println(employeeResponse.getStatus());
        System.out.println(employeeResponse.getData().get(0).getEmployeeName());
    }

    @Test
    public void createEmployee() {
        //Serializer
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setName("Zaki Akhmad Faridzan");
        employeeRequest.setAge("21");
        employeeRequest.setSalary("6000");

        Response response = RestAssured
                .given()
                .baseUri("http://dummy.restapiexample.com")
                .basePath("/api")
                .log()
                .all()
                .header("Content-type", "application/json")
                .header("Accept", "*/*")
                .body(employeeRequest)
                .post("/v1/create");

        // Check response
        response.getBody().prettyPrint();
    }
}
