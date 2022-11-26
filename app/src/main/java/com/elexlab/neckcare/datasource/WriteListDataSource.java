package com.elexlab.neckcare.datasource;

import android.content.Context;

import com.elexlab.neckcare.datasource.models.App;
import com.elexlab.neckcare.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class WriteListDataSource extends DBDataSource<App>{
    private static List<App> caches = new ArrayList<>();
    public WriteListDataSource(Context context) {
        super(context);
    }

    @Override
    public void addData(App app, DataSourceCallback<App> dataSourceCallback) {
        super.addData(app, new DataSourceCallback<App>() {
            @Override
            public void onSuccess(App app, String... extraParams) {
                caches.add(app);
                if(dataSourceCallback != null){
                    dataSourceCallback.onSuccess(app, extraParams);
                }
            }

            @Override
            public void onFailure(String errMsg, int code) {
                if(dataSourceCallback != null){
                    dataSourceCallback.onFailure(errMsg, code);
                }
            }
        });
    }

    @Override
    public void getDatas(DataSourceCallback<List<App>> dataSourceCallback, DataCondition dataCondition, Class<App> clazz) {
        if(!caches.isEmpty()){
            if(dataSourceCallback != null){
                dataSourceCallback.onSuccess(caches);
            }
            return;
        }
        super.getDatas(new DataSourceCallback<List<App>>() {
            @Override
            public void onSuccess(List<App> apps, String... extraParams) {
                for(App app:apps){
                    app.setIcon(AppUtils.getDrawableByPackageName(context,app.getPackageName()));
                }
                caches = apps;
                if(dataSourceCallback != null){
                    dataSourceCallback.onSuccess(apps, extraParams);
                }
            }

            @Override
            public void onFailure(String errMsg, int code) {
                if(dataSourceCallback != null){
                    dataSourceCallback.onFailure(errMsg, code);
                }
            }
        }, dataCondition, clazz);
    }
}
