package mjimeno.mybooks2.Red;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
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
    private Context mContext=this;



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

        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);


        Intent intent1 = new Intent(mContext, BookListActivity.class);
        intent1.setAction(AppConstant.DELETE_BOOK_ACTION); //añadimos action borrar
        intent1.putExtra("book_position", data.get("book_position")); //pasamos el valor del custom data
        PendingIntent borrarLibroIntent = PendingIntent.getActivity(mContext, (int) System.currentTimeMillis(), intent1, 0);

        Intent intent2 = new Intent(mContext, BookListActivity.class);
        intent2.setAction(AppConstant.SHOW_DETAILS_BOOK); // añadimos action mostrar detalle
        intent2.putExtra("book_position", data.get("book_position"));//pasamos el valor del custom data
        PendingIntent mostrarDetalleIntent = PendingIntent.getActivity(mContext, (int) System.currentTimeMillis(), intent2, 0);



        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext, getString(R.string.default_notification_channel_id));
              // creamos notificación


        mBuilder.setSmallIcon(R.mipmap.icono_logo); // icono libro
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icono_logo));
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(body);
        //añadimos acciones
        mBuilder.addAction(new NotificationCompat.Action(R.drawable.ic_outline_delete, getString(R.string.delete_book), borrarLibroIntent));
        mBuilder.addAction(new NotificationCompat.Action(R.drawable.ic_list_detail, getString(R.string.show_bookdetail), mostrarDetalleIntent));

       //añadimos un bigStyle
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.bigtext)+ " " + data.get("book_position")));




     // Si es version Oreo o mas es necesario crear canal y establecemos sus correpondientes atributos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //para android oreo se necesita NotificationChannel
            Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext.getPackageName() + "/" + R.raw.mision_imposible_peliculas);

            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(getString(R.string.default_notification_channel_id), name, importance);
            //creamos canal
            channel.setDescription(description);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            // necesario crear objeto AudioAttributes porque el metodo setSound requiere dos parametros ( la Uri del sonido, y AudioAttributes)

            channel.enableLights(true);
            channel.setLightColor(Color.BLUE); // led azul
            channel.setSound(sound, attributes); // reproduce un MP3 que esta en la carpeta RES/raw
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400}); // vibración
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(channel);
        }
        else{
            // para versiones anteriores a Android 8.0
            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(defaultSoundUri);
            mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            mBuilder.setAutoCancel(true);
            mBuilder.setLights(Color.BLUE,1,1);
            mBuilder.setVibrate(new long[]{500,500,500,500,500});

            }


        assert mNotificationManager != null;
        mNotificationManager.notify(0, mBuilder.build());


    }

    }

