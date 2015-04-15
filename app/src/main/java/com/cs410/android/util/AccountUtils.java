package com.cs410.android.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.cs410.android.account.Authenticatable;
import com.cs410.android.ui.SignInActivity;
import com.cs410.android.ui.SignUpActivity;

import java.io.IOException;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * A utility class for NimbleNotes user accounts
 *
 * <p>
 * Contains data tags used in Bundles, Intent Extras, and JSON.
 * <p>
 * Handles asynchronously getting an authentication token from the account manager
 */
public class AccountUtils {

    private static String TAG = AccountUtils.class.getName();

    // Must match account_type in strings.xml (referenced in authenticator.xml)
    public static final String ACCOUNT_TYPE = "com.cs410.android";

    public static final String ACCOUNT_NAME = "EMAIL";

    // Default NimbleNotes authentication token type
    public static final String AUTHTOKEN_TYPE = "AUTHTOKEN_TYPE";

    // Intent flags and params
    public static final String FLAG_ADDING_NEW_ACCOUNT = "ADDING_NEW_ACCOUNT";
    public static final String FLAG_LAUNCH_ACTIVITY = "LAUNCH_ACTIVITY";
    public static final String PARAM_TOKEN = "PARAM_TOKEN";
    public static final String PARAM_EMAIL = "PARAM_EMAIL";
    public static final String PARAM_PASSWORD = "PARAM_PASSWORD";

    private static AccountManager accountManager;
    private static Account account;
    private static Context context;
    private static Authenticatable caller;

    /**
     * Begins the process to get an authtoken from the account manager
     *
     * If an {@link android.accounts.Account} for this application exists, then it will asynchronously attempt to find
     * its auth token.
     *
     * @param context context of the application
     * @param caller calling object that implements {@link com.cs410.android.account.Authenticatable}
     */
    public static void authenticate(Context context, Authenticatable caller) {
        AccountUtils.context = context;
        AccountUtils.caller = caller;
        AccountUtils.accountManager = AccountManager.get(context);


        // Retrieve list of Accounts in the AccountManager and check if any match that of ours
        // Spawn AsyncTask if match is found
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        if (accounts.length > 0) {
            Log.d(TAG, "Account found. Calling getAuthToken");
            account = accounts[0];
            (new GetAuthTokenAsyncTask()).execute();
        }
        // If no Account exists, add Account then getAuthToken
        else {
            Log.d(TAG, "No account found. Creating account then calling getAuthToken");
            Intent intent = new Intent(context, SignInActivity.class);
            // Add flag to mark that a new Account must be created
            intent.putExtra(FLAG_ADDING_NEW_ACCOUNT, true);
            intent.putExtra(FLAG_LAUNCH_ACTIVITY, true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            ((Activity) context).finish();
        }
    }

    // USES SERVER BASE URL NOT API BASE URL!!!!!!
    public static CourseAppApi getUnauthenticatedApiInterface(){
        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(WebUtils.SERVER)
                .setClient(new OkClient())
                .build();

        // Create api to interact with the server as a Java interface
        return restAdapter.create(CourseAppApi.class);
    }

    private static CourseAppApi getAuthenticatedApiInterface(final String authToken){
        // Create request interceptor to add headers to retrofit web calls
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader(WebUtils.KEY_HEADER_TOKEN, authToken);
            }
        };

        // Create Rest adapter using the OkHttp library as a client
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(WebUtils.BASE_API_URL)
                .setClient(new OkClient())
                .setRequestInterceptor(requestInterceptor)
                .build();

        // Create nimblenote api to interact with the server as a Java interface
        return restAdapter.create(CourseAppApi.class);
    }

    /**
     * An AsyncTask to get an auth token from Android's Account Manager
     */
    private static class GetAuthTokenAsyncTask extends AsyncTask<Void, Void, Bundle> {

        public GetAuthTokenAsyncTask() { }
        /**
         * Gets an auth token from the {@link android.accounts.AccountManager}
         *
         * @return data bundle received from the {@link android.accounts.AccountManager}, null if none
         */
        @Override
        protected Bundle doInBackground(Void... params) {
            if (account != null) {
                AccountManagerFuture future = accountManager.getAuthToken(account,
                        AUTHTOKEN_TYPE, null, (Activity) context, null, null);
                try {
                    return (Bundle) future.getResult();
                } catch (OperationCanceledException | AuthenticatorException | IOException e) {
                    Log.d(TAG, e.getMessage());
                }
            }
            return null;
        }

        /**
         * Unpack Bundle from {@link android.accounts.AccountManager} and decide whether to set
         * main page content view or start
         * sign in page to prompt the user to enter their credentials
         *
         * @param bundle data bundle received from the Account, null if none
         */
        @Override
        protected void onPostExecute(Bundle bundle) {
            if(bundle == null) {
                caller.onAuthReceived(false, null, null);
            }
            else {
                // Unpack Bundle
                final String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);

                // Get api to interact with the server as a Java interface and return
                CourseAppApi courseAppApi = getAuthenticatedApiInterface(authToken);
                caller.onAuthReceived(true, courseAppApi, bundle);
            }
        }
    }
}