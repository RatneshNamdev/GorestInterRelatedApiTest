package com.liseinfotech.gorestApiTest;

import com.github.javafaker.Faker;
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
        Faker faker = new Faker();

        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";

        int userId = 0;
        String body = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
        try {
            Response response = postUser(body);
            UserData userData = response.as(UserData.class);

            userId = userData.getId();
            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));

            assertThat(userData.getId(), notNullValue());
            assertThat(userData.getName(), is(userName));
            assertThat(userData.getEmail(), is(userEmail));
            assertThat(userData.getGender(), is(userGender));
            assertThat(userData.getStatus(), is(userStatus));
        } finally {
            deleteUser(userId);
        }
    }

    @Test(priority = 2)
    public void getUserDetails() throws JSONException {
        Faker faker = new Faker();

        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";

        int userId = 0;
        String body = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
        try {
            Response response = postUser(body);
            UserData userData = response.as(UserData.class);

            userId = userData.getId();
            Response getResponse = getUser(userId);
            UserData getUserData = getResponse.as(UserData.class);

            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
            assertThat(userData.getId(), notNullValue());
            assertThat(userData.getName(), is(userName));
            assertThat(userData.getEmail(), is(userEmail));
            assertThat(userData.getGender(), is(userGender));
            assertThat(userData.getStatus(), is(userStatus));

            assertThat(getUserData.getId(), is(notNullValue()));
            assertThat(getUserData.getName(), is(userName));
            assertThat(getUserData.getEmail(), is(userEmail));
            assertThat(getUserData.getGender(), is(userGender));
            assertThat(getUserData.getStatus(), is(userStatus));
        }finally {
            deleteUser(userId);
        }
    }

    @Test(priority = 3)
    public void updateUserDetails() throws JSONException{
        Faker faker = new Faker();

        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";

        int userId = 0;

        String userNameUpdated = faker.name().fullName();
        String userEmailUpdated = faker.internet().emailAddress();
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

    @Test(priority = 4)
    public void deleteUser() throws JSONException{
        Faker faker = new Faker();

        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";

        int userId = 0;
        String body = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
        try {
            Response response = postUser(body);
            UserData userData = response.as(UserData.class);

            userId = userData.getId();
            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));

            assertThat(userData.getId(), notNullValue());
            assertThat(userData.getName(), is(userName));
            assertThat(userData.getEmail(), is(userEmail));
            assertThat(userData.getGender(), is(userGender));
            assertThat(userData.getStatus(), is(userStatus));
        } finally {
            deleteUser(userId);
        }
    }

    @Test(priority = 5)
    public void invalidPostRequest() throws JSONException{

        Faker faker = new Faker();
        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";

        String body = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";

        Response response = postUser(body);
        response = postUser(body);

        JSONArray jsonArray = new JSONArray(response.asString());
        JSONObject jsonObject = jsonArray.getJSONObject(0);

        assertThat(response.statusCode(), is(HttpStatus.SC_UNPROCESSABLE_ENTITY));
        assertThat(jsonObject.getString("field"),is("email"));
        assertThat(jsonObject.getString("message"), is("has already been taken"));
    }

    @Test(priority = 6)
    public void CreateNewUserUsingPosts() throws JSONException{
        Faker faker = new Faker();

        String userTitle = faker.name().title();
        String userBody = faker.lorem().fixedString(50);
        int userId = 2293;

        String body = "{" + "\"user_id\": \""+userId+"\"," + "\"title\": \""+userTitle+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
        int id = 0;
        try {
            Response response = postsUsers(body);

            System.out.println(response.asString());
            UsersDataPosts userData = response.as(UsersDataPosts.class);
            id = userData.getId();

            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
            assertThat(userData.getId(), is(notNullValue()));
            assertThat(userData.getUser_id(), is(userId));
            assertThat(userData.getTitle(), is(userTitle));
            assertThat(userData.getBody(), is(userBody));
        }finally {
            deleteUserByIdPosts(id);
        }
    }

    @Test(priority = 7)
    public void getUserUsingPosts() throws JSONException{
        Faker faker = new Faker();

        String userTitle = faker.name().title();
        String userBody = faker.lorem().fixedString(50);
        int userId = 2293;

        String body = "{" + "\"user_id\": \""+userId+"\"," + "\"title\": \""+userTitle+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
        int id =0;
        try {
            Response response = postsUsers(body);
            UsersDataPosts userData = response.as(UsersDataPosts.class);
            id = userData.getId();
            Response getResponse = getUserPosts(id);
            System.out.println(getResponse.asString());

            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
            assertThat(userData.getId(), is(notNullValue()));
            assertThat(userData.getUser_id(), is(userId));
            assertThat(userData.getTitle(), is(userTitle));
            assertThat(userData.getBody(), is(userBody));
        }finally {
            deleteUserByIdPosts(id);
        }

    }

    @Test(priority = 8)
    public void updateUserUsingPosts() throws JSONException{
        Faker faker = new Faker();

        String userTitle = faker.name().title();
        String userBody = faker.lorem().fixedString(50);
        String updatedUserBody = faker.lorem().fixedString(60);
        int userId = 2293;

        String body = "{" + "\"user_id\": \""+userId+"\"," + "\"title\": \""+userTitle+"\"," + "\"body\": \""+userBody+"\"\n" + "}";

        String updatedBody = "{" + "\"title\": \""+userTitle+"\"," + "\"body\": \""+updatedUserBody+"\"\n" + "}";
        int id = 0;
        try {
            Response response = postsUsers(body);
            UsersDataPosts userData = response.as(UsersDataPosts.class);
            id = userData.getId();
            Response responseUpdate = updateUserPosts(updatedBody, id);
            UsersDataPosts postsUserData = responseUpdate.as(UsersDataPosts.class);

            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
            assertThat(userData.getId(), is(notNullValue()));
            assertThat(userData.getUser_id(), is(userId));
            assertThat(userData.getTitle(), is(userTitle));
            assertThat(userData.getBody(), is(userBody));

            assertThat(postsUserData.getId(), is(id));
            assertThat(postsUserData.getUser_id(), is(userId));
            assertThat(postsUserData.getTitle(), is(userTitle));
            assertThat(postsUserData.getBody(), is(updatedUserBody));
        }finally {
            deleteUserByIdPosts(id);
        }
    }

    @Test(priority = 9)
    public void deleteUserPosts(){
        Faker faker = new Faker();

        String userTitle = faker.name().title();
        String userBody = faker.lorem().fixedString(50);
        int userId = 2293;

        String body = "{" + "\"user_id\": \""+userId+"\"," + "\"title\": \""+userTitle+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
        int id = 0;
        try {
            Response response = postsUsers(body);
            UsersDataPosts userData = response.as(UsersDataPosts.class);
            id = userData.getId();
        }finally {
            deleteUserByIdPosts(id);
        }
    }

    @Test(priority = 10)
    public void CreateNewUserUsingComments() throws JSONException{
        Faker faker = new Faker();

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();
        int userPostId = 1098;

        String body = "{" + "\"post_id\": \""+userPostId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";

        int userId = 0;
        try {
            Response response = postCommentsUsers(body);
            CommentsPostData userData = response.as(CommentsPostData.class);
            userId = userData.getId();

            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
            assertThat(userData.getPost_id(), is(userPostId));
            assertThat(userData.getName(), is(userName));
            assertThat(userData.getBody(), is(userBody));
            assertThat(userData.getEmail(), is(userEmail));
            assertThat(userData.getId(), notNullValue());
        }finally {
            deleteCommentsUser(userId);
        }
    }

    @Test(priority = 11)
    public void getUserUsingComments(){
        Faker faker = new Faker();

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();
        int userPostId = 1098;

        String body = "{" + "\"post_id\": \""+userPostId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";

        int userId = 0;
        try {
            Response response = postCommentsUsers(body);
            CommentsPostData userData = response.as(CommentsPostData.class);
            userId = userData.getId();
            Response getResponse = getUserComments(userId);
            CommentsPostData getUserData = getResponse.as(CommentsPostData.class);
            System.out.println("getRes : " + getResponse.asString());

            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));

            assertThat(getUserData.getId(), is(userId));
            assertThat(getUserData.getPost_id(), is(userPostId));
            assertThat(getUserData.getName(), is(userName));
            assertThat(getUserData.getEmail(), is(userEmail));
            assertThat(getUserData.getBody(), is(userBody));
        }finally {
            deleteCommentsUser(userId);
        }
    }

    @Test(priority = 12)
    public void updateCommentsUser(){
        Faker faker = new Faker();

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();
        int userPostId = 1098;

        String body = "{" + "\"post_id\": \""+userPostId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
        String updateName = faker.name().fullName();
        String updateEmail = faker.internet().emailAddress();
        String updateBody = "{" + "\"email\":\""+updateEmail+"\"," + "\"name\": \""+updateName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";

        int userId = 0;
        try {
            Response response = postCommentsUsers(body);
            CommentsPostData userData = response.as(CommentsPostData.class);
            userId = userData.getId();

            Response updateResponse = updateCommentsUser(updateBody, userId);
            CommentsPostData updatedUserData = updateResponse.as(CommentsPostData.class);
            System.out.println("upRes : " + updateResponse.asString());

            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
            assertThat(updatedUserData.getId(), is(userId));
            assertThat(updatedUserData.getPost_id(), is(userPostId));
            assertThat(updatedUserData.getEmail(), is(updateEmail));
            assertThat(updatedUserData.getName(), is(updateName));
            assertThat(updatedUserData.getBody(), is(userBody));
        }finally {
            deleteCommentsUser(userId);
        }
    }

    @Test(priority = 13)
    public void deleteCommentsUser(){
        Faker faker = new Faker();

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();
        int userPostId = 1098;

        String body = "{" + "\"post_id\": \""+userPostId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
        int userId = 0;
        try {
            Response response = postCommentsUsers(body);
            CommentsPostData userData = response.as(CommentsPostData.class);
            userId = userData.getId();
        }finally {
            deleteCommentsUser(userId);
        }
    }

    @Test(priority = 14)
    public void todosNewUserCreate(){
        Faker faker = new Faker();

        String userTitle = faker.name().title();
        String userDueOn = "2022-05-03T00:00:00.000+05:30";
        String userStatus = "pending";
        int userId = 3427;

        String body = "{" + "\"user_id\": \""+userId+"\"," + "\"title\":\""+userTitle+"\"," + "\"due_on\": \""+userDueOn+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";

        int id = 0;
        try {
            Response response = todosPostUser(body);
            TodosPostData userData = response.as(TodosPostData.class);
            id = userData.getId();

            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
            assertThat(userData.getId(), is(notNullValue()));
            assertThat(userData.getUser_id(), is(userId));
            assertThat(userData.getTitle(), is(userTitle));
            assertThat(userData.getDue_on(), is(userDueOn));
            assertThat(userData.getStatus(), is(userStatus));
        }finally {
            deleteTodosUser(id);
        }
    }

    @Test(priority = 15)
    public void todosGetUserById(){
        Faker faker = new Faker();

        String userTitle = faker.name().title();
        String userDueOn = "2022-05-03T00:00:00.000+05:30";
        String userStatus = "pending";
        int userId = 3427;

        String body = "{" + "\"user_id\": \""+userId+"\"," + "\"title\":\""+userTitle+"\"," + "\"due_on\": \""+userDueOn+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
        int id = 0;
        try {
            Response response = todosPostUser(body);
            TodosPostData userData = response.as(TodosPostData.class);
            id = userData.getId();

            Response getResponse = todosGetUser(id);
            System.out.println("getRes : " + getResponse.asString());
            TodosPostData getUserData = getResponse.as(TodosPostData.class);
            assertThat(getUserData.getId(),is(notNullValue()));
            assertThat(getUserData.getUser_id(), is(userId));
            assertThat(getUserData.getTitle(), is(userTitle));
            assertThat(getUserData.getDue_on(), is(userDueOn));
            assertThat(getUserData.getStatus(), is(userStatus));
        }finally {
            deleteTodosUser(id);
        }
    }

    @Test(priority = 16)
    public void updateTodosUser(){
        Faker faker = new Faker();

        String userTitle = faker.name().title();
        String userDueOn = "2022-05-03T00:00:00.000+05:30";
        String userStatus = "pending";

        int userId = 3427;
        String body = "{" + "\"user_id\": \""+userId+"\"," + "\"title\":\""+userTitle+"\"," + "\"due_on\": \""+userDueOn+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";

        String updateTitle = faker.name().title();
        String updatedBody = "{" + "\"title\":\""+updateTitle+"\"," + "\"due_on\": \""+userDueOn+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
        int id = 0;
        try {
            Response response = todosPostUser(body);
            TodosPostData userData = response.as(TodosPostData.class);

            id = userData.getId();
            Response updatedResponse = updateTodosUserDetail(updatedBody, id);
            TodosPostData updatedUserData = updatedResponse.as(TodosPostData.class);

            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
            assertThat(updatedUserData.getId(), is(id));
            assertThat(updatedUserData.getUser_id(), is(userId));
            assertThat(updatedUserData.getDue_on(), is(userDueOn));
            assertThat(updatedUserData.getTitle(), is(updateTitle));
            assertThat(updatedUserData.getStatus(), is(userStatus));
        }finally {
            deleteTodosUser(id);
        }
    }

    @Test(priority = 17)
    public void todosDeleteUserById() {
        Faker faker = new Faker();

        String userTitle = faker.name().title();
        String userDueOn = "2022-05-03T00:00:00.000+05:30";
        String userStatus = "pending";
        int userId = 3427;

        String body = "{" + "\"user_id\": \"" + userId + "\"," + "\"title\":\"" + userTitle + "\"," + "\"due_on\": \"" + userDueOn + "\"," + "\"status\": \"" + userStatus + "\"\n" + "}";
        int id = 0;
        try {
            Response response = todosPostUser(body);
            TodosPostData userData = response.as(TodosPostData.class);
            id = userData.getId();
        }finally {
            deleteTodosUser(id);
        }
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

    public Response getUser(int userId){
            Response response = given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .when()
                    .param("access-token", tokenNumber)
                    .request(Method.GET, "/public/v2/users/"+userId)
                    .then()
                    .extract()
                    .response();

            assertThat(response.statusCode(), is(HttpStatus.SC_OK));
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

    public Response postsUsers(String body){
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .body(body)
                .request(Method.POST, "/public/v2/posts/")
                .then()
                .extract()
                .response();

        return response;
    }

    public Response getUserPosts(int id){
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .request(Method.GET, "/public/v2/posts/"+id)
                .then()
                .extract()
                .response();

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        return response;
    }

    public Response updateUserPosts(String updatedBody, int id){
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .body(updatedBody)
                .request(Method.PUT, "/public/v2/posts/"+id)
                .then()
                .extract()
                .response();

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        return response;
    }

    public Response deleteUserByIdPosts(int id){
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .request(Method.DELETE, "/public/v2/posts/"+id)
                .then()
                .extract()
                .response();

        assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));

        return response;
    }

    public Response postCommentsUsers(String body){
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .body(body)
                .request(Method.POST, "/public/v2/comments/")
                .then()
                .extract()
                .response();

        return response;
    }

    public Response getUserComments(int id){
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .request(Method.GET, "/public/v2/comments/"+id)
                .then()
                .extract()
                .response();

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        return response;
    }

    public Response updateCommentsUser(String updateBody, int userId){
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .body(updateBody)
                .request(Method.PUT, "/public/v2/comments/"+userId)
                .then()
                .extract()
                .response();

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        return response;
    }

    public Response deleteCommentsUser(int id){
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .request(Method.DELETE, "/public/v2/comments/"+id)
                .then()
                .extract()
                .response();

        assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));

        return response;
    }

    public Response todosPostUser( String body){
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .body(body)
                .request(Method.POST, "/public/v2/todos/")
                .then()
                .extract()
                .response();

        return response;
    }

    public Response todosGetUser(int id){
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .param("access-token", tokenNumber)
                .request(Method.GET, "/public/v2/todos/"+id)
                .then()
                .extract()
                .response();

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        return response;
    }

    public Response updateTodosUserDetail(String updatedBody, int id){
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .body(updatedBody)
                .request(Method.PUT, "/public/v2/todos/"+id)
                .then()
                .extract()
                .response();

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        return response;
    }

    public Response deleteTodosUser(int id){
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .request(Method.DELETE, "/public/v2/todos/"+id)
                .then()
                .extract()
                .response();

        assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));

        return response;
    }
}
