package com.cs410.android.util;

import android.content.Context;

import com.overthink.mechmaid.util.Toaster;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * A utility class for web services
 */
public class WebUtils {

    /**
     * The URL to the NimbleNotes web server
     */
    public static final String SERVER = "https://fast-reaches-4404.herokuapp.com";

    /**
     * The base url used in all web calls to the NimbleNotes server
     */
    public static final String BASE_API_URL = SERVER + "/api";

    /**
     * The HTTP header key for a user's auth token (for authenticated web calls)
     */
    public static final String KEY_HEADER_TOKEN = "Authorization";

    /**
     * Retrofit callback wrapper class that handles all callback errors.
     */
    public static abstract class RetroCallback<T> implements Callback<T> {

        private Context context;

        public RetroCallback(Context context){
            this.context = context;
        }

        /**
         * Handles general retrofit response failures
         *
         * @param retrofitError
         */
        @Override
        public void failure(RetrofitError retrofitError) {
            switch(retrofitError.getKind()) {
                case HTTP:
                    Toaster.showToastFromString(context, "Error: " + retrofitError.getResponse().getStatus());
                    break;
                case CONVERSION:
                    Toaster.showToastFromString(context, "Conversion Error");
                    break;
                case NETWORK:
                    Toaster.showToastFromString(context, "Network Error");
                    break;
                case UNEXPECTED:
                    Toaster.showToastFromString(context, "Unexpected Error");
                    break;
            }
        }
    }
}
