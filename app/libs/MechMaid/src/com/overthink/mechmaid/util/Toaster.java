package com.overthink.mechmaid.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Makes toast!
 */
public class Toaster {

    /**
     * Shows a short toast message with the desired resource as text
     *
     * @param context currently running context
     * @param resourceId id of string resource to set as toast message
     *
     * @return created Toast object
     */
    public static Toast showToastFromResource(Context context, int resourceId) {
        Toast toast = Toast.makeText(context, context.getString(resourceId), Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }

    /**
     * Shows a toast message with the desired resource as text and with the desired duration
     *
     * @param context currently running context
     * @param resourceId id of string resource to set as toast message
     * @param duration how long to show the toast message (Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     *
     * @return created Toast object
     */
    public static Toast showToastFromResource(Context context, int resourceId, int duration) {
        Toast toast = Toast.makeText(context, context.getString(resourceId), duration);
        toast.show();
        return toast;
    }

    /**
     * Shows a short toast message with the desired string as text
     *
     * @param context currently running context
     * @param string string to set as toast message
     *
     * @return created Toast object
     */
    public static Toast showToastFromString(Context context, String string) {
        Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }

    /**
     * Shows a toast message with the desired string as text and with the desired duration
     *
     * @param context currently running context
     * @param string string to set as toast message
     * @param duration how long to show the toast message (Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     *
     * @return created Toast object
     */
    public static Toast showToastFromString(Context context, String string, int duration) {
        Toast toast = Toast.makeText(context, string, duration);
        toast.show();
        return toast;
    }

}
