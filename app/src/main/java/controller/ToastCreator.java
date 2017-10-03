package controller;

import android.content.Context;
import android.widget.Toast;

/**
 * Creates toast message
 *
 * Created by Ka Kyen Lam on 3/09/2017.
 */

public class ToastCreator {
    /**
     * Create toast message based on String parameter
     *
     * @param text Toast message
     */
    public static void createToast(Context context, String text) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
