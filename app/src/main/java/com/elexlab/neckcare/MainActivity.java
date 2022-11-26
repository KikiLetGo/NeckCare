package com.elexlab.neckcare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elexlab.neckcare.datasource.DataSourceCallback;
import com.elexlab.neckcare.datasource.WriteListDataSource;
import com.elexlab.neckcare.datasource.models.App;
import com.elexlab.neckcare.misc.EasyLog;
import com.elexlab.neckcare.phonemonitor.ExerciseState;
import com.elexlab.neckcare.phonemonitor.UserAvatar;
import com.elexlab.neckcare.pojos.Setting;
import com.elexlab.neckcare.service.KeepRunningService;
import com.elexlab.neckcare.ui.activities.AppChooseActivity;
import com.elexlab.neckcare.ui.activities.ExerciseActivity;
import com.elexlab.neckcare.ui.adapters.WriteListAdapter;
import com.elexlab.neckcare.utils.AppUtils;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.push.HmsMessaging;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private SeekBar sbExercisePeriod;
    private TextView tvExercisePeriod;
    private SeekBar sbRepeats;
    private TextView tvRepeats;
    private SeekBar sbForceLevel;
    private TextView tvForceLevel;
    private GridView gvWhiteList;

    private final int CHOOSE_APP_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(! isIgnoringBatteryOptimizations()){
            requestIgnoreBatteryOptimizations();
        }

        startForegroundService(new Intent(this, KeepRunningService.class));

        requestDrawOverLays();

        usageStatsPermissionCheck();

        initHMSCorePush();

    }

    private boolean isIgnoringBatteryOptimizations() {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(getPackageName());
        }
        return isIgnoring;
    }

    private void requestIgnoreBatteryOptimizations() {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestDrawOverLays() {
        if (!Settings.canDrawOverlays(this)) {
            //没有权限，给出提示，并跳转界面然用户enable
            Toast.makeText(NeckCareApplication.context, "请先赋予应用悬浮窗权限", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 2);
        }
    }



    private void loadWriteList(){
        new WriteListDataSource(this).getDatas(new DataSourceCallback<List<App>>() {
            @Override
            public void onSuccess(List<App> apps, String... extraParams) {
                runOnUiThread(()->{
                    WriteListAdapter writeListAdapter = new WriteListAdapter(MainActivity.this,apps);
                    gvWhiteList.setAdapter(writeListAdapter);
                });
            }

            @Override
            public void onFailure(String errMsg, int code) {

            }
        }, null, App.class);
    }


    public void setSettings(Setting settings){
        SharedPreferences sharedPreferences = getSharedPreferences("NeckCare_Setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("exercisePeriod", settings.getExercisePeriod());
        editor.putInt("repeats", settings.getRepeats());
        editor.putInt("forceLevel", settings.getForceLevel());
        editor.apply();
    }

    public Setting getSettings(){
        Setting setting = new Setting();
        SharedPreferences sharedPreferences = getSharedPreferences("NeckCare_Setting", Context.MODE_PRIVATE);
        setting.setExercisePeriod(sharedPreferences.getInt("exercisePeriod", 10));
        setting.setRepeats(sharedPreferences.getInt("repeats", 50));
        setting.setRepeats(sharedPreferences.getInt("forceLevel", 100));
        return setting;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(CHOOSE_APP_CODE == requestCode){
            loadWriteList();

        }
    }

    private boolean usageStatsPermissionCheck() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        final AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        requestReadNetworkStats();
        return false;
    }

    // 打开“有权查看使用情况的应用”页面
    private void requestReadNetworkStats() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }

    private void initHMSCorePush(){
        // 设置HMS自动初始化(Token)
        HmsMessaging.getInstance(this).setAutoInitEnabled(true);
        try {
            // 主题订阅
            HmsMessaging.getInstance(this).subscribe("exercise")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            // 获取主题订阅的结果
                            if (task.isSuccessful()) {
                                EasyLog.i(TAG, "subscribe topic successfully");
                            } else {
                                EasyLog.e(TAG, "subscribe topic failed, return value is " + task.getException().getMessage());
                            }
                        }
                    });
        } catch (Exception e) {
            EasyLog.e(TAG, "subscribe failed, catch exception : " + e.getMessage());
        }
    }
}