package com.baina.hackathon.apkshare;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.baina.hackathon.wifiControl.WifiApControl;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {

    private final static int QR_WIDTH = 200;
    private final static int QR_HEIGHT = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        btnWifiAp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiApControl.openWifiAp(getBaseContext(), "test-hot-pot", "123456789");
            }
        });
    }
}
