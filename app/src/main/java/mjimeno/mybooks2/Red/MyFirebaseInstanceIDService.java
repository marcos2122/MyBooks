package mjimeno.mybooks2.Red;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    //se ejecuta cuando nos asignan un token o cuando se actualiza
    public static final String TAG = "Noticias";
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"TOKEN: " +token);

    }
}
