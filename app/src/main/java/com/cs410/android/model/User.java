package com.cs410.android.model;

/**
 * Created by steven on 2/25/15.
 */
public class User {
    String name;
    String email;
    String password;

    public User(String email, String password) {
        this(null, email, password);
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
