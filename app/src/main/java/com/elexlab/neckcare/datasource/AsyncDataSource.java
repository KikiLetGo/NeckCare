package com.elexlab.neckcare.datasource;

import java.util.List;

public abstract class AsyncDataSource <T>{

    private final static String TAG = AsyncDataSource.class.getSimpleName();

    public abstract void addData(T t,DataSourceCallback<T> dataSourceCallback);
    public abstract void addData(DataCondition dataCondition,DataSourceCallback<T> dataSourceCallback);

    public abstract void deleteData(T t,DataSourceCallback<T> dataSourceCallback);
    public abstract void deleteData(DataCondition dataCondition,DataSourceCallback<T> dataSourceCallback);

    public abstract void updateData(T t,DataSourceCallback<T> dataSourceCallback);
    public abstract void updateData(DataCondition dataCondition,DataSourceCallback<T> dataSourceCallback);

    public abstract void getData(DataSourceCallback<T> dataSourceCallback,DataCondition dataCondition,Class<T> clazz);
    public abstract void getDatas(DataSourceCallback<List<T>>  dataSourceCallback, DataCondition dataCondition, Class<T> clazz);

    public abstract void cancelRequest(String tag);
    public abstract void cancelAllRequest();
}
