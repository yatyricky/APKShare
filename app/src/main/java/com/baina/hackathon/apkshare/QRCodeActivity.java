package com.baina.hackathon.apkshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.baina.hackathon.wifiControl.WifiApControl;

public class QRCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString(MainActivity.INTENT_KEY_APK_URI);

        TextView txtView = (TextView) findViewById(R.id.textViewQRCode);
        txtView.setText(message);

        WifiApControl.openWifiAp(getBaseContext(), "test-hot-pot", "123456789");
        enableServer();
    }

    private void enableServer() {
    }
}
