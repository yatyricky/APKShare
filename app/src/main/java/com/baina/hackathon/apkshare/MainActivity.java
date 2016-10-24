package com.baina.hackathon.apkshare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baina.hackathon.httpServer.NHttpFileServer;
import com.baina.hackathon.wifiControl.WifiApAdmin;
import com.baina.hackathon.wifiControl.WifiApControl;

import android.widget.ListView;
import com.baina.hackathon.apkFinder.ApkFinder;
import com.baina.hackathon.apkFinder.AppInfoAdapter;
import com.baina.hackathon.wifiControl.WifiApControl;

public class MainActivity extends AppCompatActivity {

    private final static int QR_WIDTH = 200;
    private final static int QR_HEIGHT = 200;
    public final static String INTENT_KEY_APK_URI = "apk_uri";

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

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
                WifiApControl.openWifiAp(getBaseContext(), "test-hot-pot", "123456789");
            }
        });

        Button btnStartServer = (Button) findViewById(R.id.httpServerBtn);
        btnStartServer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    NHttpFileServer.startServer(this.getClass().getResource("/").toString(), 8080);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                TextView textView = (TextView) findViewById(R.id.ipText);
                String ipAddress = WifiApAdmin.getWifiApIpAddress();
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
    }

    public void startLauncherActivity(View view) {
        Intent intent = new Intent(this, ListOfInstalledApps.class);
        startActivity(intent);
    }

}
