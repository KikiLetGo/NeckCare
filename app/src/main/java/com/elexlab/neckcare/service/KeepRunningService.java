package com.elexlab.neckcare.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.elexlab.neckcare.managers.ScreenStatusManager;
import com.elexlab.neckcare.phonemonitor.PhoneStateMachine;
import com.elexlab.neckcare.phonemonitor.UserAvatar;

public class KeepRunningService extends Service {
    private Handler handler = new Handler();

    private UserAvatar userAvatar;
    private static final int SERVICE_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        userAvatar = UserAvatar.getInstance();
        monitorScreen();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.v("KeepRunningService","running");
                handler.postDelayed(this,1000);
                userAvatar.update();
            }
        },1000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (manager != null) {

            NotificationChannel channel = new NotificationChannel("NeckCare","NeckCare", NotificationManager.IMPORTANCE_NONE);

            manager.createNotificationChannel(channel);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"NeckCare");


            startForeground(SERVICE_ID, builder.build());
        }

        return START_STICKY;
    }

    private void monitorScreen(){
        ScreenStatusManager.getInstance().monitorScreen(this);
    }
}
