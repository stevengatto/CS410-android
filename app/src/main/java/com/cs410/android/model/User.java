package com.cs410.android.model;

import java.util.List;

/**
 * Created by steven on 2/25/15.
 */
public class User {
    public String name;
    public String email;
    public String password;
    public List<Course> enrolledCourses;

    public User(String email, String password) {
        this(null, email, password);
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
