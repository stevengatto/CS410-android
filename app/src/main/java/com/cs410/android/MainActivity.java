package com.cs410.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class MainActivity extends Activity {

    EditText userName, password;
    /**
     * This will handle all of the buttons on the menu
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sign_in);
        initialize();
    }

    private void initialize() {
        userName = (EditText) findViewById(R.id.et_userEmail);
        password = (EditText) findViewById(R.id.et_userPassword);
    }

    public void loginClick(View v){


    }

    public void registerClick(View v){
        //Does this work for you STEVE?
        startActivity(new Intent(this, register.class));
    }
}
