package com.baina.hackathon.apkshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.baina.hackathon.apkFinder.ApkFinder;
import com.baina.hackathon.apkFinder.AppInfoAdapter;

public class ListOfInstalledApps extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_installed_apps);

        showList();
    }

    private void showList() {
        AppInfoAdapter a = new AppInfoAdapter(getApplicationContext(), ApkFinder.queryAppInfo(getApplicationContext()));
        ListView listView = (ListView) findViewById(R.id.installedApkList);
        listView.setAdapter(a);
    }
}
