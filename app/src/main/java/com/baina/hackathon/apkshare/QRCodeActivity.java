package com.baina.hackathon.apkshare;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baina.hackathon.httpServer.http.TransferServer;
import com.baina.hackathon.wifiControl.WifiApAdmin;

import org.nanohttpd.util.ServerRunner;

public class QRCodeActivity extends AppCompatActivity {
    private WifiApAdmin wifiApAdmin;
    private String pkgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString(MainActivity.INTENT_KEY_APK_URI);
        String ssid = bundle.getString(MainActivity.INTENT_KEY_SSID);
        String password = bundle.getString(MainActivity.INTENT_KEY_PASSWORD);
        wifiApAdmin = WifiApAdmin.getInstance();

        TextView txtView = (TextView) findViewById(R.id.textViewQRCode);
        message = "http://" + wifiApAdmin.getGateway(getBaseContext()) + ":8080/" + message;
        txtView.setText(message);

        TextView textViewssid = (TextView) findViewById(R.id.textViewSSID);
        textViewssid.setText(ssid);

        TextView textViewPassword = (TextView) findViewById(R.id.textViewPassword);
        textViewPassword.setText(password);

        pkgPath = message;
        enableServer(ssid, password);
    }

    private void showQRCode(String data) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = (int)(displaymetrics.widthPixels * 0.8);

        Bitmap bitmap = QRBuilder.buildQRBitmap(data, width, width);
        if (bitmap != null) {
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "Generating QR code failed.", Toast.LENGTH_LONG);
        }
    }



    private void enableServer(String ssid, String password) {
        WifiApAdmin.StartWifiApListener wifiApListener = new  WifiApAdmin.StartWifiApListener() {

            @Override
            public void onEvent(WifiApAdmin.WifiResult status) {
                switch(status) {
                    // Hotspot enabled
                    case WIFI_START:
                        TransferServer server = new TransferServer(8080);
                        server.setRootDir(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getPackageName());
                        ServerRunner.executeInstance(server);

                        runThread();

                        break;
                    case WIFI_FAIL:
                        break;
                }
            }

            private void runThread() {

                new Thread() {
                    public void run() {
                        try {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    String uri = "http://" + wifiApAdmin.getGateway(getBaseContext()) + ":8080/" + pkgPath;
                                    showQRCode(uri);
                                }
                            });
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        };

        if (this.wifiApAdmin.isWifiApEnabled(this)) {
            this.wifiApAdmin.closeWifiAp(this);
        }
        this.wifiApAdmin.startWifiAp(getBaseContext(), ssid, password, wifiApListener);
    }
}
