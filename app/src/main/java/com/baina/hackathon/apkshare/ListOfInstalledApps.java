package com.baina.hackathon.apkshare;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baina.hackathon.apkFinder.ApkFinder;
import com.baina.hackathon.apkFinder.AppInfo;
import com.baina.hackathon.apkFinder.AppInfoAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOfInstalledApps extends AppCompatActivity implements DialogInputAPInfo.NoticeDialogListener {

    private Activity current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_installed_apps);

        current = this;

        // Loading all apps
        ProgressDialog progress = new ProgressDialog(current);
        progress.setMessage("Loading...");
        new LaunchLoading(progress).execute();
    }

    /**
     * Display all installed apps on device.
     * @param adapter
     */
    private void showList(AppInfoAdapter adapter) {
        ListView listView = (ListView) findViewById(R.id.installedApkList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            private android.app.FragmentManager fm = current.getFragmentManager();

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long arg3) {
                // Get AP info
                DialogInputAPInfo apInfoDialog = new DialogInputAPInfo();

                Map<String, Object> cargo = new HashMap<>();
                cargo.put("Context", current);
                cargo.put("AdapterView", adapterView);
                cargo.put("Integer", new Integer(i));
                apInfoDialog.passObjects(cargo);

                apInfoDialog.show(fm, "string");
            }}
        );
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Map<String, Object> cargo = ((DialogInputAPInfo)dialog).getObjects();

        // Get app info
        AppInfoAdapter ada = (AppInfoAdapter) ((AdapterView)cargo.get("AdapterView")).getAdapter();
        List<AppInfo> la = ada.getAppInfos();
        AppInfo app = la.get(((Integer)cargo.get("Integer")).intValue());

        // Doing in background
        ProgressDialog progress = new ProgressDialog(current);
        progress.setMessage("Loading...");
        new CopyTask(progress).execute(app, dialog);
    }

    /**
     * Loading all apps on device may take a while, hence doing in background.
     */
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

    /**
     * Extracting apk & copying to sd card may take a while, hence doing in background.
     */
    private class CopyTask extends AsyncTask<Object, Integer, Bundle> {

        private ProgressDialog progress = null;

        public CopyTask(ProgressDialog progress) {
            this.progress = progress;
        }

        public void onPreExecute() {
            progress.show();
        }

        public Bundle doInBackground(Object... params) {
            AppInfo app = (AppInfo)params[0];
            DialogInputAPInfo dialog = (DialogInputAPInfo)params[1];

            String pkgPath = ApkFinder.copyAppToSdcard(current, app);
            Bundle extras = new Bundle();

            extras.putString(MainActivity.INTENT_KEY_APK_URI, pkgPath);
            extras.putString(MainActivity.INTENT_KEY_SSID, dialog.getSSID());
            extras.putString(MainActivity.INTENT_KEY_PASSWORD, dialog.getPassword());
            return extras;
        }

        protected void onProgressUpdate(Integer... values) {

        }

        public void onPostExecute(Bundle result) {
            // Pass path to QRCode Activity
            Intent intent = new Intent(current, QRCodeActivity.class);
            intent.putExtras(result);

            startActivity(intent);

            progress.dismiss();
        }
    }
}
