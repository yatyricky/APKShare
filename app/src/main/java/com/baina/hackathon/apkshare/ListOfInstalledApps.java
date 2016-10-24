package com.baina.hackathon.apkshare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baina.hackathon.apkFinder.ApkFinder;
import com.baina.hackathon.apkFinder.AppInfo;
import com.baina.hackathon.apkFinder.AppInfoAdapter;

import java.util.List;

public class ListOfInstalledApps extends AppCompatActivity {

    private Activity current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_installed_apps);

        current = this;

        ProgressDialog progress = new ProgressDialog(current);
        progress.setMessage("Loading...");
        new LaunchLoading(progress).execute();

    }

    private void showList(AppInfoAdapter adapter) {
        ListView listView = (ListView) findViewById(R.id.installedApkList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long arg3) {
                // Extract APK to SD card
                AppInfoAdapter ada = (AppInfoAdapter) adapterView.getAdapter();
                List<AppInfo> la = ada.getAppInfos();
                AppInfo app = la.get(i);
                String pkgPath = ApkFinder.copyAppToSdcard(current, app);

                // Pass path to QRCode Activity
                Intent intent = new Intent(current, QRCodeActivity.class);
                intent.putExtra(MainActivity.INTENT_KEY_APK_URI, pkgPath);
                startActivity(intent);
            }}
        );
    }

    private class LaunchLoading extends AsyncTask<Void, Void, AppInfoAdapter> {

        private ProgressDialog progress = null;

        public LaunchLoading(ProgressDialog progress) {
            this.progress = progress;
        }

        public void onPreExecute() {
            progress.show();

        }

        public AppInfoAdapter doInBackground(Void... params) {
            return new AppInfoAdapter(current, ApkFinder.queryAppInfo(current));
        }

        protected void onProgressUpdate(Void... values) {

        }

        public void onPostExecute(AppInfoAdapter result) {
//            Toast.makeText(current, "Done", Toast.LENGTH_LONG);
            progress.dismiss();
            showList(result);
        }
    }
}
