package com.controlledthinking.banana.test;

import com.controlledthinking.dropwizard.TwilioPhoneNumberApplication;
import com.controlledthinking.dropwizard.beans.Privilege;
import com.controlledthinking.dropwizard.core.Customer;
import com.controlledthinking.dropwizard.core.MessageGroup;
import com.controlledthinking.dropwizard.core.User;
import com.controlledthinking.dropwizard.db.CustomerDAO;
import com.controlledthinking.dropwizard.db.UserDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static io.restassured.RestAssured.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.BeforeClass;

public class CustomerResourceTest {

    private Logger logger = Logger.getLogger(CustomerResourceTest.class.getName());
    private static final CustomerDAO custDao = mock(CustomerDAO.class);
    private static final UserDAO userDao = mock(UserDAO.class);
    private final User user = new User("brintoul", Stream.of(Privilege.USER).collect(Collectors.toSet()));
    private final Customer theCustomer = new Customer();
    private final MessageGroup theGroup = new MessageGroup();
    private ObjectMapper mapper = new ObjectMapper();
    private String authHeader;
    
    @BeforeClass
    public static void startServer() {
        try {        
            TwilioPhoneNumberApplication.main(new String[] {"server", "twilio-numbers-test.yml"});
            RestAssured.port = Integer.valueOf(5656);
            RestAssured.baseURI = "http://localhost";
            RestAssured.basePath = "/";
        } catch (Exception ex) {
            Logger.getLogger(CustomerResourceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Before
    public void setup() {
        try {

            theCustomer.setFirstName("Joe");
            theCustomer.setLastName("Blow");
            theCustomer.setNumberText("+16197849666");
            theCustomer.setUser(new User(1));
            
            theGroup.setGroupName("Tester Group");

            ObjectNode obj = mapper.createObjectNode();
            obj.put("username", "brintoul");
            obj.put("password", "temporary");
            Response resp = given().
                body(obj.toString()).
                contentType(ContentType.JSON).
                post("auth/login");
            authHeader = "Bearer " + resp.getBody().asString();
            RestAssured.requestSpecification = new RequestSpecBuilder().
                    addHeader("Authorization", authHeader).
                    setContentType(ContentType.JSON).
                    build();
        } catch (Exception ex) {
            Logger.getLogger(CustomerResourceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testFullCustomer() {
        try {
            List<Customer> customers = new ArrayList();
            //Add the customer
            given().
                    body(mapper.writeValueAsString(theCustomer)).
                    put("customers");
            //Get the list of customers
            customers = given().
                    get("customers/user").as(customers.getClass());
            //Delete the customer
            given().
                    delete("customers/1");
            
        } catch (Exception ex) {
            Logger.getLogger(CustomerResourceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testFullMessageGroup() {
        try {
            //Add the customer
            given().
                    body(mapper.writeValueAsString(theCustomer)).
                    put("customers");
            //Add the group
            given().
                    body(mapper.writeValueAsString(theGroup)).
                    put("groups");
            //Add the customer to the group
            //TODO:  THIS IS UGLY.  ASSUMING THE INSERTED USER IS ID OF 2
            //SHOULD FIX BY USING AN IN-MEMORY DB WHICH GETS DESTROYED, ETC
            given().
                    post("groups/1/customers?customerId=2");
            //Get the user's groups
            get("groups/user").then().body("[0].groupName", equalTo("Tester Group"));
            //log().all() is pretty nice - easy way to see response
            get("customers/group/1").then().log().all().body("[0].lastName", equalTo("Blow"));
            given().delete("groups/1/customers/2").then().statusCode(200);
            //Should have no customers in the group
            get("customers/group/1").then().log().all().body("[0]", equalTo(null));
        } catch(Exception ex) {
            Logger.getLogger(CustomerResourceTest.class.getName()).log(Level.SEVERE, null, ex);            
        }
    }
}
