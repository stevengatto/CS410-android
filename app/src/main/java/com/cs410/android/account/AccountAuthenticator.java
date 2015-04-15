package com.cs410.android.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.cs410.android.model.SigninResponse;
import com.cs410.android.model.User;
import com.cs410.android.ui.SignInActivity;
import com.cs410.android.util.AccountUtils;

import retrofit.RetrofitError;

/**
 * Authenticates the user with the server. Works to retrieve previous auth tokens from the AccountManager, create new
 * Accounts in the AccountManager, or update current Accounts.
 */
public class AccountAuthenticator extends AbstractAccountAuthenticator {

    private static final String TAG = AccountAuthenticator.class.getName();

    private Context context;
    private AccountManager accountManager;

    public AccountAuthenticator(final Context context) {
        super(context);

        this.context = context;
        accountManager = AccountManager.get(context);
    }

    /**
     * @inheritDoc
     *
     * <p>
     * Creates an Intent with {@link com.cs410.android.util.AccountUtils#FLAG_ADDING_NEW_ACCOUNT} to start
     * {@link com.cs410.android.ui.SignInActivity} with the intention of creating a new Account with
     * their credentials.
     * </p>
     *
     */
    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType,
                             String[] requiredFeatures, Bundle options) throws NetworkErrorException {

        // Clear out any previous accounts
        Account[] accounts = accountManager.getAccountsByType(accountType);
        for (int i=0; i < accounts.length; i++) {
            accountManager.removeAccount(accounts[i], null, null);
            // TODO: Log users out of accounts before they are cleared out
        }

        final Intent intent = new Intent(context, SignInActivity.class);
        intent.putExtra(AccountUtils.ACCOUNT_TYPE, accountType);
        intent.putExtra(AccountUtils.AUTHTOKEN_TYPE, authTokenType);
        intent.putExtra(AccountUtils.FLAG_ADDING_NEW_ACCOUNT, true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    /**
     * @inheritDoc
     *
     * <p>
     * If an auth token does not exist, try to re-authenticate with the server and call this method again. If an auth
     * token already exists, simply Bundle it up and return it. If neither of these cases works, start the
     * {@link com.cs410.android.ui.SignInActivity} and request user credentials.
     * </p>
     */
    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType,
                               Bundle options) throws NetworkErrorException {

        // Get instance of AccountManager and check for auth token
        final AccountManager am = AccountManager.get(context);
        String authToken = am.peekAuthToken(account, authTokenType);

        // Try to authenticate user with email and password
        if (TextUtils.isEmpty(authToken)) {
            Log.d(TAG, "No auth token found in getAuthToken");
            final String password = am.getPassword(account);
            if (password != null) {
                String email = account.name;
                Log.d(TAG, "Password not null, trying background login");

                // Try background sign in before deciding that, update auth token
                authToken = userSignIn(email, password);
                accountManager.setAuthToken(account, authTokenType, authToken);
            }
        }

        // If an auth token is found, return it
        if (!TextUtils.isEmpty(authToken)) {
            Log.d(TAG, "Saved auth token found");
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, AccountUtils.ACCOUNT_NAME);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, AccountUtils.ACCOUNT_TYPE);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }

        // Could not access the user's password, so prompt user for their credentials
        // by creating an Intent to display the QuickTourActivity
        Log.d(null, "Starting sign-in activity since there's nothing to work with");
        final Intent intent = new Intent(context, SignInActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(AccountUtils.ACCOUNT_TYPE, AccountUtils.ACCOUNT_TYPE);
        intent.putExtra(AccountUtils.AUTHTOKEN_TYPE, authTokenType);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    /**
     * Synchronously sign-in to our web server.
     *
     * <p>
     * Note: This method should not be called from the main thread.
     * </p>
     *
     * @param email user's email
     * @param password user's password
     * @return authtoken if received, null if none
     */
    public static String userSignIn(String email, String password) {
        String authtoken = null;

        try {
            User user = new User(email, password);
            SigninResponse signinResponse = AccountUtils.getUnauthenticatedAuthInterface().signIn(user);
            authtoken = signinResponse.token;
        } catch (RetrofitError e) {
            // Do nothing
        }

        // Keep complete logs
        if (authtoken == null) {
            Log.d(TAG, "Background login failed");
        }
        else {
            Log.d(TAG, "Background login successful");
        }

        // Returns an authtoken if web call is successful or null otherwise (this is desired behavior)
        return authtoken;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options)
                                     throws NetworkErrorException {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType,
                                    Bundle options) throws NetworkErrorException {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features)
                              throws NetworkErrorException {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Bundle getAccountRemovalAllowed(AccountAuthenticatorResponse response, Account account)
                                           throws NetworkErrorException {
        return super.getAccountRemovalAllowed(response, account);
    }
}
