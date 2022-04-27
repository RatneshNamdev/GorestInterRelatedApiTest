package com.liseinfotech.gorestApiTest;

import com.github.javafaker.Faker;
import com.liseinfotech.gorestApiTest.steps.BaseTest;
import com.liseinfotech.gorestModels.CommentsPostData;
import com.liseinfotech.gorestModels.TodosPostData;
import com.liseinfotech.gorestModels.UserData;
import com.liseinfotech.gorestModels.UsersDataPosts;
import com.sun.corba.se.impl.oa.toa.TOA;
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

public class GorestApiTest extends BaseTest {

    @Test(priority = 1)
    public void createUserThroughPost() throws JSONException{
        Faker faker = new Faker();

        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";

        int userId = 0;
        String body = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
        try {
            UserData userData = postUser(body);
            userId = userData.getId();

            assertThat(userData.getId(), notNullValue());
            assertThat(userData.getName(), is(userName));
            assertThat(userData.getEmail(), is(userEmail));
            assertThat(userData.getGender(), is(userGender));
            assertThat(userData.getStatus(), is(userStatus));
        } finally {
            Response response =  deleteUser(userId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 2)
    public void getUserByUserId() throws JSONException {
        Faker faker = new Faker();

        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";

        int userId = 0;
        String body = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
        try {
            UserData userData = postUser(body);
            userId = userData.getId();

            UserData getUserData = getUser(userId);

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
            Response response = deleteUser(userId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 3)
    public void updateUserByUserId() throws JSONException{
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
            UserData userData = postUser(body);
            userId = userData.getId();

            UserData updateUserData = updateUserDetail(bodyForUpdation, userId);

            assertThat(userData.getId(), notNullValue());
            assertThat(userData.getName(), is(userName));
            assertThat(userData.getEmail(), is(userEmail));
            assertThat(userData.getGender(), is(userGender));
            assertThat(userData.getStatus(), is(userStatus));

            assertThat(updateUserData.getId(), is(userId));
            assertThat(updateUserData.getEmail(), is(userEmailUpdated));
            assertThat(updateUserData.getName(), is(userNameUpdated));
            assertThat(updateUserData.getGender(), is(userGender));
            assertThat(updateUserData.getStatus(), is(userStatus));
        } finally {
           Response response = deleteUser(userId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 4)
    public void deleteUserByUserId() throws JSONException{
        Faker faker = new Faker();

        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";

        int userId = 0;
        String body = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
        try {
            UserData userData = postUser(body);
            userId = userData.getId();

            assertThat(userData.getId(), notNullValue());
            assertThat(userData.getName(), is(userName));
            assertThat(userData.getEmail(), is(userEmail));
            assertThat(userData.getGender(), is(userGender));
            assertThat(userData.getStatus(), is(userStatus));
        } finally {
            Response response = deleteUser(userId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 17)
    public void invalidRequestThroughUsers() throws JSONException{

        Faker faker = new Faker();
        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";

        String body = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";

        Response userData = invalidPostUser(body);
        userData = invalidPostUser(body);

        JSONArray jsonArray = new JSONArray(userData.asString());
        JSONObject jsonObject = jsonArray.getJSONObject(0);

        assertThat(userData.statusCode(), is(HttpStatus.SC_UNPROCESSABLE_ENTITY));
        assertThat(jsonObject.getString("field"),is("email"));
        assertThat(jsonObject.getString("message"), is("has already been taken"));
    }

    @Test(priority = 19)
    public void getInvalidUserId() throws JSONException {
        Faker faker = new Faker();

        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";

        int userId = 0000;
        String body = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";

        UserData userData = postUser(body);

        Response getUserData = getInvalidUser(userId);
        JSONObject jsonObject = new JSONObject(getUserData.asString());

        assertThat(userData.getId(), notNullValue());
        assertThat(userData.getName(), is(userName));
        assertThat(userData.getEmail(), is(userEmail));
        assertThat(userData.getGender(), is(userGender));
        assertThat(userData.getStatus(), is(userStatus));

        assertThat(jsonObject.getString("message"), containsString("Resource not found"));
    }

    @Test(priority = 18)
    public void missingFieldThroughPost() throws JSONException{

        Faker faker = new Faker();
        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";

        String body = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"\n" + "}";
        Response userData = invalidPostUser(body);

        JSONArray jsonArray = new JSONArray(userData.asString());
        JSONObject jsonObject = jsonArray.getJSONObject(0);

        assertThat(userData.statusCode(), is(HttpStatus.SC_UNPROCESSABLE_ENTITY));
        assertThat(jsonObject.getString("field"),is("status"));
        assertThat(jsonObject.getString("message"), is("can't be blank"));
    }

    @Test(priority = 20)
    public void updateUserByInvalidUserId() throws JSONException{
        Faker faker = new Faker();

        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";

        int userId = 0000;

        String userNameUpdated = faker.name().fullName();
        String userEmailUpdated = faker.internet().emailAddress();
        String bodyForUpdation = "{" + "\"name\": \""+userNameUpdated+"\"," + "\"email\": \""+userEmailUpdated+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";

        String body = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";

        UserData userData = postUser(body);

        Response updateUserData = updateInvalidUserDetail(bodyForUpdation, userId);
        JSONObject jsonObject = new JSONObject(updateUserData.asString());

//        assertThat(userData.getStatus(), is(HttpStatus.SC_CREATED));
        assertThat(userData.getId(), notNullValue());
        assertThat(userData.getName(), is(userName));
        assertThat(userData.getEmail(), is(userEmail));
        assertThat(userData.getGender(), is(userGender));
        assertThat(userData.getStatus(), is(userStatus));

        assertThat(updateUserData.statusCode(), is(HttpStatus.SC_NOT_FOUND));
        assertThat(jsonObject.getString("message"), is("Resource not found"));
    }

    @Test(priority = 21)
    public void deleteUserByInvalidUserId() throws JSONException{
        Faker faker = new Faker();

        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";

        int userId = 0000;
        String body = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
        try {
            UserData userData = postUser(body);

//            assertThat(userData.getStatus(), is(HttpStatus.SC_CREATED));
            assertThat(userData.getId(), notNullValue());
            assertThat(userData.getName(), is(userName));
            assertThat(userData.getEmail(), is(userEmail));
            assertThat(userData.getGender(), is(userGender));
            assertThat(userData.getStatus(), is(userStatus));
        } finally {
             Response deleteResponse = deleteUser(userId);
             assertThat(deleteResponse.statusCode(), is(HttpStatus.SC_NOT_FOUND));
        }
    }

    @Test(priority = 5)
    public void postsNewUser() throws JSONException{
        int id = 0;
        int userId = 0;
        Faker faker = new Faker();
        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";
        String userTitle = faker.name().title();
        String userBody = faker.lorem().fixedString(50);

        try {
            String postBody = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            UserData userData = postUser(postBody);
            userId = userData.getId();

            String body = "{" + "\"user_id\": \""+userId+"\"," + "\"title\": \""+userTitle+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(body);
            id = postsUserData.getId();

//            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
            assertThat(userData.getId(), is(notNullValue()));
            assertThat(postsUserData.getUser_id(), is(userId));
            assertThat(postsUserData.getTitle(), is(userTitle));
            assertThat(postsUserData.getBody(), is(userBody));
        }finally {
            Response response = deleteUserByIdPosts(id);
            assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));
            Response deleteUsers = deleteUser(userId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 6)
    public void getPostsById() throws JSONException{
        int userId = 0;
        int id =0;

        Faker faker = new Faker();
        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";
        String userTitle = faker.name().title();
        String userBody = faker.lorem().fixedString(50);
        try {
            String postBody = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            UserData userData = postUser(postBody);
            userId = userData.getId();

            String body = "{" + "\"user_id\": \""+userId+"\"," + "\"title\": \""+userTitle+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(body);
            id = postsUserData.getId();
            UsersDataPosts postsGetUserData = getUserPosts(id);

//            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
//            assertThat(getResponse.statusCode(), is(HttpStatus.SC_OK));

            assertThat(userData.getId(), is(notNullValue()));
            assertThat(postsGetUserData.getUser_id(), is(userId));
            assertThat(postsGetUserData.getTitle(), is(userTitle));
            assertThat(postsGetUserData.getBody(), is(userBody));
        }finally {
            Response response = deleteUserByIdPosts(id);
            assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));
            Response deleteUsers = deleteUser(userId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 7)
    public void updatePostsById() throws JSONException{
        int id = 0;
        int userId = 0;

        Faker faker = new Faker();
        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";

        String userTitle = faker.name().title();
        String userBody = faker.lorem().fixedString(50);
        String updatedUserBody = faker.lorem().fixedString(60);
        try {
            String postBody = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            UserData userData = postUser(postBody);
            userId = userData.getId();

            String body = "{" + "\"user_id\": \""+userId+"\"," + "\"title\": \""+userTitle+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            String updatedBody = "{" + "\"title\": \""+userTitle+"\"," + "\"body\": \""+updatedUserBody+"\"\n" + "}";

            UsersDataPosts postsUserData = postsUsers(body);
            id = postsUserData.getId();

            UsersDataPosts postsUserUpdate = updateUserPosts(updatedBody, id);

//            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
            assertThat(userData.getId(), is(notNullValue()));
            assertThat(postsUserData.getUser_id(), is(userId));
            assertThat(postsUserData.getTitle(), is(userTitle));
            assertThat(postsUserData.getBody(), is(userBody));

            assertThat(postsUserUpdate.getId(), is(id));
            assertThat(postsUserUpdate.getUser_id(), is(userId));
            assertThat(postsUserUpdate.getTitle(), is(userTitle));
            assertThat(postsUserUpdate.getBody(), is(updatedUserBody));
        }finally {
            Response response = deleteUserByIdPosts(id);
            assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));
            Response deleteUsers = deleteUser(userId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 8)
    public void deletePostsById(){
        int userId = 0;
        int id = 0;

        Faker faker = new Faker();
        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";

        String userTitle = faker.name().title();
        String userBody = faker.lorem().fixedString(50);
        try {
            String postBody = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            UserData userData = postUser(postBody);
            userId = userData.getId();

            String body = "{" + "\"user_id\": \""+userId+"\"," + "\"title\": \""+userTitle+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(body);
            id = postsUserData.getId();
        }finally {
            Response response =deleteUserByIdPosts(id);
            assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));
            Response deleteUsers = deleteUser(userId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 22)
    public void postsUserByInvalidUserId() throws JSONException{
        Faker faker = new Faker();
        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";
        String userTitle = faker.name().title();
        String userBody = faker.lorem().fixedString(50);
        int userId = 0000;
        try {
            String postBody = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            UserData userData = postUser(postBody);

            String body = "{" + "\"user_id\": \""+userId+"\"," + "\"title\": \""+userTitle+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            Response postsUserData = invalidPostsUsers(body);
            JSONArray jsonArray = new JSONArray(postsUserData.asString());
            JSONObject jsonObject = jsonArray.getJSONObject(0);

//            assertThat(userData.getStatus(), is(HttpStatus.SC_CREATED));

            assertThat(jsonObject.getString("field"), containsString("user"));
            assertThat(jsonObject.getString("message"), containsString("must exist"));
        }finally {
            Response deleteUsers = deleteUser(userId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NOT_FOUND));
        }
    }

    @Test(priority = 23)
    public void getPostsByInvalidId() throws JSONException{
        int userId = 0;
        int id =0;

        Faker faker = new Faker();
        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";
        String userTitle = faker.name().title();
        String userBody = faker.lorem().fixedString(50);
        try {
            String postBody = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            UserData userData = postUser(postBody);
            userId = userData.getId();

            String body = "{" + "\"user_id\": \""+userId+"\"," + "\"title\": \""+userTitle+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(body);

            Response getResponse = getInvliadUserPosts(id);

//            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
            assertThat(userData.getId(), is(notNullValue()));
            assertThat(postsUserData.getUser_id(), is(userId));
            assertThat(postsUserData.getTitle(), is(userTitle));
            assertThat(postsUserData.getBody(), is(userBody));
            assertThat(getResponse.statusCode(), is(HttpStatus.SC_NOT_FOUND));
        }finally {
            Response response = deleteUserByIdPosts(id);
            assertThat(response.statusCode(), is(HttpStatus.SC_NOT_FOUND));
            Response deleteUsers = deleteUser(userId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 24)
    public void updatePostsByInvalidId() throws JSONException{
        int id = 0;
        int userId = 0;

        Faker faker = new Faker();
        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";

        String userTitle = faker.name().title();
        String userBody = faker.lorem().fixedString(50);
        String updatedUserBody = faker.lorem().fixedString(60);
        try {
            String postBody = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            UserData userData = postUser(postBody);
            userId = userData.getId();

            String body = "{" + "\"user_id\": \""+userId+"\"," + "\"title\": \""+userTitle+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            String updatedBody = "{" + "\"title\": \""+userTitle+"\"," + "\"body\": \""+updatedUserBody+"\"\n" + "}";

            UsersDataPosts postsUserData = postsUsers(body);

            Response postsUserUpdate = updateInvalidUserPosts(updatedBody, id);
            System.out.println("res : " + postsUserUpdate.asString());
            JSONObject jsonObject = new JSONObject(postsUserUpdate.asString());

//            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
            assertThat(userData.getId(), is(notNullValue()));
            assertThat(postsUserData.getUser_id(), is(userId));
            assertThat(postsUserData.getTitle(), is(userTitle));
            assertThat(postsUserData.getBody(), is(userBody));

            assertThat(postsUserUpdate.statusCode(), is(HttpStatus.SC_NOT_FOUND));
            assertThat(jsonObject.getString("message"), is("Resource not found"));
        }finally {
            Response response = deleteUserByIdPosts(id);
            assertThat(response.statusCode(), is(HttpStatus.SC_NOT_FOUND));
            Response deleteUsers = deleteUser(userId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 25)
    public void deletePostsByInvalidId(){
        int usersId = 0;
        int id = 0;

        Faker faker = new Faker();
        String userEmail = faker.internet().emailAddress();
        String userName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";

        String userTitle = faker.name().title();
        String userBody = faker.lorem().fixedString(50);
        try {
            String postBody = "{" + "\"name\": \""+userName+"\"," + "\"email\": \""+userEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            UserData userData = postUser(postBody);
            usersId = userData.getId();

            String body = "{" + "\"user_id\": \""+usersId+"\"," + "\"title\": \""+userTitle+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(body);
        }finally {
            Response response =deleteUserByIdPosts(id);
            assertThat(response.statusCode(), is(HttpStatus.SC_NOT_FOUND));
            Response deleteUsers = deleteUser(usersId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 9)
    public void commentsOnNewUser() throws JSONException{
        int postUserId = 0;
        int usersId = 0;
        int commentsId = 0;

        Faker faker = new Faker();
        String usersEmail = faker.internet().emailAddress();
        String usersName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";
        String postUserTitle = faker.name().title();
        String postUserBody = faker.lorem().fixedString(50);

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();
        try {
            String usersBody = "{" + "\"name\": \""+usersName+"\"," + "\"email\": \""+usersEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            UserData userData = postUser(usersBody);
            usersId = userData.getId();

            String postBody = "{" + "\"user_id\": \""+usersId+"\"," + "\"title\": \""+postUserTitle+"\"," + "\"body\": \""+postUserBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(postBody);
            postUserId = postsUserData.getId();

            String body = "{" + "\"post_id\": \""+postUserId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            CommentsPostData commentsUserData = postCommentsUsers(body);
            commentsId = commentsUserData.getId();

//            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
            assertThat(commentsUserData.getPost_id(), is(postUserId));
            assertThat(commentsUserData.getName(), is(userName));
            assertThat(commentsUserData.getBody(), is(userBody));
            assertThat(commentsUserData.getEmail(), is(userEmail));
            assertThat(commentsUserData.getId(), is(commentsId));
        }finally {
            Response response = deleteCommentsUser(commentsId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deletePosts = deleteUserByIdPosts(postUserId);
            assertThat(deletePosts.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deleteUsers = deleteUser(usersId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 26)
    public void invalidCommentsOnUser() throws JSONException{
        int postUserId = 0;
        int usersId = 0;
        int commentsId = 0;

        Faker faker = new Faker();
        String usersEmail = faker.internet().emailAddress();
        String usersName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";
        String postUserTitle = faker.name().title();
        String postUserBody = faker.lorem().fixedString(50);

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();
        try {
            String usersBody = "{" + "\"name\": \""+usersName+"\"," + "\"email\": \""+usersEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            UserData userData = postUser(usersBody);
            usersId = userData.getId();

            String postBody = "{" + "\"user_id\": \""+usersId+"\"," + "\"title\": \""+postUserTitle+"\"," + "\"body\": \""+postUserBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(postBody);

            String body = "{" + "\"post_id\": \""+postUserId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            Response commentsUserData = invalidPostCommentsUsers(body);
            JSONArray jsonArray = new JSONArray(commentsUserData.asString());
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            assertThat(commentsUserData.statusCode(), is(HttpStatus.SC_UNPROCESSABLE_ENTITY));
            assertThat(jsonObject.getString("field"), containsString("post"));
            assertThat(jsonObject.getString("message"), containsString("must exist"));
        }finally {
            Response response = deleteCommentsUser(commentsId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NOT_FOUND));

            Response deletePosts = deleteUserByIdPosts(postUserId);
            assertThat(deletePosts.statusCode(), is(HttpStatus.SC_NOT_FOUND));

            Response deleteUsers = deleteUser(usersId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 10)
    public void getCommentsByUserId(){
        int postUserId = 0;
        int usersId = 0;
        int commentsId = 0;

        Faker faker = new Faker();
        String usersEmail = faker.internet().emailAddress();
        String usersName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";
        String postUserTitle = faker.name().title();
        String postUserBody = faker.lorem().fixedString(50);

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();
        try {
            String usersBody = "{" + "\"name\": \""+usersName+"\"," + "\"email\": \""+usersEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            UserData userData = postUser(usersBody);
            usersId = userData.getId();

            String postBody = "{" + "\"user_id\": \""+usersId+"\"," + "\"title\": \""+postUserTitle+"\"," + "\"body\": \""+postUserBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(postBody);
            postUserId = postsUserData.getId();

            String body = "{" + "\"post_id\": \""+postUserId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            CommentsPostData commentsUserData = postCommentsUsers(body);
            commentsId = commentsUserData.getId();

            CommentsPostData getCommentsUserData = getUserComments(commentsId);

//            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));

            assertThat(getCommentsUserData.getId(), is(commentsId));
            assertThat(getCommentsUserData.getPost_id(), is(postUserId));
            assertThat(getCommentsUserData.getName(), is(userName));
            assertThat(getCommentsUserData.getEmail(), is(userEmail));
            assertThat(getCommentsUserData.getBody(), is(userBody));

//            assertThat(getResponse.statusCode(), is(HttpStatus.SC_OK));
        }finally {
            Response response = deleteCommentsUser(commentsId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deletePosts = deleteUserByIdPosts(postUserId);
            assertThat(deletePosts.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deleteUsers = deleteUser(usersId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 27)
    public void getCommentsByInvalidUserId(){
        int postUserId = 0;
        int usersId = 0;
        int commentsId = 0;

        Faker faker = new Faker();
        String usersEmail = faker.internet().emailAddress();
        String usersName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";
        String postUserTitle = faker.name().title();
        String postUserBody = faker.lorem().fixedString(50);

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();
        try {
            String usersBody = "{" + "\"name\": \""+usersName+"\"," + "\"email\": \""+usersEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            UserData userData = postUser(usersBody);
            usersId = userData.getId();

            String postBody = "{" + "\"user_id\": \""+usersId+"\"," + "\"title\": \""+postUserTitle+"\"," + "\"body\": \""+postUserBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(postBody);
            postUserId = postsUserData.getId();

            String body = "{" + "\"post_id\": \""+postUserId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            CommentsPostData commentsUserData = postCommentsUsers(body);

            Response getResponse = getInvalidUserComments(commentsId);
            JSONObject jsonObject = new JSONObject(getResponse.asString());

            assertThat(getResponse.statusCode(), is(HttpStatus.SC_NOT_FOUND));
            assertThat(jsonObject.getString("message"), containsString("Resource not found"));
        }finally {
            Response response = deleteCommentsUser(commentsId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NOT_FOUND));

            Response deletePosts = deleteUserByIdPosts(postUserId);
            assertThat(deletePosts.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deleteUsers = deleteUser(usersId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 11)
    public void updateCommentsByUserId(){
        int postUserId = 0;
        int usersId = 0;
        int commentsId = 0;

        Faker faker = new Faker();
        String usersEmail = faker.internet().emailAddress();
        String usersName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";
        String postUserTitle = faker.name().title();
        String postUserBody = faker.lorem().fixedString(50);

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();

        String updateName = faker.name().fullName();
        String updateEmail = faker.internet().emailAddress();
        try {
            String usersBody = "{" + "\"name\": \""+usersName+"\"," + "\"email\": \""+usersEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            UserData userData = postUser(usersBody);
            usersId = userData.getId();

            String postBody = "{" + "\"user_id\": \""+usersId+"\"," + "\"title\": \""+postUserTitle+"\"," + "\"body\": \""+postUserBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(postBody);
            System.out.println("post-res : " + postsUserData.toString());
            postUserId = postsUserData.getId();

            String body = "{" + "\"post_id\": \""+postUserId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            String updateBody = "{" + "\"email\":\""+updateEmail+"\"," + "\"name\": \""+updateName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";

            CommentsPostData commentsUserData = postCommentsUsers(body);
            commentsId = commentsUserData.getId();

            CommentsPostData commentsUpdateUserData = updateCommentsUser(updateBody, commentsId);
            System.out.println("upRes : " + commentsUpdateUserData.toString());

//            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
//            assertThat(updateResponse.statusCode(), is(HttpStatus.SC_OK));

            assertThat(commentsUpdateUserData.getId(), is(commentsId));
            assertThat(commentsUpdateUserData.getPost_id(), is(postUserId));
            assertThat(commentsUpdateUserData.getEmail(), is(updateEmail));
            assertThat(commentsUpdateUserData.getName(), is(updateName));
            assertThat(commentsUpdateUserData.getBody(), is(userBody));
        }finally {
            Response response = deleteCommentsUser(commentsId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deletePosts = deleteUserByIdPosts(postUserId);
            assertThat(deletePosts.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deleteUsers = deleteUser(usersId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 28)
    public void updateCommentsByInvalidUserId(){
        int postUserId = 0;
        int usersId = 0;
        int commentsId = 0;

        Faker faker = new Faker();
        String usersEmail = faker.internet().emailAddress();
        String usersName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";
        String postUserTitle = faker.name().title();
        String postUserBody = faker.lorem().fixedString(50);

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();

        String updateName = faker.name().fullName();
        String updateEmail = faker.internet().emailAddress();
        try {
            String usersBody = "{" + "\"name\": \""+usersName+"\"," + "\"email\": \""+usersEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            UserData userData = postUser(usersBody);
            System.out.println("users-res : " + userData.toString());
            usersId = userData.getId();

            String postBody = "{" + "\"user_id\": \""+usersId+"\"," + "\"title\": \""+postUserTitle+"\"," + "\"body\": \""+postUserBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(postBody);
            System.out.println("post-res : " + postsUserData.toString());
            postUserId = postsUserData.getId();

            String body = "{" + "\"post_id\": \""+postUserId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            String updateBody = "{" + "\"email\":\""+updateEmail+"\"," + "\"name\": \""+updateName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";

            CommentsPostData commentsUserData = postCommentsUsers(body);

            Response updateResponse = updateInvalidCommentsUser(updateBody, commentsId);
            JSONObject jsonObject = new JSONObject(updateResponse.asString());

//            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
            assertThat(updateResponse.statusCode(), is(HttpStatus.SC_NOT_FOUND));

            assertThat(jsonObject.getString("message"), containsString("Resource not found"));
        }finally {
            Response response = deleteCommentsUser(commentsId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NOT_FOUND));

            Response deletePosts = deleteUserByIdPosts(postUserId);
            assertThat(deletePosts.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deleteUsers = deleteUser(usersId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 12)
    public void deleteCommentsByUserId(){
        int postUserId = 0;
        int usersId = 0;
        int commentsId = 0;

        Faker faker = new Faker();
        String usersEmail = faker.internet().emailAddress();
        String usersName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";
        String postUserTitle = faker.name().title();
        String postUserBody = faker.lorem().fixedString(50);

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();
        try {
            String usersBody = "{" + "\"name\": \""+usersName+"\"," + "\"email\": \""+usersEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            UserData userData = postUser(usersBody);
            System.out.println("users-res : " + userData.toString());
            usersId = userData.getId();

            String postBody = "{" + "\"user_id\": \""+usersId+"\"," + "\"title\": \""+postUserTitle+"\"," + "\"body\": \""+postUserBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(postBody);
            System.out.println("post-res : " + postsUserData.toString());
            postUserId = postsUserData.getId();

            String body = "{" + "\"post_id\": \""+postUserId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            CommentsPostData commentsUserData = postCommentsUsers(body);
            commentsId = commentsUserData.getId();
        }finally {
            Response response = deleteCommentsUser(commentsId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deletePosts = deleteUserByIdPosts(postUserId);
            assertThat(deletePosts.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deleteUsers = deleteUser(usersId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 29)
    public void deleteCommentsByInvalidUserId(){
        int postUserId = 0;
        int usersId = 0;
        int commentsId = 0;

        Faker faker = new Faker();
        String usersEmail = faker.internet().emailAddress();
        String usersName = faker.name().fullName();
        String userGender = "male";
        String userStatus = "active";
        String postUserTitle = faker.name().title();
        String postUserBody = faker.lorem().fixedString(50);

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();
        try {
            String usersBody = "{" + "\"name\": \""+usersName+"\"," + "\"email\": \""+usersEmail+"\"," + "\"gender\": \""+userGender+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            UserData userData = postUser(usersBody);
            System.out.println("users-res : " + userData.toString());
            usersId = userData.getId();

            String postBody = "{" + "\"user_id\": \""+usersId+"\"," + "\"title\": \""+postUserTitle+"\"," + "\"body\": \""+postUserBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(postBody);
            System.out.println("post-res : " + postsUserData.toString());
            postUserId = postsUserData.getId();

            String body = "{" + "\"post_id\": \""+postUserId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            CommentsPostData commentsUserData = postCommentsUsers(body);
            System.out.println("res : " + commentsUserData.toString());
        }finally {
            Response response = deleteCommentsUser(commentsId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NOT_FOUND));

            Response deletePosts = deleteUserByIdPosts(postUserId);
            assertThat(deletePosts.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deleteUsers = deleteUser(usersId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 13)
    public void todosNewUser(){
        int postUserId = 0;
        int usersId = 0;
        int commentsId = 0;
        int todosUserId = 0;

        Faker faker = new Faker();
        String usersEmail = faker.internet().emailAddress();
        String usersName = faker.name().fullName();
        String usersGender = "male";
        String usersStatus = "active";

        String postUserTitle = faker.name().title();
        String postUserBody = faker.lorem().fixedString(50);

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();

        String userTitle = faker.name().title();
        String userDueOn = "2022-05-03T00:00:00.000+05:30";
        String userStatus = "pending";
        try {
            String usersBody = "{" + "\"name\": \""+usersName+"\"," + "\"email\": \""+usersEmail+"\"," + "\"gender\": \""+usersGender+"\"," + "\"status\": \""+usersStatus+"\"\n" + "}";
            UserData userData = postUser(usersBody);
            System.out.println("users-res : " + userData.toString());
            usersId = userData.getId();

            String postBody = "{" + "\"user_id\": \""+usersId+"\"," + "\"title\": \""+postUserTitle+"\"," + "\"body\": \""+postUserBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(postBody);
            System.out.println("post-res : " + postsUserData.toString());
            postUserId = postsUserData.getId();

            String commentsBody = "{" + "\"post_id\": \""+postUserId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            CommentsPostData commentsUserData = postCommentsUsers(commentsBody);
            System.out.println("comm-res : " + commentsUserData.toString());
            commentsId = commentsUserData.getId();

            String body = "{" + "\"user_id\": \""+commentsId+"\"," + "\"title\":\""+userTitle+"\"," + "\"due_on\": \""+userDueOn+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            TodosPostData todosUserData = todosPostUser(body);
            System.out.println("todos-res : " + todosUserData.toString());
            todosUserId = todosUserData.getId();

//            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
            assertThat(todosUserData.getId(), is(todosUserId));
            assertThat(todosUserData.getUser_id(), is(commentsId));
            assertThat(todosUserData.getTitle(), is(userTitle));
            assertThat(todosUserData.getDue_on(), is(userDueOn));
            assertThat(todosUserData.getStatus(), is(userStatus));
        }finally {
            Response response = deleteTodosUser(todosUserId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deleteComments = deleteCommentsUser(commentsId);
            assertThat(deleteComments.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deletePosts = deleteUserByIdPosts(postUserId);
            assertThat(deletePosts.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deleteUsers = deleteUser(usersId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 30)
    public void invalidTodosPost(){
        int postUserId = 0;
        int usersId = 0;
        int commentsId = 0;
        int todosUserId = 0;

        Faker faker = new Faker();
        String usersEmail = faker.internet().emailAddress();
        String usersName = faker.name().fullName();
        String usersGender = "male";
        String usersStatus = "active";

        String postUserTitle = faker.name().title();
        String postUserBody = faker.lorem().fixedString(50);

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();

        String userTitle = faker.name().title();
        String userDueOn = "2022-05-03T00:00:00.000+05:30";
        String userStatus = "pending";
        try {
            String usersBody = "{" + "\"name\": \""+usersName+"\"," + "\"email\": \""+usersEmail+"\"," + "\"gender\": \""+usersGender+"\"," + "\"status\": \""+usersStatus+"\"\n" + "}";
            UserData userData = postUser(usersBody);
            System.out.println("users-res : " + userData.toString());
            usersId = userData.getId();

            String postBody = "{" + "\"user_id\": \""+usersId+"\"," + "\"title\": \""+postUserTitle+"\"," + "\"body\": \""+postUserBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(postBody);
            System.out.println("post-res : " + postsUserData.toString());
            postUserId = postsUserData.getId();

            String commentsBody = "{" + "\"post_id\": \""+postUserId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            CommentsPostData commentsUserData = postCommentsUsers(commentsBody);
            System.out.println("comm-res : " + commentsUserData.toString());

            String body = "{" + "\"user_id\": \""+commentsId+"\"," + "\"title\":\""+userTitle+"\"," + "\"due_on\": \""+userDueOn+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            Response todosUserData = invalidTodosPostUser(body);
            System.out.println("todos-res : " + todosUserData.asString());
            JSONArray jsonArray = new JSONArray(todosUserData.asString());
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            assertThat(todosUserData.statusCode(), is(HttpStatus.SC_UNPROCESSABLE_ENTITY));
            assertThat(jsonObject.getString("field"), containsString("user"));
            assertThat(jsonObject.getString("message"), containsString("must exist"));
        }finally {
            Response response = deleteTodosUser(todosUserId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NOT_FOUND));

            Response deleteComments = deleteCommentsUser(commentsId);
            assertThat(deleteComments.statusCode(), is(HttpStatus.SC_NOT_FOUND));

            Response deletePosts = deleteUserByIdPosts(postUserId);
            assertThat(deletePosts.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deleteUsers = deleteUser(usersId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 14)
    public void getTodosUserById(){
        int postUserId = 0;
        int usersId = 0;
        int commentsId = 0;
        int todosUserId = 0;

        Faker faker = new Faker();
        String usersEmail = faker.internet().emailAddress();
        String usersName = faker.name().fullName();
        String usersGender = "male";
        String usersStatus = "active";

        String postUserTitle = faker.name().title();
        String postUserBody = faker.lorem().fixedString(50);

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();

        String userTitle = faker.name().title();
        String userDueOn = "2022-05-03T00:00:00.000+05:30";
        String userStatus = "pending";
        try {
            String usersBody = "{" + "\"name\": \""+usersName+"\"," + "\"email\": \""+usersEmail+"\"," + "\"gender\": \""+usersGender+"\"," + "\"status\": \""+usersStatus+"\"\n" + "}";
            UserData userData = postUser(usersBody);
            System.out.println("users-res : " + userData.toString());
            usersId = userData.getId();

            String postBody = "{" + "\"user_id\": \""+usersId+"\"," + "\"title\": \""+postUserTitle+"\"," + "\"body\": \""+postUserBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(postBody);
            System.out.println("post-res : " + postsUserData.toString());
            postUserId = postsUserData.getId();

            String commentsBody = "{" + "\"post_id\": \""+postUserId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            CommentsPostData commentsUserData = postCommentsUsers(commentsBody);
            System.out.println("comm-res : " + commentsUserData.toString());
            commentsId = commentsUserData.getId();


            String body = "{" + "\"user_id\": \""+commentsId+"\"," + "\"title\":\""+userTitle+"\"," + "\"due_on\": \""+userDueOn+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            TodosPostData todosUserData = todosPostUser(body);
            todosUserId = todosUserData.getId();
            TodosPostData todosGetUserData = todosGetUser(todosUserId);
            System.out.println("getRes : " + todosGetUserData.toString());

//            assertThat(getResponse.statusCode(), is(HttpStatus.SC_OK));
            assertThat(todosGetUserData.getId(),is(todosUserId));
            assertThat(todosGetUserData.getUser_id(), is(commentsId));
            assertThat(todosGetUserData.getTitle(), is(userTitle));
            assertThat(todosGetUserData.getDue_on(), is(userDueOn));
            assertThat(todosGetUserData.getStatus(), is(userStatus));
        }finally {
            Response response = deleteTodosUser(todosUserId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deleteComments = deleteCommentsUser(commentsId);
            assertThat(deleteComments.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deletePosts = deleteUserByIdPosts(postUserId);
            assertThat(deletePosts.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deleteUsers = deleteUser(usersId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 31)
    public void getTodosUserByInvalidId(){
        int postUserId = 0;
        int usersId = 0;
        int commentsId = 0;
        int todosUserId = 0;

        Faker faker = new Faker();
        String usersEmail = faker.internet().emailAddress();
        String usersName = faker.name().fullName();
        String usersGender = "male";
        String usersStatus = "active";

        String postUserTitle = faker.name().title();
        String postUserBody = faker.lorem().fixedString(50);

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();

        String userTitle = faker.name().title();
        String userDueOn = "2022-05-03T00:00:00.000+05:30";
        String userStatus = "pending";
        try {
            String usersBody = "{" + "\"name\": \""+usersName+"\"," + "\"email\": \""+usersEmail+"\"," + "\"gender\": \""+usersGender+"\"," + "\"status\": \""+usersStatus+"\"\n" + "}";
            UserData userData = postUser(usersBody);
            System.out.println("users-res : " + userData.toString());
            usersId = userData.getId();

            String postBody = "{" + "\"user_id\": \""+usersId+"\"," + "\"title\": \""+postUserTitle+"\"," + "\"body\": \""+postUserBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(postBody);
            System.out.println("post-res : " + postsUserData.toString());
            postUserId = postsUserData.getId();

            String commentsBody = "{" + "\"post_id\": \""+postUserId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            CommentsPostData commentsUserData = postCommentsUsers(commentsBody);
            System.out.println("comm-res : " + commentsUserData.toString());
            commentsId = commentsUserData.getId();


            String body = "{" + "\"user_id\": \""+commentsId+"\"," + "\"title\":\""+userTitle+"\"," + "\"due_on\": \""+userDueOn+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            TodosPostData todosUserData = todosPostUser(body);

            Response todosGetUserData = invalidTodosGetUser(todosUserId);
            System.out.println("getRes : " + todosGetUserData.asString());
            JSONObject jsonObject = new JSONObject(todosGetUserData.asString());

            assertThat(todosGetUserData.statusCode(), is(HttpStatus.SC_NOT_FOUND));
            assertThat(jsonObject.getString("message"), containsString("Resource not found"));

        }finally {
            Response response = deleteTodosUser(todosUserId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NOT_FOUND));

            Response deleteComments = deleteCommentsUser(commentsId);
            assertThat(deleteComments.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deletePosts = deleteUserByIdPosts(postUserId);
            assertThat(deletePosts.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deleteUsers = deleteUser(usersId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 15)
    public void updateTodosUserById(){
        int postUserId = 0;
        int usersId = 0;
        int commentsId = 0;
        int todosUserId = 0;

        Faker faker = new Faker();
        String usersEmail = faker.internet().emailAddress();
        String usersName = faker.name().fullName();
        String usersGender = "male";
        String usersStatus = "active";

        String postUserTitle = faker.name().title();
        String postUserBody = faker.lorem().fixedString(50);

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();

        String userTitle = faker.name().title();
        String userDueOn = "2022-05-03T00:00:00.000+05:30";
        String userStatus = "pending";
        try {
            String usersBody = "{" + "\"name\": \""+usersName+"\"," + "\"email\": \""+usersEmail+"\"," + "\"gender\": \""+usersGender+"\"," + "\"status\": \""+usersStatus+"\"\n" + "}";
            UserData userData = postUser(usersBody);
            System.out.println("users-res : " + userData.toString());
            usersId = userData.getId();

            String postBody = "{" + "\"user_id\": \""+usersId+"\"," + "\"title\": \""+postUserTitle+"\"," + "\"body\": \""+postUserBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(postBody);
            System.out.println("post-res : " + postsUserData.toString());
            postUserId = postsUserData.getId();

            String commentsBody = "{" + "\"post_id\": \""+postUserId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            CommentsPostData commentsUserData = postCommentsUsers(commentsBody);
            System.out.println("comm-res : " + commentsUserData.toString());
            commentsId = commentsUserData.getId();

            String body = "{" + "\"user_id\": \""+commentsId+"\"," + "\"title\":\""+userTitle+"\"," + "\"due_on\": \""+userDueOn+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";

            String updateTitle = faker.name().title();
            String updatedBody = "{" + "\"title\":\""+updateTitle+"\"," + "\"due_on\": \""+userDueOn+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            TodosPostData todosUserData = todosPostUser(body);
            todosUserId = todosUserData.getId();
            TodosPostData todosUpdateUserData = updateTodosUserDetail(updatedBody, todosUserId);

//            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
//            assertThat(updatedResponse.statusCode(), is(HttpStatus.SC_OK));
            assertThat(todosUpdateUserData.getId(), is(todosUserId));
            assertThat(todosUpdateUserData.getUser_id(), is(commentsId));
            assertThat(todosUpdateUserData.getDue_on(), is(userDueOn));
            assertThat(todosUpdateUserData.getTitle(), is(updateTitle));
            assertThat(todosUpdateUserData.getStatus(), is(userStatus));
        }finally {
            Response response = deleteTodosUser(todosUserId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deleteComments = deleteCommentsUser(commentsId);
            assertThat(deleteComments.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deletePosts = deleteUserByIdPosts(postUserId);
            assertThat(deletePosts.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deleteUsers = deleteUser(usersId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 32)
    public void updateTodosUserByInvalidId(){
        int postUserId = 0;
        int usersId = 0;
        int commentsId = 0;
        int todosUserId = 0;

        Faker faker = new Faker();
        String usersEmail = faker.internet().emailAddress();
        String usersName = faker.name().fullName();
        String usersGender = "male";
        String usersStatus = "active";

        String postUserTitle = faker.name().title();
        String postUserBody = faker.lorem().fixedString(50);

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();

        String userTitle = faker.name().title();
        String userDueOn = "2022-05-03T00:00:00.000+05:30";
        String userStatus = "pending";
        try {
            String usersBody = "{" + "\"name\": \""+usersName+"\"," + "\"email\": \""+usersEmail+"\"," + "\"gender\": \""+usersGender+"\"," + "\"status\": \""+usersStatus+"\"\n" + "}";
            UserData userData = postUser(usersBody);
            System.out.println("users-res : " + userData.toString());
            usersId = userData.getId();

            String postBody = "{" + "\"user_id\": \""+usersId+"\"," + "\"title\": \""+postUserTitle+"\"," + "\"body\": \""+postUserBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(postBody);
            System.out.println("post-res : " + postsUserData.toString());
            postUserId = postsUserData.getId();

            String commentsBody = "{" + "\"post_id\": \""+postUserId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            CommentsPostData commentsUserData = postCommentsUsers(commentsBody);
            System.out.println("comm-res : " + commentsUserData.toString());
            commentsId = commentsUserData.getId();

            String body = "{" + "\"user_id\": \""+commentsId+"\"," + "\"title\":\""+userTitle+"\"," + "\"due_on\": \""+userDueOn+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";

            String updateTitle = faker.name().title();
            String updatedBody = "{" + "\"title\":\""+updateTitle+"\"," + "\"due_on\": \""+userDueOn+"\"," + "\"status\": \""+userStatus+"\"\n" + "}";
            TodosPostData todosPostUserData = todosPostUser(body);

            Response todosUpdateUserData = invalidUpdateTodosUserDetail(updatedBody, todosUserId);
            JSONObject jsonObject = new JSONObject(todosUpdateUserData.asString());
            System.out.println("upRes : " + todosUpdateUserData.asString());

//            assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));
            assertThat(todosUpdateUserData.statusCode(), is(HttpStatus.SC_NOT_FOUND));
            assertThat(jsonObject.getString("message"), containsString("Resource not found"));
        }finally {
            Response response = deleteTodosUser(todosUserId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NOT_FOUND));

            Response deleteComments = deleteCommentsUser(commentsId);
            assertThat(deleteComments.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deletePosts = deleteUserByIdPosts(postUserId);
            assertThat(deletePosts.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deleteUsers = deleteUser(usersId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 16)
    public void deleteTodosUserById() {
        int postUserId = 0;
        int usersId = 0;
        int commentsId = 0;
        int todosUserId = 0;

        Faker faker = new Faker();
        String usersEmail = faker.internet().emailAddress();
        String usersName = faker.name().fullName();
        String usersGender = "male";
        String usersStatus = "active";

        String postUserTitle = faker.name().title();
        String postUserBody = faker.lorem().fixedString(50);

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();

        String userTitle = faker.name().title();
        String userDueOn = "2022-05-03T00:00:00.000+05:30";
        String userStatus = "pending";
        try {
            String usersBody = "{" + "\"name\": \""+usersName+"\"," + "\"email\": \""+usersEmail+"\"," + "\"gender\": \""+usersGender+"\"," + "\"status\": \""+usersStatus+"\"\n" + "}";
            UserData userData = postUser(usersBody);
            System.out.println("users-res : " + userData.toString());
            usersId = userData.getId();

            String postBody = "{" + "\"user_id\": \""+usersId+"\"," + "\"title\": \""+postUserTitle+"\"," + "\"body\": \""+postUserBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(postBody);
            System.out.println("post-res : " + postsUserData.toString());
            postUserId = postsUserData.getId();

            String commentsBody = "{" + "\"post_id\": \""+postUserId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            CommentsPostData commentsUserData = postCommentsUsers(commentsBody);
            System.out.println("comm-res : " + commentsUserData.toString());
            commentsId = commentsUserData.getId();

            String body = "{" + "\"user_id\": \"" + commentsId + "\"," + "\"title\":\"" + userTitle + "\"," + "\"due_on\": \"" + userDueOn + "\"," + "\"status\": \"" + userStatus + "\"\n" + "}";
            TodosPostData todosUserData = todosPostUser(body);
            todosUserId = todosUserData.getId();
        }finally {
            Response response = deleteTodosUser(todosUserId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deleteComments = deleteCommentsUser(commentsId);
            assertThat(deleteComments.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deletePosts = deleteUserByIdPosts(postUserId);
            assertThat(deletePosts.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deleteUsers = deleteUser(usersId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    @Test(priority = 16)
    public void deleteTodosUserByInvalidId() {
        int postUserId = 0;
        int usersId = 0;
        int commentsId = 0;
        int todosUserId = 0;

        Faker faker = new Faker();
        String usersEmail = faker.internet().emailAddress();
        String usersName = faker.name().fullName();
        String usersGender = "male";
        String usersStatus = "active";

        String postUserTitle = faker.name().title();
        String postUserBody = faker.lorem().fixedString(50);

        String userName = faker.name().fullName();
        String userBody = faker.lorem().fixedString(50);
        String userEmail = faker.internet().emailAddress();

        String userTitle = faker.name().title();
        String userDueOn = "2022-05-03T00:00:00.000+05:30";
        String userStatus = "pending";
        try {
            String usersBody = "{" + "\"name\": \""+usersName+"\"," + "\"email\": \""+usersEmail+"\"," + "\"gender\": \""+usersGender+"\"," + "\"status\": \""+usersStatus+"\"\n" + "}";
            UserData userData = postUser(usersBody);
            System.out.println("users-res : " + userData.toString());
            usersId = userData.getId();

            String postBody = "{" + "\"user_id\": \""+usersId+"\"," + "\"title\": \""+postUserTitle+"\"," + "\"body\": \""+postUserBody+"\"\n" + "}";
            UsersDataPosts postsUserData = postsUsers(postBody);
            System.out.println("post-res : " + postsUserData.toString());
            postUserId = postsUserData.getId();

            String commentsBody = "{" + "\"post_id\": \""+postUserId+"\"," + "\"email\":\""+userEmail+"\"," + "\"name\": \""+userName+"\"," + "\"body\": \""+userBody+"\"\n" + "}";
            CommentsPostData commentsUserData = postCommentsUsers(commentsBody);
            System.out.println("comm-res : " + commentsUserData.toString());
            commentsId = commentsUserData.getId();

            String body = "{" + "\"user_id\": \"" + commentsId + "\"," + "\"title\":\"" + userTitle + "\"," + "\"due_on\": \"" + userDueOn + "\"," + "\"status\": \"" + userStatus + "\"\n" + "}";
            TodosPostData todosUserData = todosPostUser(body);
        }finally {
            Response response = deleteTodosUser(todosUserId);
            assertThat(response.statusCode(), is(HttpStatus.SC_NOT_FOUND));

            Response deleteComments = deleteCommentsUser(commentsId);
            assertThat(deleteComments.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deletePosts = deleteUserByIdPosts(postUserId);
            assertThat(deletePosts.statusCode(), is(HttpStatus.SC_NO_CONTENT));

            Response deleteUsers = deleteUser(usersId);
            assertThat(deleteUsers.statusCode(), is(HttpStatus.SC_NO_CONTENT));
        }
    }

    public UserData postUser( String body){
        UserData userData= given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .body(body)
                .request(Method.POST, "/public/v2/users/")
                .then()
                .extract()
                .response()
                .as(UserData.class);

        return userData;
    }

    public Response invalidPostUser( String body){
        Response response= given()
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

    public UserData getUser(int userId){
            UserData getUserData = given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .when()
                    .param("access-token", tokenNumber)
                    .request(Method.GET, "/public/v2/users/"+userId)
                    .then()
                    .extract()
                    .response()
                    .as(UserData.class);

            return getUserData;
    }

    public Response getInvalidUser(int userId){
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .param("access-token", tokenNumber)
                .request(Method.GET, "/public/v2/users/"+userId)
                .then()
                .extract()
                .response();

        return response;
    }

    public UserData updateUserDetail(String bodyForUpdation, int userId){
        UserData updateUserData = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .body(bodyForUpdation)
                .request(Method.PUT, "/public/v2/users/"+userId)
                .then()
                .extract()
                .response().as(UserData.class);

        return updateUserData;
    }

    public Response updateInvalidUserDetail(String bodyForUpdation, int userId){
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

        return response;
    }

    public UsersDataPosts postsUsers(String body){
        UsersDataPosts postsUserData = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .body(body)
                .request(Method.POST, "/public/v2/posts/")
                .then()
                .extract()
                .response()
                .as(UsersDataPosts.class);

        return postsUserData;
    }

    public Response invalidPostsUsers(String body){
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

    public UsersDataPosts getUserPosts(int id){
        UsersDataPosts postsUserData = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .request(Method.GET, "/public/v2/posts/"+id)
                .then()
                .extract()
                .response()
                .as(UsersDataPosts.class);

        return postsUserData;
    }

    public Response getInvliadUserPosts(int id){
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .request(Method.GET, "/public/v2/posts/"+id)
                .then()
                .extract()
                .response();

        return response;
    }

    public UsersDataPosts updateUserPosts(String updatedBody, int id){
        UsersDataPosts postsUserUpdate = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .body(updatedBody)
                .request(Method.PUT, "/public/v2/posts/"+id)
                .then()
                .extract()
                .response()
                .as(UsersDataPosts.class);

        return postsUserUpdate;
    }

    public Response updateInvalidUserPosts(String updatedBody, int id){
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

        return response;
    }

    public CommentsPostData postCommentsUsers(String body){
        CommentsPostData commentsUserData = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .body(body)
                .request(Method.POST, "/public/v2/comments/")
                .then()
                .extract()
                .response()
                .as(CommentsPostData.class);

        return commentsUserData;
    }

    public Response invalidPostCommentsUsers(String body){
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

    public CommentsPostData getUserComments(int id){
        CommentsPostData commentsUserData = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .request(Method.GET, "/public/v2/comments/"+id)
                .then()
                .extract()
                .response()
                .as(CommentsPostData.class);

        return commentsUserData;
    }

    public Response getInvalidUserComments(int id){
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .request(Method.GET, "/public/v2/comments/"+id)
                .then()
                .extract()
                .response();

        return response;
    }

    public CommentsPostData updateCommentsUser(String updateBody, int userId){
        CommentsPostData commentsUserData = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .body(updateBody)
                .request(Method.PUT, "/public/v2/comments/"+userId)
                .then()
                .extract()
                .response()
                .as(CommentsPostData.class);

        return commentsUserData;
    }

    public Response updateInvalidCommentsUser(String updateBody, int userId){
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

        return response;
    }

    public TodosPostData todosPostUser( String body){
        TodosPostData todosUserData = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .body(body)
                .request(Method.POST, "/public/v2/todos/")
                .then()
                .extract()
                .response()
                .as(TodosPostData.class);

        return todosUserData;
    }

    public Response invalidTodosPostUser( String body){
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

    public TodosPostData todosGetUser(int id){
        TodosPostData todosUserData = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .param("access-token", tokenNumber)
                .request(Method.GET, "/public/v2/todos/"+id)
                .then()
                .extract()
                .response()
                .as(TodosPostData.class);

        return todosUserData;
    }

    public Response invalidTodosGetUser(int id){
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .param("access-token", tokenNumber)
                .request(Method.GET, "/public/v2/todos/"+id)
                .then()
                .extract()
                .response();

        return response;
    }

    public TodosPostData updateTodosUserDetail(String updatedBody, int id){
        TodosPostData todosUserData = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .queryParam("access-token", tokenNumber)
                .body(updatedBody)
                .request(Method.PUT, "/public/v2/todos/"+id)
                .then()
                .extract()
                .response()
                .as(TodosPostData.class);

        return todosUserData;
    }

    public Response invalidUpdateTodosUserDetail(String updatedBody, int id){
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

        return response;
    }
}
