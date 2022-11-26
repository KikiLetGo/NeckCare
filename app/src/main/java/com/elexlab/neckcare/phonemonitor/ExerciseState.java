package com.elexlab.neckcare.phonemonitor;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.text.TextUtils;

import com.elexlab.neckcare.NeckCareApplication;
import com.elexlab.neckcare.datasource.DataSourceCallback;
import com.elexlab.neckcare.datasource.WriteListDataSource;
import com.elexlab.neckcare.datasource.models.App;
import com.elexlab.neckcare.exercise.Exercise;
import com.elexlab.neckcare.misc.EasyLog;
import com.elexlab.neckcare.ui.activities.ExerciseActivity;
import com.elexlab.neckcare.utils.AppUtils;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public class ExerciseState extends BaseState {
    private final static String TAG = ExerciseState.class.getSimpleName();
    @Override
    public void enter(UserAvatar userAvatar) {
        EasyLog.d(TAG,"enter ExerciseState");

    }

    @Override
    public void exit(UserAvatar userAvatar) {
        EasyLog.d(TAG,"exit ExerciseState");
        userAvatar.setUsingTime(0);
    }

    @Override
    public void execute(UserAvatar userAvatar) {
        ActivityManager am =  (android.app.ActivityManager) NeckCareApplication.context.getApplicationContext().getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        cn.getClassName();
        cn.getPackageName();
        EasyLog.d(TAG,cn.getClassName());

        long usingTime = userAvatar.getUsingTime();
        EasyLog.d(TAG,"usingTime:"+usingTime);


        if(!"com.elexlab.neckcare.ui.activities.ExerciseActivity".equals(cn.getClassName())){
            Intent intent = new Intent(NeckCareApplication.context,ExerciseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            NeckCareApplication.context.startActivity(intent);
        }


    }

    @Override
    public boolean onMessage(UserAvatar userAvatar,Telegram telegram) {
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
            UserAvatar.getInstance().setUsingTime(0);
            userAvatar.getFSM().changeState(new UsingState());
            return true;
        }
        return false;
    }
}
