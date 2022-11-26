package com.elexlab.neckcare.phonemonitor;

import android.text.TextUtils;

import com.elexlab.neckcare.NeckCareApplication;
import com.elexlab.neckcare.datasource.DataSourceCallback;
import com.elexlab.neckcare.datasource.WriteListDataSource;
import com.elexlab.neckcare.datasource.models.App;
import com.elexlab.neckcare.misc.EasyLog;
import com.elexlab.neckcare.utils.AppUtils;

import java.util.List;

public class SuspendState extends BaseState{
    private final static String TAG = SuspendState.class.getSimpleName();
    private long startTime;
    private int checkStep = 1;
    private long suspendDuration = 30;//30s default

    public SuspendState setSuspendDuration(long suspendDuration) {
        this.suspendDuration = suspendDuration;
        return this;
    }

    @Override
    public void enter(UserAvatar userAvatar) {
        EasyLog.d(TAG,"enter SuspendState");
        startTime = System.currentTimeMillis();
    }

    @Override
    public void exit(UserAvatar userAvatar) {
        EasyLog.d(TAG,"exit SuspendState");
    }

    @Override
    public void execute(UserAvatar userAvatar) {
        long nowTime = System.currentTimeMillis();
        long pastTime = (nowTime-startTime)/1000;
        EasyLog.v(TAG,"SuspendState past time:"+pastTime+"s");

        if(pastTime>suspendDuration*checkStep){//check every duration passed
            EasyLog.d(TAG,"check if exit,checkStep:"+checkStep);
            checkStep ++;//nextStep
            new WriteListDataSource(NeckCareApplication.getContext()).getDatas(new DataSourceCallback<List<App>>() {
                @Override
                public void onSuccess(List<App> apps, String... extraParams) {
                    String currentAppPackageName = AppUtils.getCurrentAppPackageName();
                    if(!TextUtils.isEmpty(currentAppPackageName)){
                        boolean isExit = true;
                        for(App app:apps){
                            if(currentAppPackageName.equals(app.getPackageName())){
                                isExit = false;
                                break;
                            }
                        }
                        if (isExit) {
                            userAvatar.getFSM().revertToPreviousState();
                        }
                    }
                }

                @Override
                public void onFailure(String errMsg, int code) {

                }
            },null,App.class);
        }
    }

    @Override
    public boolean onMessage(UserAvatar userAvatar, Telegram telegram) {
        return false;
    }
}
