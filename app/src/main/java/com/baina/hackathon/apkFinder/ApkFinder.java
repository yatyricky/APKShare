package com.baina.hackathon.apkFinder;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
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

    public static String copyPkgToSdcard(Context context, String pkgPath, String pkgName) {
        String tarPath = "";
        try {
            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                tarPath = sd.toString() + "/" + context.getPackageName() + "/" + pkgName +".apk";
                File curFile = new File(pkgPath);
                File tarFile = new File(tarPath);
                File tarDir = tarFile.getParentFile();
                if (!tarDir.exists()) {
                    tarDir.mkdirs();
                }
                if (!tarFile.exists())
                {
                    tarFile.createNewFile();
                }
                if (curFile.exists()) {
                    FileChannel src = new FileInputStream(curFile)
                            .getChannel();
                    FileChannel dst = new FileOutputStream(tarFile)
                            .getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
        return tarPath;
    }

    public static String copyAppToSdcard(Context context, AppInfo app) {
        String pkgPath = app.getPkgPath();
        String pkgName = app.getAppLabel();
        return copyPkgToSdcard(context, pkgPath, pkgName);
    }
}
