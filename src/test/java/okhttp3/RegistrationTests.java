package okhttp3;

import config.Provider;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ErrorDto;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class RegistrationTests {

    @Test
    public void registrationSuccess() throws IOException {
        int i = (int) (System.currentTimeMillis() / 1000) % 3600;
        AuthRequestDto auth = AuthRequestDto.builder().username("evnikel"+ i +"@gmail.com").password("Elen@"+i).build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth),Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);

        AuthResponseDto responseDto = Provider.getInstance().getGson().fromJson(response.body().string(),AuthResponseDto.class);
        System.out.println(responseDto.getToken());
    }

    @Test
    public void registrationUserAlreadyExists() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder().username("evnikel@gmail.com").password("Elena1234$@").build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth),Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),409);

        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(),ErrorDto.class);
        Object message = errorDto.getMessage();
        Assert.assertEquals(message,"User already exists");
        Assert.assertEquals(errorDto.getStatus(),409);
    }
    @Test
    public void registrationEmptyEmail() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder().username("").password("Elena1234$@").build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth),Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(),ErrorDto.class);
        Object message = errorDto.getMessage();
        //Assert.assertEquals(message,"не должно быть пустым");
        Assert.assertEquals(errorDto.getStatus(),400);
    }

    @Test
    public void registrationWrongPassword() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder().username("evnikel@gmail.com").password("Ele").build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth),Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(),ErrorDto.class);
        Object message = errorDto.getMessage();
        Assert.assertEquals(message," At least 8 characters;" +
                " Must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number;" +
                " Can contain special characters [@$#^&*!]");
        Assert.assertEquals(errorDto.getStatus(),400);
    }
}
