package com.cs410.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Steve's a nerd
 * Created by user on 2/24/2015.
 */
public class register extends Activity {

    EditText email, password, passwordConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initialize();
    }

    private void initialize() {
        email = (EditText) findViewById(R.id.et_register_email);
        password = (EditText) findViewById(R.id.et_register_password);
        passwordConfirm = (EditText) findViewById(R.id.et_register_passwordConfirmation);
    }

    public void registerNewAccount(View v){


    }
}
