package com.elexlab.neckcare.phonemonitor;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;

import com.elexlab.neckcare.NeckCareApplication;
import com.elexlab.neckcare.exercise.Exercise;
import com.elexlab.neckcare.managers.ScreenStatusManager;
import com.elexlab.neckcare.misc.EasyLog;
import com.elexlab.neckcare.ui.activities.ExerciseActivity;

import static android.content.Context.ACTIVITY_SERVICE;

public class ComputerExerciseState extends BaseState{
    private final static String TAG = ComputerExerciseState.class.getSimpleName();

    @Override
    public void enter(UserAvatar userAvatar) {

    }

    @Override
    public void exit(UserAvatar userAvatar) {

    }

    @Override
    public void execute(UserAvatar userAvatar) {
        if(ScreenStatusManager.getInstance().getScreenState() == ScreenStatusManager.ScreenState.SCREEN_OFF){
            userAvatar.getFSM().changeState(new AlertState());
            return;
        }
        ActivityManager am =  (android.app.ActivityManager) NeckCareApplication.context.getApplicationContext().getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        cn.getClassName();
        cn.getPackageName();
        EasyLog.d(TAG,cn.getClassName());

        EasyLog.v(TAG,"ComputerExerciseState");

        if(!"com.elexlab.neckcare.ui.activities.ExerciseActivity".equals(cn.getClassName())){
            Intent intent = new Intent(NeckCareApplication.context, ExerciseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            NeckCareApplication.context.startActivity(intent);
        }
    }

    @Override
    public boolean onMessage(UserAvatar userAvatar, Telegram telegram) {
        if(telegram.getMsgType() == Telegram.MsgType.SUSPEND){
            SuspendState state = new SuspendState();
            if(telegram.getExtraInfo() != null && telegram.getExtraInfo().containsKey("duration")){
                long duration = (long) telegram.getExtraInfo().get("duration");
                state.setSuspendDuration(duration);
            }
            userAvatar.getFSM().changeState(state);
            return true;
        }
        if(telegram.getMsgType() == Telegram.MsgType.EXERCISE_COMPLETE){
            userAvatar.getFSM().changeState(new UsingState());
            return true;
        }
        return false;
    }
}
