package demo;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.LoginRequest;
import pojo.LoginResponse;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

public class EcommerceAPITest {

		public static void main(String[] args) {
			
			RequestSpecification req= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/").setContentType(ContentType.JSON).build();
			ResponseSpecification res=new ResponseSpecBuilder().expectStatusCode(201).build();
			
			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setUserEmail("EMAIL");
			loginRequest.setUserPassword("PASSWORD");
			
			RequestSpecification reqLogin=given().spec(req).body(loginRequest);
			LoginResponse loginResponse= reqLogin.when().post("/api/ecom/auth/login").then().extract().response().as(LoginResponse.class);
			System.out.println(loginResponse.getUserId());
			System.out.println(loginResponse.getToken());
			System.out.println(loginResponse.getMessage());
		}
}
