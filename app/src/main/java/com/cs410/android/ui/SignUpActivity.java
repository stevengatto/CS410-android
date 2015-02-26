package com.cs410.android.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.cs410.android.R;
import com.cs410.android.model.SigninResponse;
import com.cs410.android.model.User;
import com.cs410.android.util.AccountUtils;
import com.cs410.android.util.CourseAppApi;
import com.cs410.android.util.WebUtils;
import com.overthink.mechmaid.util.Toaster;

import retrofit.client.Response;

/**
 * Steve's a nerd
 * Created by user on 2/24/2015.
 */
public class SignUpActivity extends Activity {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    private EditText txtName, txtEmail, txtPassword;
    private String name, email, password;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initialize();
    }

    private void initialize() {
        txtName = (EditText) findViewById(R.id.et_register_name);
        txtEmail = (EditText) findViewById(R.id.et_register_email);
        txtPassword = (EditText) findViewById(R.id.et_register_password);
    }

    public void registerNewAccount(View v) {
        name = txtName.getText().toString();
        email = txtEmail.getText().toString();
        password = txtPassword.getText().toString();

        User user = new User(name, email, password);
        CourseAppApi api = AccountUtils.getUnauthenticatedApiInterface();
        api.signUp(user, new SignUpCallback(this));
    }

    /**
     * Private class to handle callback after sign up occurs
     */
    private class SignUpCallback extends WebUtils.RetroCallback<SigninResponse> {

        public SignUpCallback(Context context) {
            super(context);
        }

        @Override
        public void success(SigninResponse signinResponse, Response response) {
            // create an Account to add/modify in AccountManager
            AccountManager accountManager = AccountManager.get(context);
            final Account account = new Account(email, AccountUtils.ACCOUNT_TYPE);

            // Create the account on the device and set the auth token we got
            Log.d(TAG, "Adding new account to account manager");
            accountManager.addAccountExplicitly(account, password, null);
            accountManager.setAuthToken(account, AccountUtils.AUTHTOKEN_TYPE, "Bearer " + signinResponse.token);
        }
    }
}

