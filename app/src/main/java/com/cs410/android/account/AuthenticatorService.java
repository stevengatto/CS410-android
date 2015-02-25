package com.cs410.android.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * A Service allowing other applications to bind to our AccountAuthenticator.
 */
public class AuthenticatorService extends Service {

    /**
     * Construct IBinder for other applications to use AccountAuthenticator
     *
     * @param intent Intent from caller
     * @return instance of IBinder for binding to AccountAuthenticator
     */
    @Override
    public IBinder onBind(Intent intent) {
        AccountAuthenticator authenticator = new AccountAuthenticator(this);
        return authenticator.getIBinder();
    }
}