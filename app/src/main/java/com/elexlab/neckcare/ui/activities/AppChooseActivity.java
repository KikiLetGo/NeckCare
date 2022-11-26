package com.elexlab.neckcare.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.Nullable;

import com.elexlab.neckcare.R;
import com.elexlab.neckcare.datasource.DataSourceCallback;
import com.elexlab.neckcare.datasource.WriteListDataSource;
import com.elexlab.neckcare.datasource.models.App;
import com.elexlab.neckcare.ui.adapters.WriteListAdapter;
import com.elexlab.neckcare.utils.AppUtils;

import java.util.List;

public class AppChooseActivity extends Activity {
    public static void startActivity(Activity activity, int requestCode){
        Intent intent = new Intent(activity, AppChooseActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }
    public static void startActivity(Context context){
        Intent intent = new Intent(context, AppChooseActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_app_choose);
        GridView gvApps = findViewById(R.id.gvApps);


        List<App> apps = AppUtils.getAllAppInfo(this,false);
        WriteListAdapter writeListAdapter = new WriteListAdapter(this,apps);
        gvApps.setAdapter(writeListAdapter);

        gvApps.setOnItemClickListener((parent, view, position, id) -> {
            App app = apps.get(position);
            new WriteListDataSource(this).addData(app, new DataSourceCallback<App>() {
                @Override
                public void onSuccess(App app, String... extraParams) {
                    runOnUiThread(()->{finish();});
                }

                @Override
                public void onFailure(String errMsg, int code) {

                }
            });
        });
    }
}
