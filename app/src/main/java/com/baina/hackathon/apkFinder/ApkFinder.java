package com.baina.hackathon.apkFinder;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by paili on 16/10/23.
 */

public class ApkFinder {

    // 获得所有启动Activity的信息，类似于Launch界面
    public static List<AppInfo> queryAppInfo(Context context) {
        List<AppInfo> listAppInfo = new ArrayList<>();
        PackageManager pm = context.getPackageManager(); // 获得PackageManager对象
        // 通过查询，获得所有ResolveInfo对象.
        List<ApplicationInfo> listAppcations = pm
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(listAppcations,new ApplicationInfo.DisplayNameComparator(pm));

        for (ApplicationInfo app : listAppcations) {
            // 只列出第三方应用程序
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {

                // 创建一个AppInfo对象
                AppInfo appInfo = new AppInfo(app, pm);
                listAppInfo.add(appInfo); // 添加至列表中
            }

        }
        return listAppInfo;
    }
}
