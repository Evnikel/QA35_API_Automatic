package okhttp3;

import config.Provider;
import dto.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class AddNewContact {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZXZuaWtlbEBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTY2OTY1NDQzMSwiaWF0IjoxNjY5MDU0NDMxfQ.rDCx2hyjhlF6xZuGAGRDlZtZOMG9da10h3wLsiJZauw";

    @Test
    public void addContactSuccess() throws IOException {
        int i = new Random().nextInt(1000)+1000;

        ContactDto contact = ContactDto.builder()
                .name("Anna"+i)
                .lastName("Fox"+i)
                .email("anna"+i+"@ukr.net")
                .phone("0534445"+i)
                .address("Tel Aviv")
                .description("friend")
                .build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(contact),Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization",token)
                .post(body).build();
        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertTrue(response.isSuccessful());
        ContactDtoResponse contactDtoResponse = Provider.getInstance().getGson().fromJson(response.body().string(),ContactDtoResponse.class);

        Assert.assertTrue(contactDtoResponse.getMessage().contains("Contact was added!"));
        // Contact was added! ID: 761491f5-4014-44b5-a2a7-c97278710a04
        String mess = contactDtoResponse.getMessage();
        String [] all =mess.split("ID: ");
        String  id = all[1];
        System.out.println(id);
    }


    @Test
    public void AddNewContactEmptyLastName() throws IOException {
        ContactDto contact = ContactDto.builder().name("Lera").email("lera@gmail.com")
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
        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getStatus(),400);
        Assert.assertEquals(errorDto.getError(),"Bad Request");
        Object mess = errorDto.getMessage();
        System.out.println(mess);
        Assert.assertTrue(mess.toString().contains( "must not be blank"));
    }

    @Test
    public void AddNewContactUnauthorized() throws IOException {
        int i = (int) (System.currentTimeMillis() / 1000) % 3600;
        ContactDto contact = ContactDto.builder().name("Lera"+i).lastName("Lera"+i).email("lera"+i+"@gmail.com")
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

        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getStatus(),401);
        Assert.assertEquals(errorDto.getError(),"Unauthorized");
        Object mess = errorDto.getMessage();
        System.out.println(mess);
        Assert.assertTrue(mess.toString().contains( "JWT strings must contain exactly 2 period characters."));

    }


}
