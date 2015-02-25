package com.cs410.android.account;

import android.os.Bundle;

import com.cs410.android.util.CourseAppApi;

/**
 * This interface allows a class to receive an auth token from our server via the
 * {@link #onAuthReceived(boolean, com.cs410.android.util.CourseAppApi, android.os.Bundle)} callback by calling
 * {@link com.cs410.android.util.AccountUtils#authenticate(android.content.Context, Authenticatable)}
 * from the Authenticatable Activity.
 */
public interface Authenticatable {

    /**
     * Callback for auth token request from
     * {@link com.cs410.android.util.AccountUtils#authenticate(android.content.Context, Authenticatable)}
     *
     * @param success true if client is authenticated, false otherwise
     * @param api retrofit api object with auth headers added
     */
    public void onAuthReceived(boolean success, CourseAppApi api, Bundle bundle);

}