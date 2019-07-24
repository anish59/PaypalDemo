package bbt.com.paypaldemo.helper;

import android.content.Context;

/**
 * Created by xitij on 17-03-2015.
 */
public class PrefUtils {

    private static String PayPalAccessToken = "PayPalAccessToken";

    public static void setPayPalAccessToken(Context ctx, String value) {
        Prefs.with(ctx).save(PayPalAccessToken, value);
    }

    public static String getPayPalAccessToken(Context ctx) {
        return Prefs.with(ctx).getString(PayPalAccessToken, " ");
    }
}
