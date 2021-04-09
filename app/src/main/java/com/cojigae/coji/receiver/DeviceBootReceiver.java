package com.cojigae.coji.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.cojigae.coji.service.NotificationService;

import java.util.Calendar;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {

            // on device boot complete, reset the alarm
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            SharedPreferences sharedPreferences = context.getSharedPreferences("daily alarm", MODE_PRIVATE);
            long millis = sharedPreferences.getLong("nextNotifyTime", Calendar.getInstance().getTimeInMillis());


            Calendar current_calendar = Calendar.getInstance();
            Calendar nextNotifyTime = Calendar.getInstance();
            nextNotifyTime.setTimeInMillis(sharedPreferences.getLong("nextNotifyTime", millis));

            if (current_calendar.after(nextNotifyTime))
                nextNotifyTime.add(Calendar.DATE, 1);

            if (manager != null) {
                manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, nextNotifyTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                manager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextNotifyTime.getTimeInMillis(), pendingIntent);
            }

            Intent serviceIntent = new Intent(context.getApplicationContext(), NotificationService.class);
            context.startService(serviceIntent);
        }
    }
}