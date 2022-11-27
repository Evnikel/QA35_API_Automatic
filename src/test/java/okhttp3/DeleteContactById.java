package okhttp3;

import config.Provider;
import dto.ContactDto;
import dto.ContactDtoResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class DeleteContactById {

    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZXZuaWtlbEBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTY2OTY1NDQzMSwiaWF0IjoxNjY5MDU0NDMxfQ.rDCx2hyjhlF6xZuGAGRDlZtZOMG9da10h3wLsiJZauw";


    String  id;
    @BeforeMethod
    public void addNewContact() throws IOException {


        ContactDto contact = ContactDto.builder()
                .name("Anna")
                .lastName("Fox")
                .email("anna@ukr.net")
                .phone("05344451231234")
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
        id = all[1];
        System.out.println(id);
    }

    @Test
    public void deleteContactByIdSuccess() throws IOException {

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/"+id)
                .addHeader("Authorization",token)
                .delete()
                .build();
        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertTrue(response.isSuccessful());
        // Contact was deleted!
        ContactDtoResponse contactDtoResponse = Provider.getInstance().getGson().fromJson(response.body().string(),ContactDtoResponse.class);
        Assert.assertEquals(contactDtoResponse.getMessage(),"Contact was deleted!");
    }
}
