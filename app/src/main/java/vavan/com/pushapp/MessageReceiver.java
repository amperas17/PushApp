package vavan.com.pushapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MessageReceiver extends BroadcastReceiver {
    public static final String MY_EXTRA = "com.vavan.broadcast.Message";

    public MessageReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Обнаружено сообщение: "
                        + intent.getStringExtra(MY_EXTRA),
                        Toast.LENGTH_LONG).show();
    }
}
