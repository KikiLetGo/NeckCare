package com.elexlab.neckcare.phonemonitor;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;

import com.elexlab.neckcare.NeckCareApplication;
import com.elexlab.neckcare.managers.ScreenStatusManager;

public class AlertState extends BaseState{
    @Override
    public void enter(UserAvatar userAvatar) {
        if(ScreenStatusManager.getInstance().getScreenState() == ScreenStatusManager.ScreenState.SCREEN_OFF){
            ScreenStatusManager.getInstance().acquireWakeLock(NeckCareApplication.getContext(),1000*5);
        }
        Vibrator vibrator = (Vibrator) NeckCareApplication.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        new Handler().postDelayed(()->{
            vibrator.vibrate(400);
        },1000);
    }

    @Override
    public void exit(UserAvatar userAvatar) {

    }

    @Override
    public void execute(UserAvatar userAvatar) {
        if(ScreenStatusManager.getInstance().getScreenState() == ScreenStatusManager.ScreenState.SCREEN_UNLOCKED){
            userAvatar.getFSM().revertToPreviousState();
            return;
        }




    }

    @Override
    public boolean onMessage(UserAvatar userAvatar, Telegram telegram) {
        return false;
    }
}
