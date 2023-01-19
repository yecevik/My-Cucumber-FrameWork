

        package APIPackage;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

//to change the order of execution we use fix method order since it is executing in top to bottom approach
//which is not good for us.
//this method sorters will execute my methods in ascending/alphabetically order
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HardCodedExamples {

    //one thing to remember
    //base URI - base URL
    //end then using when keyword, we will send the end point

    String baseURI =  RestAssured.baseURI = "http://hrm.syntaxtechs.net/syntaxapi/api";

    //we need to perform CRUD operations
    String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NzQwODk2ODcsImlzcyI6ImxvY2FsaG9zdCIsImV4cCI6MTY3N" +
            "DEzMjg4NywidXNlcklkIjoiNDc3NiJ9.qU2FG3PpTNPmytS5-7Osm6kBuIGSoVvP7dhpBys10CM";
    static String employee_id;

    @Test
    public void bgetOneEmployee(){
        //prepare the request
        //to prepare the request, we use request specification
        RequestSpecification request = given().header("Authorization", token)
                .header("Content-Type", "application/json")
                .queryParam("employee_id", employee_id);

        //to hit the end point/ to make the request which will return response
        Response response = request.when().get("/getOneEmployee.php");

        // System.out.println(response.asString());
        response.prettyPrint();
        //verifying the status code
        response.then().assertThat().statusCode(200);

        //using jsonpath method, we are extracting the value from the response body
        String firstName = response.jsonPath().getString("employee.emp_firstname");
        System.out.println(firstName);
        //first way of assertion
        Assert.assertEquals(firstName, "Yusuf");
        //second way of assertion to verify the value in response body using hamcrest matchers
        response.then().assertThat().body("employee.emp_firstname", equalTo("Yusuf"));
    }

    @Test
    public void acreateEmployee(){
        RequestSpecification request = given().header("Authorization", token)
                .header("Content-Type", "application/json").
                body("{\n" +
                        "  \"emp_firstname\": \"Yusuf\",\n" +
                        "  \"emp_lastname\": \"Cevik\",\n" +
                        "  \"emp_middle_name\": \"Engin\",\n" +
                        "  \"emp_gender\": \"M\",\n" +
                        "  \"emp_birthday\": \"1990-01-11\",\n" +
                        "  \"emp_status\": \"confirmed\",\n" +
                        "  \"emp_job_title\": \"QA manager\"\n" +
                        "}");
        Response response = request.when().post("/createEmployee.php");
        response.prettyPrint();
        //verfying the statuys code which is 201
        response.then().assertThat().statusCode(201);
        //getting the employee id from the response and use it as static one
        employee_id = response.jsonPath().getString("Employee.employee_id");
        System.out.println(employee_id);
        response.then().assertThat().body("Employee.emp_lastname", equalTo("Cevik"));
        response.then().assertThat().body("Employee.emp_middle_name", equalTo("Engin"));
        //verify console header
        response.then().assertThat().header("Access-Control-Allow-Headers", "Access-Control-Allow-Headers,Content-Type," +
                "Access-Control-Allow-Methods, " +
                "Authorization, X-Requested-With");

    }

    @Test
    public void cupdateEmployee(){
        RequestSpecification request = given().header("Authorization", token)
                .header("Content-Type", "application/json").
                body("{\n" +
                        "  \"employee_id\": \""+employee_id+"\",\n" +
                        "  \"emp_firstname\": \"James\",\n" +
                        "  \"emp_lastname\": \"Bond\",\n" +
                        "  \"emp_middle_name\": \"007\",\n" +
                        "  \"emp_gender\": \"M\",\n" +
                        "  \"emp_birthday\": \"1955-01-14\",\n" +
                        "  \"emp_status\": \"classified\",\n" +
                        "  \"emp_job_title\": \"Secret Agent (Spy)\"\n" +
                        "}");

        Response response = request.when().put("/updateEmployee.php");
        response.prettyPrint();
        //verification
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("Message", equalTo("Employee record Updated"));
    }

    @Test
    public void dgetUpdatedEmployee(){
        RequestSpecification request = given().header("Authorization", token)
                .header("Content-Type", "application/json")
                .queryParam("employee_id", employee_id);

        //to hit the end point/ to make the request which will return response
        Response response = request.when().get("/getOneEmployee.php");

        // System.out.println(response.asString());
        response.prettyPrint();
        //verifying the status code
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("employee.emp_job_title", equalTo("Secret Agent (Spy)"));
    }

}