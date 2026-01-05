package demo;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.LoginRequest;
import pojo.LoginResponse;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import java.io.File;

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
			
			//Add Product
			String token=loginResponse.getToken();
			String UserId=loginResponse.getUserId();
			RequestSpecification req2=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/").addHeader("authorization", token).build();
			
			RequestSpecification reqCreateProduct=given().spec(req2).param("productName", "Lewis")	.param("productAddedBy",UserId).param("productCategory","fashion")
					.param("productSubCategory","shirt").param("productPrice",11500).param("productDescription","Lewis Originals").param("productFor","women")
					.multiPart("productImage",new File("E:\\Piyumi\\Automation\\Rest Assured API Automation\\src\\test\\resources\\frock.png"));
			
			//Method A - Get the response into a String variable and create the JsonPath object (Json object) by inserting that String variable
			String responseCreateProduct=reqCreateProduct.when().post("/api/ecom/product/add-product").then().extract().response().asString(); 
			JsonPath js=new JsonPath(responseCreateProduct);
			System.out.println(js.getString("productId"));
			System.out.println(js.getString("message"));
			
			//Method B Get the response as a JsonPath object (Json object) directly
			JsonPath resCreateProduct=reqCreateProduct.when().post("/api/ecom/product/add-product").then().extract().response().jsonPath();
			System.out.println(resCreateProduct.getString("productId"));
			System.out.println(resCreateProduct.getString("message"));	
			
		}
}
