package com.baina.hackathon.apkshare;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
                try {
                    QRCodeWriter writer = new QRCodeWriter();
                    BitMatrix matrix = writer.encode("hello world", BarcodeFormat.QR_CODE,
                            QR_WIDTH, QR_HEIGHT);

                    Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
                    hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                    BitMatrix bitMatrix = new QRCodeWriter().encode("hello world", BarcodeFormat.QR_CODE,
                            QR_WIDTH, QR_HEIGHT, hints);
                    int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
                    for (int y = 0; y < QR_HEIGHT; y++) {
                        for (int x = 0; x < QR_WIDTH; x++) {
                            if (bitMatrix.get(x, y)) {
                                pixels[y * QR_WIDTH + x] = 0xff000000;
                            } else {
                                pixels[y * QR_WIDTH + x] = 0xffffffff;
                            }
                        }
                    }

                    Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_WIDTH, Bitmap.Config.ARGB_8888);
                    bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
                    ImageView imageView = (ImageView) findViewById(R.id.bitImg);
                    imageView.setImageBitmap(bitmap);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
