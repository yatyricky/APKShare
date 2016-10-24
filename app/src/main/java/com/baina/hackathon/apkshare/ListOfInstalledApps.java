package com.baina.hackathon.apkshare;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.baina.hackathon.apkFinder.ApkFinder;
import com.baina.hackathon.apkFinder.AppInfoAdapter;

public class ListOfInstalledApps extends AppCompatActivity {

    private Activity current;

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

        current = this;

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
                Intent intent = new Intent(current, QRCodeActivity.class);
                intent.putExtra(MainActivity.INTENT_KEY_APK_URI, "hahah");
                startActivity(intent);
            }}
        );
    }
}
