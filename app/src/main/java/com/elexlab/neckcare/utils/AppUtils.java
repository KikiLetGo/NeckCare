package com.elexlab.neckcare.utils;

import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.elexlab.neckcare.NeckCareApplication;
import com.elexlab.neckcare.datasource.models.App;
import com.elexlab.neckcare.misc.EasyLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeMap;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.USAGE_STATS_SERVICE;

public class AppUtils {

    /**
     * 获取手机已安装应用列表
     * @param ctx
     * @param isFilterSystem 是否过滤系统应用
     * @return
     */
    public static ArrayList<App> getAllAppInfo(Context ctx, boolean isFilterSystem) {
        ArrayList<App> appBeanList = new ArrayList<>();
        App app = null;

        PackageManager packageManager = ctx.getPackageManager();
        List<PackageInfo> list = packageManager.getInstalledPackages(0);
        for (PackageInfo p : list) {
            app = new App();
            app.setIcon(p.applicationInfo.loadIcon(packageManager));
            app.setName(packageManager.getApplicationLabel(p.applicationInfo).toString());
            app.setPackageName(p.applicationInfo.packageName);
            int flags = p.applicationInfo.flags;
            // 判断是否是属于系统的apk
            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0 && isFilterSystem) {
//                bean.setSystem(true);
            } else {
                appBeanList.add(app);
            }
        }
        return appBeanList;
    }

    public static Drawable getDrawableByPackageName(Context context, String packageName){
        try {
            Drawable drawable = context.getPackageManager().getApplicationIcon(packageName);
            return drawable;
        }catch (PackageManager.NameNotFoundException nameNotFoundException){
            return null;
        }
    }

    public static String getCurrentAppPackageName(){
        long time = System.currentTimeMillis();
        ComponentName topComponentName = null;
        UsageStatsManager usm = (UsageStatsManager) NeckCareApplication.context.getSystemService(USAGE_STATS_SERVICE);

        UsageEvents usageEvents = usm.queryEvents(time - 60 * 60 * 1000, time);

        UsageEvents.Event out;

        TreeMap<Long, UsageEvents.Event> map = new TreeMap();

        if (usageEvents != null) {

            while (usageEvents.hasNextEvent()) {

                out = new UsageEvents.Event();

                if (usageEvents.getNextEvent(out)) {

                    if (out != null) {

                        map.put(out.getTimeStamp(),out);

                    } else {


                    }

                } else {


                }

            }

            if (!map.isEmpty()) {

                //将keyset颠倒过来，让最近的排列在上面

                NavigableSet  keySets = map.navigableKeySet();

                Iterator iterator = keySets.descendingIterator();

                while (iterator.hasNext()) {

                    UsageEvents.Event event = map.get(iterator.next());

                    if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {

                        //当遇到有app移动到前台时，就更新topComponentName

                        topComponentName = new ComponentName(event.getPackageName(), "");

                        break;

                    }

                }

            }

        } else {


        }

        return topComponentName.getPackageName();



//        ActivityManager am =  (android.app.ActivityManager) NeckCareApplication.context.getApplicationContext().getSystemService(ACTIVITY_SERVICE);
//        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
//        cn.getClassName();
//        cn.getPackageName();
//        return cn.getPackageName();
    }
}
