package com.baina.hackathon.apkshare;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baina.hackathon.apkFinder.AppInfo;
import com.baina.hackathon.httpServer.http.TransferServer;
import com.baina.hackathon.wifiControl.WifiApAdmin;

import android.widget.ListView;
import android.widget.Toast;

import com.baina.hackathon.apkFinder.ApkFinder;
import com.baina.hackathon.apkFinder.AppInfoAdapter;

import org.nanohttpd.util.ServerRunner;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static int QR_WIDTH = 200;
    private final static int QR_HEIGHT = 200;
    public final static String INTENT_KEY_APK_URI = "apk_uri";
    public final static String INTENT_KEY_SSID = "ap_ssid";
    public final static String INTENT_KEY_PASSWORD = "ap_password";
    public final static String INTENT_KEY_APADMIN = "ap_admin";

    private Context context;

    private WifiApAdmin wifiApAdmin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        wifiApAdmin = WifiApAdmin.getInstance();

        Button btn = (Button)findViewById(R.id.dimensionalBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = QRBuilder.buildQRBitmap("hello world", QR_WIDTH, QR_HEIGHT);
                if (bitmap != null)
                {
                    ImageView imageView = (ImageView) findViewById(R.id.bitImg);
                    imageView.setImageBitmap(bitmap);
                }
            }
        });

        Button btnWifiAp = (Button) findViewById(R.id.wifiapBtn);
        btnWifiAp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!wifiApAdmin.isWifiApEnabled(getBaseContext())){
                    wifiApAdmin.startWifiAp(getBaseContext(), Build.MODEL, "123456789", new WifiApAdmin.StartWifiApListener() {
                        @Override
                        public void onEvent(WifiApAdmin.WifiResult status) {
                            switch (status) {
                                case WIFI_START:
                                    break;
                                case WIFI_FAIL:
                                    break;
                            }
                        }
                    });
                }
            }
        });

        Button btnStartServer = (Button) findViewById(R.id.httpServerBtn);
        btnStartServer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TransferServer server = new TransferServer(8080);
                server.setRootDir(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getPackageName());
                ServerRunner.executeInstance(server);
            }
        });

        Button btnShowWifiApIp = (Button) findViewById(R.id.getHostIp);
        btnShowWifiApIp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) findViewById(R.id.ipText);
                String ipAddress = wifiApAdmin.getGateway(getBaseContext());
                textView.setText(ipAddress == null ? "null" : ipAddress);
            }
        });

        Button btnApkFinder = (Button) findViewById(R.id.apkFinderBtn);
        btnApkFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppInfoAdapter a = new AppInfoAdapter(context, ApkFinder.queryAppInfo(context));
                ListView listView = (ListView) findViewById(R.id.apkLst);
                listView.setAdapter(a);
            }
        });

        ListView listView = (ListView) findViewById(R.id.apkLst);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppInfoAdapter ada = (AppInfoAdapter) adapterView.getAdapter();
                List<AppInfo> la = ada.getAppInfos();
                AppInfo app = la.get(i);

                ProgressDialog progress = new ProgressDialog(context);
                progress.setMessage("Loading...");
                new CopyTask(progress).execute(app);
            }
        });
    }

    public void startLauncherActivity(View view) {
        Intent intent = new Intent(this, ListOfInstalledApps.class);
        startActivity(intent);
    }

    public class CopyTask extends AsyncTask<AppInfo, Integer, String> {

        private ProgressDialog progress = null;

        public CopyTask(ProgressDialog progress) {
            this.progress = progress;
        }

        public void onPreExecute() {
            progress.show();
        }

        public String doInBackground(AppInfo... params) {
            AppInfo app = params[0];
            String pkgPath = ApkFinder.copyAppToSdcard(context, app);
            return pkgPath;
        }

        protected void onProgressUpdate(Integer... values) {

        }

        public void onPostExecute(String result) {
            Toast.makeText(context, result, Toast.LENGTH_LONG);
            progress.dismiss();
        }
    }

}
