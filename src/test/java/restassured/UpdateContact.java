package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class UpdateContact {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZXZuaWtlbEBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTY2OTY1NDQzMSwiaWF0IjoxNjY5MDU0NDMxfQ.rDCx2hyjhlF6xZuGAGRDlZtZOMG9da10h3wLsiJZauw";
    String id;
    @BeforeMethod
    public void setUp(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";

        ContactDto contact = ContactDto.builder()
                .name("Elena")
                .lastName("Elana123")
                .email("alena@gmail.com")
                .phone("5534445933")
                .address("Tel Aviv")
                .description("friend")
                .build();
        String message = given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .path("message");
//        ID: b82f21de-0269-4a8f-91f6-22dfedc08aff

        String[] all = message.split("ID: ");
        id = all[1];
        System.out.println("ID: "+id);
    }
    @Test
    public void updateContactEmailSuccess(){
        ContactDto contact = ContactDto.builder()
                .id(id)
                .name("Elena")
                .lastName("Elana123")
                .email("alena123@gmail.com")
                .phone("5534445933")
                .address("Tel Aviv")
                .description("friend")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message",containsString("Contact was updated"));
    }
    @Test
    public void updateContactPhoneSuccess(){
        ContactDto contact = ContactDto.builder()
                .id(id)
                .name("Elena")
                .lastName("Elana123")
                .email("alena@gmail.com")
                .phone("5522445933")
                .address("Tel Aviv")
                .description("friend")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message",containsString("Contact was updated"));
    }
    @Test
    public void updateContactNameFormatError(){
        ContactDto contact = ContactDto.builder()
                .id(id)
                .name("")
                .lastName("Elana123")
                .email("alena@gmail.com")
                .phone("5534445933")
                .address("Tel Aviv")
                .description("friend")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message.name",containsString("must not be blank"));
    }
    @Test
    public void updateContactEmailFormatError(){
        ContactDto contact = ContactDto.builder()
                .id(id)
                .name("Elena")
                .lastName("Elana123")
                .email("alenagmail.com")
                .phone("5534445933")
                .address("Tel Aviv")
                .description("friend")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message.email",containsString("must be a well-formed email address"));
    }
    @Test
    public void updateContactPhoneFormatError(){
        ContactDto contact = ContactDto.builder()
                .id(id)
                .name("Elena")
                .lastName("Elana123")
                .email("alena@gmail.com")
                .phone("55344459335678903456")
                .address("Tel Aviv")
                .description("friend")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message.phone",containsString("Phone number must contain only digits! And length min 10, max 15!"));
    }
    @Test
    public void updateContactUnauthorized(){
        given()
                .header("Authorization","fghjytr")
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("error",containsString("Unauthorized"))
                .assertThat().body("message",containsString("JWT strings must contain exactly 2 period characters. Found: 0"));
    }

    @Test (enabled = false,  description = "Bag BUG - 400 code instead of 404")
    public void updateContactNotFound(){
       ContactDto contact = ContactDto.builder()
                .id("02739f0f-9f3c-4e5b-b766-464938e008hj")
               .name("Elena")
               .lastName("Elana123")
               .email("alena@gmail.com")
               .phone("5534445933")
               .address("Tel Aviv")
               .description("friend")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(404)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message",containsString("Contact with id: 02739f0f-9f3c-4e5b-b766-464938e008hj not found in your contacts!"));
    }
}
