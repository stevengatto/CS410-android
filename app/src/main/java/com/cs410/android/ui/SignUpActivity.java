package com.cs410.android.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.cs410.android.R;
import com.cs410.android.model.SigninResponse;
import com.cs410.android.model.User;
import com.cs410.android.util.AccountUtils;
import com.cs410.android.util.CourseAppApi;
import com.cs410.android.util.WebUtils;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.annotations.RegExp;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static eu.inmite.android.lib.validations.form.annotations.RegExp.EMAIL;

/**
 * Steve's a nerd
 * Created by user on 2/24/2015.
 */
public class SignUpActivity extends Activity {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    @NotEmpty(messageId = R.string.validation_name_empty, order = 1)
    private EditText txtName;

    @NotEmpty(messageId = R.string.validation_email_empty, order = 2)
    @RegExp(value = EMAIL, messageId = R.string.validation_email_format, order = 3)
    private EditText txtEmail;

    @NotEmpty(messageId = R.string.validation_password_empty, order = 4)
    @MinLength(value = 8, messageId = R.string.validation_password_min_len, order = 5)
    private EditText txtPassword;

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
        FormValidator.startLiveValidation(this, findViewById(R.id.sign_up_form_container),
                new SimpleErrorPopupCallback(this));
    }

    public void registerNewAccount(View v) {
        // Validate fields, quit if any have errors
        boolean fieldsValid = FormValidator.validate(this, new SimpleErrorPopupCallback(this, true));
        if (!fieldsValid) {
            return;
        }

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
            String token = "Bearer " + signinResponse.token;
            boolean addNewAccount = getIntent().getBooleanExtra(AccountUtils.FLAG_ADDING_NEW_ACCOUNT, false);
            boolean launchActivity = getIntent().getBooleanExtra(AccountUtils.FLAG_LAUNCH_ACTIVITY, false);
            Intent resultIntent = new Intent();
            resultIntent.putExtra(AccountUtils.FLAG_ADDING_NEW_ACCOUNT, addNewAccount);
            resultIntent.putExtra(AccountUtils.FLAG_LAUNCH_ACTIVITY, launchActivity);
            resultIntent.putExtra(AccountUtils.PARAM_TOKEN, token);
            resultIntent.putExtra(AccountUtils.PARAM_EMAIL, email);
            resultIntent.putExtra(AccountUtils.PARAM_PASSWORD, password);
            setResult(RESULT_OK, resultIntent);
            finish();
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            super.failure(retrofitError);
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}

