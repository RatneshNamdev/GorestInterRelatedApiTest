package com.liseinfotech.gorestApiTest;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class GorestApiTest extends BaseTest{

    @Test(priority = 1)
    public void postCreateNewUser() throws JSONException{

        String userEmail = "AkshayKumar_bollywood@yost-schmeler.net";
        String userName = "Kumar Akshay";
        String userGender = "male";
        String userStatus = "active";

        int userId = 0;

        String userNameUpdated = "Tiger shroff";
        String userEmailUpdated = "shroff_tiger@yost-schmeler.net";
        String bodyForUpdation = "{" + "\"name\": \""+userNameUpdated+"\"," + "\"email\": \""+userEmailUpdated+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";

        String body = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
        try {
            Response response = postUser(body);
            UserData userData = response.as(UserData.class);

            userId = userData.getId();
            Response updatedResponse = updateUserDetail(bodyForUpdation, userId);
            UserData updatedUserData = updatedResponse.as(UserData.class);

            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));

            assertThat(userData.getId(), notNullValue());
            assertThat(userData.getName(), is(userName));
            assertThat(userData.getEmail(), is(userEmail));
            assertThat(userData.getGender(), is(userGender));
            assertThat(userData.getStatus(), is(userStatus));

            assertThat(updatedUserData.getId(), is(userId));
            assertThat(updatedUserData.getEmail(), is(userEmailUpdated));
            assertThat(updatedUserData.getName(), is(userNameUpdated));
            assertThat(updatedUserData.getGender(), is(userGender));
            assertThat(updatedUserData.getStatus(), is(userStatus));
        } finally {
            deleteUser(userId);
        }
    }

    @Test(priority = 2)
    public void getUserDetails() throws JSONException {

        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .param("access-token", tokenNumber)
                .request(Method.GET, "/public/v2/posts/")
                .then()
                .extract()
                .response();

        JSONArray jsonArray = new JSONArray(response.asString());
        JSONObject jsonObject = jsonArray.getJSONObject(0);

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        assertThat(jsonObject.getInt("id"), is(notNullValue()));
        assertThat(jsonObject.getInt("user_id"), is(greaterThan(0)));
        assertThat(jsonObject.getString("title"),notNullValue());
    }

    @Test(priority = 3)
    public void invalidPostRequest() throws JSONException{

        String userEmail = "ratnesh_namdev@yost-schmeler.net";
        String userName = "ratnesh namdev";
        String userGender = "male";
        String userStatus = "active";

        String body = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .body(body)
                .request(Method.POST, "/public/v2/users/")
                .then()
                .extract()
                .response();

        JSONArray jsonArray = new JSONArray(response.asString());
        JSONObject jsonObject = jsonArray.getJSONObject(0);

        assertThat(response.statusCode(), is(HttpStatus.SC_UNPROCESSABLE_ENTITY));
        assertThat(jsonObject.getString("field"),is("email"));
        assertThat(jsonObject.getString("message"), is("has already been taken"));
    }

    public Response postUser( String body){
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .body(body)
                .request(Method.POST, "/public/v2/users/")
                .then()
                .extract()
                .response();

        return response;
    }

    public Response updateUserDetail(String bodyForUpdation, int userId){
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .body(bodyForUpdation)
                .request(Method.PUT, "/public/v2/users/"+userId)
                .then()
                .extract()
                .response();

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        return response;
    }

    public Response deleteUser(int userId){
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .request(Method.DELETE, "/public/v2/users/"+userId)
                .then()
                .extract()
                .response();

        assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));

        return response;
    }
}
