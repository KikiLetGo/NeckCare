package com.elexlab.neckcare;

import android.app.Application;
import android.content.Context;

import com.elexlab.neckcare.misc.EasyLog;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.push.HmsMessaging;
import com.igexin.sdk.GetuiPushException;
import com.igexin.sdk.IUserLoggerInterface;
import com.igexin.sdk.PushManager;

public class NeckCareApplication extends Application {
    private final static String TAG = NeckCareApplication.class.getSimpleName();
    public static Context context;
    public NeckCareApplication() {
        context = this;
    }
    public static Context getContext(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        com.igexin.sdk.PushManager.getInstance().initialize(this);
        com.igexin.sdk.PushManager.getInstance().setDebugLogger(this, new IUserLoggerInterface() {
            @Override
            public void log(String s) {
                EasyLog.i("PUSH_LOG",s);
            }
        });
        try {
            PushManager.getInstance().checkManifest(this);
        } catch (Exception | GetuiPushException e) {
            e.printStackTrace();
        }



    }


}
