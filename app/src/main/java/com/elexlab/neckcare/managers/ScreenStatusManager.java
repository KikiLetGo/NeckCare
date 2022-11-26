package com.elexlab.neckcare.managers;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

import com.elexlab.neckcare.misc.EasyLog;
import com.elexlab.neckcare.phonemonitor.ExerciseState;

public class ScreenStatusManager{
    private static ScreenStatusManager instance;
    public static ScreenStatusManager getInstance(){
        if(instance == null){
            instance = new ScreenStatusManager();
        }
        return instance;
    }

    private ScreenState screenState = ScreenState.SCREEN_OFF;

    public ScreenState getScreenState() {
        return screenState;
    }

    public enum ScreenState{
        SCREEN_ON,
        SCREEN_OFF,
        SCREEN_UNLOCKED,
    }
    private static class ScreenStatusReceiver extends BroadcastReceiver{
        private ScreenStatusManager screenStatusManager;
        public ScreenStatusReceiver(ScreenStatusManager screenStatusManager) {
            this.screenStatusManager = screenStatusManager;
        }

        private static final String TAG = ScreenStatusReceiver.class.getSimpleName();
        @Override
        public void onReceive(Context context, Intent intent) {
            if ( "android.intent.action.SCREEN_ON".equals(intent.getAction())) {
                EasyLog.i(TAG,"screen on");
                //screen on 不计入状态
                //screenStatusManager.screenState =ScreenState.SCREEN_ON;
            }else if("android.intent.action.SCREEN_OFF".equals(intent.getAction())){
                EasyLog.i(TAG,"screen off");
                screenStatusManager.screenState =ScreenState.SCREEN_OFF;

            }else if("android.intent.action.USER_PRESENT".equals(intent.getAction())){
                //用户解锁才算使用
                EasyLog.i(TAG,"ACTION_USER_PRESENT");
                screenStatusManager.screenState =ScreenState.SCREEN_UNLOCKED;
            }
        }
    }
    public PowerManager.WakeLock acquireWakeLock(Context context, long timeout) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm == null)
            return null;
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.FULL_WAKE_LOCK |
                        PowerManager.ON_AFTER_RELEASE,
                context.getClass().getName());
        wakeLock.acquire(timeout);
        return wakeLock;
    }
    public void monitorScreen(Context context){
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isInteractive();
        if(!isScreenOn){
            screenState = ScreenState.SCREEN_OFF;
        }else{
            KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

            boolean locked = mKeyguardManager.inKeyguardRestrictedInputMode();
            if(!locked){
                screenState = ScreenState.SCREEN_UNLOCKED;
            }else{
                screenState = ScreenState.SCREEN_OFF;
            }
        }

        ScreenStatusReceiver mScreenStatusReceiver = new ScreenStatusReceiver(this);

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(Intent.ACTION_SCREEN_ON);

        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);

        intentFilter.addAction(Intent.ACTION_USER_PRESENT);

        context.registerReceiver(mScreenStatusReceiver, intentFilter);
    }




}
