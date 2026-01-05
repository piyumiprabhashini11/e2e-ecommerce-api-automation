package demo;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.CreateOrderRequest;
import pojo.LoginRequest;
import pojo.LoginResponse;
import pojo.OrdersPojo;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EcommerceAPITest {

		public static void main(String[] args) {
			
			//Login
			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setUserEmail("EMAIL");
			loginRequest.setUserPassword("PASSWORD");
			
			RequestSpecification req= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/").setContentType(ContentType.JSON).build();
			RequestSpecification reqLogin=given().spec(req).body(loginRequest);
			LoginResponse loginResponse= reqLogin.when().post("/api/ecom/auth/login").then().extract().response().as(LoginResponse.class);
			System.out.println(loginResponse.getUserId());
			System.out.println(loginResponse.getToken());
			System.out.println(loginResponse.getMessage());
			ResponseSpecification res=new ResponseSpecBuilder().expectStatusCode(201).build();
			
			//Create Product
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
			JsonPath resCreateProduct=reqCreateProduct.when().post("/api/ecom/product/add-product").then().log().all().extract().response().jsonPath();
			System.out.println(resCreateProduct.getString("productId"));
			System.out.println(resCreateProduct.getString("message"));	
			
			//Create Order
			String productOrderdId=resCreateProduct.getString("productId");
			
			OrdersPojo orderValues=new OrdersPojo();
			orderValues.setCountry("Japan");
			orderValues.setProductOrderedId(productOrderdId);
			
			List<OrdersPojo> ordersList= new ArrayList<>();
			ordersList.add(orderValues);
			
			CreateOrderRequest orderDetails= new CreateOrderRequest();
			orderDetails.setOrders(ordersList);
			
			RequestSpecification request= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization", token).setContentType(ContentType.JSON) .build();	
			RequestSpecification reqCreateOrder=given().log().all().spec(request).body(orderDetails);
		    String responseCreateOrder=reqCreateOrder.when().post("/api/ecom/order/create-order").then().log().all().extract().response().asString();
		    System.out.println("The response of Create Oreder is "+responseCreateOrder);
					
		    JsonPath js3=new JsonPath(responseCreateOrder);
		    List<String> orderIds=js3.getList("orders");
		    String productOrderId=orderIds.get(0);
		    		
		    //Delete Product	
		    RequestSpecification req3=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("authorization",token).build();
		    RequestSpecification reqDeleteProduct= given().spec(req3).pathParam("productId", productOrderdId);
		    String responseDeleteProduct=reqDeleteProduct.when().delete("/api/ecom/product/delete-product/{productId}").then().log().all().extract().response().asString();
		   
		   //Delete Order
		   RequestSpecification req4=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("authorization",token).build();
		   RequestSpecification reqDeleteOrder= given().spec(req4).pathParam("orderId", productOrderId);
		   String responseDeleteOrder=reqDeleteOrder.when().delete("/api/ecom/order/delete-order/{orderId}").then().log().all().extract().response().asString();
		}
}
