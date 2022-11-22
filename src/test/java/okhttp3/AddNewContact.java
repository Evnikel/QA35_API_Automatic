package okhttp3;

import config.Provider;
import dto.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class AddNewContact {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZXZuaWtlbEBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTY2OTY1NDQzMSwiaWF0IjoxNjY5MDU0NDMxfQ.rDCx2hyjhlF6xZuGAGRDlZtZOMG9da10h3wLsiJZauw";

    @Test
    public void addNewContactSuccess() throws IOException {
        int i = (int) (System.currentTimeMillis() / 1000) % 3600;
        ContactRequestDto contact = ContactRequestDto.builder().name("Lera"+i).lastName("Lera"+i).email("lera"+i+"@gmail.com")
                .phone("123456"+i).address("Israel").description("My best friend").build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(contact),Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization",token)
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);

        ContactResponseDto responseDto = Provider.getInstance().getGson().fromJson(response.body().string(),ContactResponseDto.class);
        System.out.println(responseDto.getMessage());
    }


    @Test
    public void AddNewContactEmptyLastName() throws IOException {
        ContactRequestDto contact = ContactRequestDto.builder().name("Lera").email("lera@gmail.com")
                .phone("123456945783").address("Israel").description("My best friend").build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(contact),Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization",token)
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);
        System.out.println(response.message());

        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(),ErrorDto.class);
        Object message = errorDto.getMessage();
        //Assert.assertEquals(message,"не должно быть пустым");
        Assert.assertEquals(errorDto.getStatus(),400);
    }

    @Test
    public void AddNewContactUnauthorized() throws IOException {
        int i = (int) (System.currentTimeMillis() / 1000) % 3600;
        ContactRequestDto contact = ContactRequestDto.builder().name("Lera"+i).lastName("Lera"+i).email("lera"+i+"@gmail.com")
                .phone("123456"+i).address("Israel").description("My best friend").build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(contact),Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization","gjtull")
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),401);

    }


}
