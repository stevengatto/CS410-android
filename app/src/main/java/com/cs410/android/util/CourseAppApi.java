package com.cs410.android.util;

import com.cs410.android.model.Course;
import com.cs410.android.model.SigninResponse;
import com.cs410.android.model.User;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Interface to turn the Nimblenotes API into a Java Interface
 */
public interface CourseAppApi {

    /**
     * Synchronous POST request to sign in a user
     *
     * @param userToSignIn user to be signed in
     */
    @POST("/auth/local")
    SigninResponse signIn(@Body User userToSignIn);

    /**
     * POST request to sign in a user
     *
     * @param userToSignIn user to be signed in
     * @param callback callback method to execute when the POST request finishes
     */
    @POST("/auth/local")
    void signIn(@Body User userToSignIn, Callback<SigninResponse> callback);

    /**
     * POST request to sign up a user
     *
     * @param userToSignUp user to be signed up
     * @param callback callback method to execute when the POST request finishes
     */
    @POST("/users")
    void signUp(@Body User userToSignUp, Callback<SigninResponse> callback);

    @GET("/courses")
    void getCourses(Callback<List<Course>> callback);

    @GET("/courses/{id}")
    void getSingleCourse(@Path("id") String id, Callback<Course> callback);
}