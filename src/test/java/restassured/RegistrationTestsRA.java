package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.AuthRequestDto;
import dto.ErrorDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class RegistrationTestsRA {

    @BeforeMethod
    public void setUp(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }

    @Test
    public void registrationSuccess(){
        int i = new Random().nextInt(1000)+1000;
        AuthRequestDto auth = AuthRequestDto.builder().username("ev"+i+"@gmail.com").password("Elena1234$@").build();

        String  token = given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .path("token");
        System.out.println(token);

    }
    @Test
    public void registrationWrongUserName(){

        AuthRequestDto auth = AuthRequestDto.builder().username("nogmail.com").password("Elena1234$@").build();

        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.username",containsString("must be a well-formed email address"));


    }

    @Test
    public void registrationUserAlreadyExists(){

        AuthRequestDto auth = AuthRequestDto.builder().username("evnikel@gmail.com").password("Elena1234$@").build();

        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(409)
                .assertThat().body("message",containsString("User already exists"))
                .assertThat().body("status",equalTo(409));

    }

    @Test
    public void registrationEmptyEmail(){

        AuthRequestDto auth = AuthRequestDto.builder().username("").password("Elena1234$@").build();

        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.username",containsString("must not be blank"));

    }

    @Test
    public void registrationWrongPassword(){

        AuthRequestDto auth = AuthRequestDto.builder().username("evnikel@gmail.com").password("Ele").build();

        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.password",containsString(" At least 8 characters; Must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number; Can contain special characters [@$#^&*!]"));

    }
}
