package com.baina.hackathon.apkFinder;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * Created by paili on 16/10/23.
 */

public class AppInfo {

    private String appLabel;    //应用程序标签
    private Drawable appIcon ;  //应用程序图像
    private String pkgName ;    //应用程序所对应的包名
    private String pkgPath;     //应用程序的安装路径

    public AppInfo(){}

    public AppInfo(ApplicationInfo app, PackageManager pm) {
        setAppLabel((String) app.loadLabel(pm));
        setPkgName(app.packageName);
        setAppIcon(app.loadIcon(pm));
        setPkgPath(app.publicSourceDir);
    }

    public String getAppLabel() {
        return appLabel;
    }
    public void setAppLabel(String appName) {
        this.appLabel = appName;
    }
    public Drawable getAppIcon() {
        return appIcon;
    }
    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
    public String getPkgName(){
        return pkgName ;
    }
    public void setPkgName(String pkgName){
        this.pkgName=pkgName ;
    }
    public String getPkgPath(){
        return pkgPath ;
    }
    public void setPkgPath(String pkgPath){
        this.pkgPath=pkgPath ;
    }
}
