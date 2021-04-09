package com.cojigae.coji.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;

import com.cojigae.coji.receiver.AlarmReceiver;
import com.cojigae.coji.receiver.DeviceBootReceiver;

import java.util.Calendar;

public class NotificationService extends Service {
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPreferences = getSharedPreferences("pref", Activity.MODE_PRIVATE);

        if(intent == null)
            return Service.START_STICKY;
        else {
            if (sharedPreferences != null && sharedPreferences.contains("isSwitchOn")) {
                boolean isOnOff = sharedPreferences.getBoolean("isSwitchOn", false);
                setNotification(isOnOff);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void setNotification(boolean b) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance()))
            calendar.add(Calendar.DATE, 1);

        SharedPreferences.Editor editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
        editor.putLong("nextNotifyTime", (long)calendar.getTimeInMillis());
        editor.apply();

        dailyNotification(calendar, b);
    }

    private void dailyNotification(Calendar calendar, boolean dailyNotify) {
        PackageManager pm = this.getPackageManager();
        ComponentName receiver = new ComponentName(getApplicationContext(), DeviceBootReceiver.class);
        Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if(dailyNotify){
            if (alarmManager != null) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
            pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        } else
            alarmManager.cancel(pendingIntent);
    }
}