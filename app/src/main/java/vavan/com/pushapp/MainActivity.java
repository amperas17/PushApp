package vavan.com.pushapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    public static final String MY_ACTION = "com.vavan.broadcast";
    public static final String ALARM_MESSAGE = "Срочно открой приложение!";
    public static final String MY_EXTRA = "com.vavan.broadcast.Message";

    private static final int NOTIFY_ID = 101;

    int notifyID = NOTIFY_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Context context = getApplicationContext();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public void onClickNotify(View view) {
        Context context = getApplicationContext();

        Intent notificationIntent = new Intent(context, MainActivity.class);
        //открывать ту же активность
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);

        //long vibrates[] = {1000,500,1000,1000,1000};
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.mdpipowerighltning)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.molniya))
                .setTicker("Тебе пришло сообщение!")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                //.setVibrate(vibrates)
                .setContentTitle("Напоминание #" + notifyID)
                .setContentText("Текст уведомления")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND |
                        Notification.DEFAULT_VIBRATE);


        Notification notification = builder.build();

        //настойчивое уведомление
        //notification.flags = notification.flags | Notification.FLAG_INSISTENT;

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifyID, notification);

        notifyID++;
    }


    public void onClickBroadcast(View view) {
        Intent intent = new Intent();
        intent.setAction(MY_ACTION);
        intent.putExtra(MY_EXTRA, ALARM_MESSAGE);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);
    }


    public void onClickCall(View view) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:+79788481128"));
        startActivity(intent);
    }


    public void onClickStartService(View v) {
        startService(new Intent(this, MyService.class));
    }

    public void onClickStopService(View v) {
        stopService(new Intent(this, MyService.class));
    }
}
