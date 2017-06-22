package ominext.com.readmestories.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Vinh on 10/28/2016.
 */

public class ProgressDialogUtils {

    public static ProgressDialog create(Context context) {
        return create(context, "Please wait...", false);
    }

    public static ProgressDialog create(Context context, String message) {
        return create(context, message, true);
    }

    public static ProgressDialog create(Context context, String message, Boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(cancelable);
        progressDialog.setIndeterminate(true);
        return progressDialog;
    }
}
