package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class DeleteContactByIdRA {

    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZXZuaWtlbEBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTY2OTY1NDQzMSwiaWF0IjoxNjY5MDU0NDMxfQ.rDCx2hyjhlF6xZuGAGRDlZtZOMG9da10h3wLsiJZauw";

    String id;
    @BeforeMethod
    public void setUp(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";


        ContactDto contact = ContactDto.builder()
                .name("Anna")
                .lastName("Fox")
                .email("anna@ukr.net")
                .phone("053444599999")
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
        // Contact was added! ID: 761491f5-4014-44b5-a2a7-c97278710a04
        String [] all = message.split("ID: ");
        id = all[1];
        System.out.println(id);


    }
    @Test
    public void deleteContactById(){
        given()
                .header("Authorization",token)
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message",containsString("Contact was deleted!"));


    }

    @Test
    public void deleteContactByWrongIdContactFormatError(){
        given()
                .header("Authorization",token)
                .when()
                .delete("contacts/"+"52")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message",containsString("Contact with id: 52 not found in your contacts!"));
    }

    @Test
    public void deleteContactsByIdUnauthorized(){
        given()
                .header("Authorization","fghjbgbn")
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("error",containsString("Unauthorized"))
                .assertThat().body("message",containsString("JWT strings must contain exactly 2 period characters. Found: 0"));
    }
    @Test(enabled = false,  description = "Bag BUG - 400 code instead of 404")
    public void deleteContactsByIdContactNotFound(){
        given()
                .header("Authorization",token)
                .when()
                .delete("contacts/b82f21de-0269-4a8f-91f6-22dfedc08ahh")
                .then()
                .assertThat().statusCode(404)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message",containsString("Contact with id: b82f21de-0269-4a8f-91f6-22dfedc08ahh not found in your contacts!"));

    }
}
