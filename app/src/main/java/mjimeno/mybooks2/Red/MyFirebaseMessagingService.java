package mjimeno.mybooks2.Red;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import mjimeno.mybooks2.Activities.BookDetailActivity;
import mjimeno.mybooks2.Activities.BookListActivity;
import mjimeno.mybooks2.Activities.LoginActivity;
import mjimeno.mybooks2.Activities.PresentationActivity;
import mjimeno.mybooks2.Activities.SplashActivity;
import mjimeno.mybooks2.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "Noticias";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from = remoteMessage.getFrom();
        Log.d(TAG,"Mensaje recibido de" + from);

             if (remoteMessage.getNotification()!= null) {
                 Log.d(TAG, "Notificacion: " + remoteMessage.getNotification().getBody());

                 sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
              }

            if (remoteMessage.getData().size()>0)
              {
              Log.d(TAG,"Data: " +remoteMessage.getData());

              }

    }

    private void sendNotification(String title, String body) {
        Log.d(TAG,"Cuerponotif: " +body);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id));

        Intent ii = new Intent(this, PresentationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(body);
        mBuilder.setSound(soundUri);
        mBuilder.setPriority(Notification.PRIORITY_MAX);


        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //para android oreo se necesita NotificationChannely NotificationCompat.Builder(this)está en desuso

            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(getString(R.string.default_notification_channel_id),name,importance);
            channel.setDescription(description);

            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(0, mBuilder.build());



    }
}
