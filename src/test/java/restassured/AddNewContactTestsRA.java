package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class AddNewContactTestsRA {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZXZuaWtlbEBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTY2OTY1NDQzMSwiaWF0IjoxNjY5MDU0NDMxfQ.rDCx2hyjhlF6xZuGAGRDlZtZOMG9da10h3wLsiJZauw";

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }

    @Test
    public void addContactSuccess() {
        int i = new Random().nextInt(1000) + 1000;

        ContactDto contact = ContactDto.builder()
                .name("Anna" + i)
                .lastName("Fox" + i)
                .email("anna" + i + "@ukr.net")
                .phone("0534445" + i)
                .address("Tel Aviv")
                .description("friend")
                .build();
        given()
                .header("Authorization", token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message", containsString("Contact was added!"));
    }

    @Test
    public void AddNewContactEmptyLastName() {

        ContactDto contact = ContactDto.builder()
                .name("Anna")
                .lastName("")
                .email("anna@ukr.net")
                .phone("0534445")
                .address("Tel Aviv")
                .description("friend")
                .build();
        given()
                .header("Authorization", token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message.name",containsString("must not be blank"));
    }

    @Test
    public void addNewContactWrongEmailFormat(){
        ContactDto contact = ContactDto.builder()
                .name("Lisa")
                .lastName("Lis")
                .email("@gmail.com")
                .phone("1534225433")
                .address("Haifa")
                .description("Qa")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message.email",containsString("must be a well-formed email address"));
    }
    @Test
    public void addNewContactWrongPhoneFormat(){
        ContactDto contact = ContactDto.builder()
                .name("Lisa")
                .lastName("Lis")
                .email("lisa@gmail.com")
                .phone("0532245")
                .address("TA")
                .description("Qa")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message.phone",containsString("Phone number must contain only digits! And length min 10, max 15!"));
    }
    @Test
    public void addNewContactWithInvalidAddress(){
        ContactDto contact = ContactDto.builder()
                .name("Lisa")
                .lastName("Lis")
                .email("lisa@gmail.com")
                .phone("153224566992")
                .address(" ")
                .description("Qa")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message.address",containsString("must not be blank"));
    }

    @Test
    public void AddNewContactUnauthorized() {

        given()
                .header("Authorization", "ghgjfkf")
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("error",containsString("Unauthorized"))
                .assertThat().body("message",containsString("JWT strings must contain exactly 2 period characters. Found: 0"));

    }

}
