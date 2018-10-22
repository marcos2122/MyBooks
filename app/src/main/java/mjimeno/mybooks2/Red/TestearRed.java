package mjimeno.mybooks2.Red;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class TestearRed {
    public static boolean isNetworkConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null || !info.isConnected() || !info.isAvailable()) {
            return false;
        }
        return true;
    }
}
