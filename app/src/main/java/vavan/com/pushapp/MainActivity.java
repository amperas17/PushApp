package vavan.com.pushapp;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
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
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public static final String MY_ACTION = "com.vavan.broadcast";
    public static final String ALARM_MESSAGE = "Срочно открой приложение!";
    public static final String MY_EXTRA = "com.vavan.broadcast.Message";

    private static final int NOTIFY_ID = 101;


    final String LOG_TAG = "myLogs";

    final int TASK1_CODE = 1;
    final int TASK2_CODE = 2;
    final int TASK3_CODE = 3;

    public final static int STATUS_START = 100;
    public final static int STATUS_FINISH = 200;

    public final static String PARAM_TIME = "time";
    public final static String PARAM_TASK = "task";
    public final static String PARAM_RESULT = "result";
    public final static String PARAM_STATUS = "status";

    public final static String BROADCAST_ACTION = "com.vavan.servicebackbroadcast";

    TextView tvTask1;
    TextView tvTask2;
    TextView tvTask3;
    BroadcastReceiver br;



    int notifyID = NOTIFY_ID;
    ProgressBar pbForService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pbForService = (ProgressBar)findViewById(R.id.pbForService);

        tvTask1 = (TextView) findViewById(R.id.tvTask1);
        tvTask2 = (TextView) findViewById(R.id.tvTask2);
        tvTask3 = (TextView) findViewById(R.id.tvTask3);

        // создаем BroadcastReceiver
        br = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                int task = intent.getIntExtra(PARAM_TASK, 0);
                int status = intent.getIntExtra(PARAM_STATUS, 0);
                Log.d(LOG_TAG, "onReceive: task = " + task + ", status = " + status);

                // Ловим сообщения о старте задач
                if (status  == STATUS_START) {
                    switch (task) {
                        case TASK1_CODE:
                            tvTask1.setText("Task1 start");
                            break;
                        case TASK2_CODE:
                            tvTask2.setText("Task2 start");
                            break;
                        case TASK3_CODE:
                            tvTask3.setText("Task3 start");
                            break;
                    }
                }

                // Ловим сообщения об окончании задач
                if (status == STATUS_FINISH) {
                    int result = intent.getIntExtra(PARAM_RESULT, 0);
                    switch (task) {
                        case TASK1_CODE:
                            tvTask1.setText("Task1 finish, result = " + result);
                            break;
                        case TASK2_CODE:
                            tvTask2.setText("Task2 finish, result = " + result);
                            break;
                        case TASK3_CODE:
                            tvTask3.setText("Task3 finish, result = " + result);
                            break;
                    }
                }
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // дерегистрируем (выключаем) BroadcastReceiver
        unregisterReceiver(br);
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
        pbForService.setVisibility(View.VISIBLE);
    }

    public void onClickStopService(View v) {
        stopService(new Intent(this, MyService.class));
        pbForService.setVisibility(View.INVISIBLE);

    }

    public void onClickStartServices(View v) {
        Intent intent;

        // Создаем Intent для вызова сервиса,
        // кладем туда параметр времени и код задачи
        intent = new Intent(this, MyService2.class).putExtra(PARAM_TIME, 7)
                .putExtra(PARAM_TASK, TASK1_CODE);
        // стартуем сервис
        startService(intent);

        intent = new Intent(this, MyService2.class).putExtra(PARAM_TIME, 4)
                .putExtra(PARAM_TASK, TASK2_CODE);
        startService(intent);

        intent = new Intent(this, MyService2.class).putExtra(PARAM_TIME, 6)
                .putExtra(PARAM_TASK, TASK3_CODE);
        startService(intent);
    }
}
