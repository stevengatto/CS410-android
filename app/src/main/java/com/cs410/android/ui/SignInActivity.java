package com.cs410.android.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.cs410.android.R;
import com.cs410.android.model.SigninResponse;
import com.cs410.android.model.User;
import com.cs410.android.util.AccountUtils;
import com.cs410.android.util.CourseAppApi;
import com.cs410.android.util.WebUtils;

import retrofit.client.Response;

public class SignInActivity extends Activity {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    private EditText txtEmail, txtPassword;
    private String email, password;
    private Context context = this;

    /**
     * This will handle all of the buttons on the menu
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signin);
        initialize();
    }

    private void initialize() {
        txtEmail = (EditText) findViewById(R.id.et_userEmail);
        txtPassword = (EditText) findViewById(R.id.et_userPassword);
    }

    public void loginClick(View v){
        email = txtEmail.getText().toString();
        password = txtPassword.getText().toString();
        User user = new User(email, password);

        CourseAppApi api = AccountUtils.getUnauthenticatedSignInApiInterface();
        api.signIn(user, new SignInCallback(this));
    }

    public void registerClick(View v){
        //Does this work for you STEVE?
        startActivity(new Intent(this, SignUpActivity.class));
    }

    /**
     * Private class to handle callback after sign up occurs
     */
    private class SignInCallback extends WebUtils.RetroCallback<SigninResponse> {

        public SignInCallback(Context context) {
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
            accountManager.setAuthToken(account, AccountUtils.AUTHTOKEN_TYPE, signinResponse.token);
        }
    }
}
