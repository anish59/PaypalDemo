package bbt.com.paypaldemo.helper;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressUtils {

    private static ProgressDialog progressDialog;

    public static void showProgress(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    public static void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}