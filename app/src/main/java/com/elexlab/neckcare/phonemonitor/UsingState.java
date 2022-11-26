package com.elexlab.neckcare.phonemonitor;

import android.text.TextUtils;

import com.elexlab.neckcare.NeckCareApplication;
import com.elexlab.neckcare.datasource.DataSourceCallback;
import com.elexlab.neckcare.datasource.WriteListDataSource;
import com.elexlab.neckcare.datasource.models.App;
import com.elexlab.neckcare.exercise.Exercise;
import com.elexlab.neckcare.managers.ScreenStatusManager;
import com.elexlab.neckcare.misc.EasyLog;
import com.elexlab.neckcare.utils.AppUtils;

import java.util.List;

public class UsingState extends BaseState{
    private final static String TAG = UsingState.class.getSimpleName();

    private long startTime;

    @Override
    public void enter(UserAvatar userAvatar) {
        EasyLog.d(TAG,"enter UsingState");
        startTime = System.currentTimeMillis();

    }

    @Override
    public void exit(UserAvatar userAvatar) {
        EasyLog.d(TAG,"exit UsingState");
        long endTime = System.currentTimeMillis();
        long usingTime = endTime - startTime;
        userAvatar.setUsingTime(userAvatar.getUsingTime()+usingTime);
    }

    @Override
    public void execute(UserAvatar userAvatar) {

        long nowTime = System.currentTimeMillis();
        long usingTime = nowTime - startTime;

        long userUsingTime = userAvatar.getUsingTime()+usingTime;

        EasyLog.v(TAG,"user using time:"+userUsingTime);

        if(userUsingTime >= Exercise.EXERCISE_PERIOD){

            new WriteListDataSource(NeckCareApplication.getContext()).getDatas(new DataSourceCallback<List<App>>() {
                @Override
                public void onSuccess(List<App> apps, String... extraParams) {
                    String currentAppPackageName = AppUtils.getCurrentAppPackageName();
                    if(!TextUtils.isEmpty(currentAppPackageName)){
                        for(App app:apps){
                            if(currentAppPackageName.equals(app.getPackageName())){
                                EasyLog.d(TAG,"current using app in whitelist,pkgName:"+app.getPackageName());
                                userAvatar.getFSM().changeState(new SuspendState());
                                return;
                            }
                        }
                    }
                    userAvatar.getFSM().changeState(new ExerciseState());
                }

                @Override
                public void onFailure(String errMsg, int code) {

                }
            },null,App.class);
            return;
        }
        if(ScreenStatusManager.getInstance().getScreenState() == ScreenStatusManager.ScreenState.SCREEN_OFF){
            userAvatar.getFSM().changeState(RestState.getInstance());
        }
    }

    @Override
    public boolean onMessage(UserAvatar userAvatar, Telegram telegram) {
        return false;
    }
}
