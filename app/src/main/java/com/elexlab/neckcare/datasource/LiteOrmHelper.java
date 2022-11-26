package com.elexlab.neckcare.datasource;

import android.content.Context;

import com.litesuits.orm.LiteOrm;

/**
 * Created by Young on 2016/12/15.
 */
public class LiteOrmHelper {
    private static final String DB_NAME = "neck_care.db";

    private static volatile LiteOrm sInstance;

    private LiteOrmHelper() {
        // Avoid direct instantiate
    }

    public static LiteOrm getLiteOrmInstance(Context context) {
        if (sInstance == null) {
            synchronized (LiteOrmHelper.class) {
                if (sInstance == null) {
                    sInstance = LiteOrm.newCascadeInstance(context, DB_NAME);
                    sInstance.setDebugged(true);
                }
            }
        }
        return sInstance;
    }
}
