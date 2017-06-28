/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.controlledthinking.dropwizard.TwilioPhoneNumberApplication;
import com.controlledthinking.dropwizard.beans.Privilege;
import com.controlledthinking.dropwizard.core.Customer;
import com.controlledthinking.dropwizard.core.User;
import com.controlledthinking.dropwizard.db.CustomerDAO;
import com.controlledthinking.dropwizard.db.UserDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;

public class CustomerResourceTest {

    private Logger logger = Logger.getLogger(CustomerResourceTest.class.getName());
    private static final CustomerDAO custDao = mock(CustomerDAO.class);
    private static final UserDAO userDao = mock(UserDAO.class);
    private final User user = new User("brintoul", Stream.of(Privilege.USER).collect(Collectors.toSet()));
    private final Customer theCustomer = new Customer();
    private ObjectMapper mapper = new ObjectMapper();
    private String authHeader;
    
//    @ClassRule
//    public static final ResourceTestRule resources = ResourceTestRule.builder()
//            .addResource(new CustomerResource(custDao,userDao))
//            .build();
    
    @Before
    public void setup() {
        try {
            TwilioPhoneNumberApplication.main(new String[] {"server", "twilio-numbers.yml"});
            ObjectNode obj = mapper.createObjectNode();
            obj.put("username", "brintoul");
            obj.put("password", "temporary");
            Response resp = given().
                contentType("application/json").
                body(obj.toString()).
                post("http://localhost:5656/auth/login");
            authHeader = "Bearer " + resp.getBody().asString();
        } catch (Exception ex) {
            Logger.getLogger(CustomerResourceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testFullCustomers() {
        try {
            theCustomer.setFirstName("Joe");
            theCustomer.setLastName("Blow");
            theCustomer.setNumberText("+16197849666");
            theCustomer.setUser(new User(1));
            List<Customer> customers = new ArrayList();
            //Add the customer
            given().
                    header("Authorization", authHeader).
                    contentType(ContentType.JSON).
                    body(mapper.writeValueAsString(theCustomer)).
                    put("http://localhost:5656/customers");
            //Get the list of customers
            customers = given().
                    header("Authorization", authHeader).
                    contentType(ContentType.JSON).
                    get("http://localhost:5656/customers/user").as(customers.getClass());
            logger.info(customers.toString());
            //Delete the customer
            given().
                    header("Authorization", authHeader).
                    contentType(ContentType.JSON).
                    delete("http://localhost:5656/customers/1");
            
        } catch (Exception ex) {
            Logger.getLogger(CustomerResourceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
