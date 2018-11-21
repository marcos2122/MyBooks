package mjimeno.mybooks2.Red;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import mjimeno.mybooks2.Activities.BookListActivity;
import mjimeno.mybooks2.Fragments.BookListFragment;

public class ConnectivityChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (TestearRed.isNetworkConnected(context))
        {
           // Toast.makeText(context, "Red disponible",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context, "Red no disponible",Toast.LENGTH_LONG).show();

        }
    }
}
