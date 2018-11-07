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

import java.util.Map;

import mjimeno.mybooks2.Activities.BookDetailActivity;
import mjimeno.mybooks2.Activities.BookListActivity;
import mjimeno.mybooks2.Activities.LoginActivity;
import mjimeno.mybooks2.Activities.PresentationActivity;
import mjimeno.mybooks2.Activities.SplashActivity;
import mjimeno.mybooks2.Constants.AppConstant;
import mjimeno.mybooks2.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "Noticias";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from = remoteMessage.getFrom();
        Log.d(TAG,"Mensaje recibido de" + from);

             if (remoteMessage.getNotification()!= null && remoteMessage.getData().size()>0 ) {
                 Log.d(TAG, "Notificacion: " + remoteMessage.getNotification().getBody());

                 sendNotification(remoteMessage.getNotification().getTitle(),
                         remoteMessage.getNotification().getBody(),
                         remoteMessage.getData());
              }

            if (remoteMessage.getData().size()>0)
              {
              Log.d(TAG,"book: " +remoteMessage.getData().get("book_position"));

              }

    }

    private void sendNotification(String title, String body, Map<String, String> data) {



        Intent intent1 = new Intent(this, BookListActivity.class);
        intent1.setAction(AppConstant.DELETE_BOOK_ACTION); //añadimos action borrar
        intent1.putExtra("book_position", data.get("book_position")); //pasamos el valor del custom data
        PendingIntent borrarLibroIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent1, 0);

        Intent intent2 = new Intent(this, BookListActivity.class);
        intent2.setAction(AppConstant.SHOW_DETAILS_BOOK); // añadimos action mostrar detalle
        intent2.putExtra("book_position", data.get("book_position"));//pasamos el valor del custom data
        PendingIntent mostrarDetalleIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent2, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id));



        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(body);
        mBuilder.addAction(new NotificationCompat.Action(R.drawable.ic_outline_delete, getString(R.string.delete_book), borrarLibroIntent));
        mBuilder.addAction(new NotificationCompat.Action(R.drawable.ic_list_detail, getString(R.string.show_bookdetail), mostrarDetalleIntent));

        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.bigtext)+ " " + data.get("book_position")));



        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //para android oreo se necesita NotificationChannely NotificationCompat.Builder(this)está en desuso

            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(getString(R.string.default_notification_channel_id), name, importance);
            channel.setDescription(description);

            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(0, mBuilder.build());


    }
    }

