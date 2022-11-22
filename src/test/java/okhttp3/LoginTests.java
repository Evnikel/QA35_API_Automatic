package okhttp3;

import com.google.gson.Gson;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ErrorDto;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import config.Provider;

public class LoginTests {





    @Test
    public void loginSuccess() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder().username("evnikel@gmail.com").password("Elena1234$@").build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth),Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);

        AuthResponseDto responseDto = Provider.getInstance().getGson().fromJson(response.body().string(),AuthResponseDto.class);
        System.out.println(responseDto.getToken());
    }

    @Test
    public void loginWrongEmail() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder().username("evnikelgmail.com").password("Elena1234$@").build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth),Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),401);

        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(),ErrorDto.class);
        Object message = errorDto.getMessage();
        Assert.assertEquals(message,"Login or Password incorrect");
        Assert.assertEquals(errorDto.getStatus(),401);
    }
    @Test
    public void loginWrongEPassword() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder().username("evnikel@gmail.com").password("Ele").build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth),Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),401);

        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(),ErrorDto.class);
        Object message = errorDto.getMessage();
        Assert.assertEquals(message,"Login or Password incorrect");
        Assert.assertEquals(errorDto.getStatus(),401);
    }
}

//eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZXZuaWtlbEBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTY2OTY1NDQzMSwiaWF0IjoxNjY5MDU0NDMxfQ.rDCx2hyjhlF6xZuGAGRDlZtZOMG9da10h3wLsiJZauw
