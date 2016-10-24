package com.baina.hackathon.apkshare;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QRCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString(MainActivity.INTENT_KEY_APK_URI);
        String ssid = bundle.getString(MainActivity.INTENT_KEY_SSID);
        String password = bundle.getString(MainActivity.INTENT_KEY_PASSWORD);

        TextView txtView = (TextView) findViewById(R.id.textViewQRCode);
        txtView.setText(message);

        TextView textViewssid = (TextView) findViewById(R.id.textViewSSID);
        textViewssid.setText(ssid);

        TextView textViewPassword = (TextView) findViewById(R.id.textViewPassword);
        textViewPassword.setText(password);

//        WifiApControl.openWifiAp(getBaseContext(), "test-hot-pot", "123456789");
        enableServer();
        showQRCode(message);
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

    private void enableServer() {
    }
}
