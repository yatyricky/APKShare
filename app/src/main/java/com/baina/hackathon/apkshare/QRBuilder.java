package com.baina.hackathon.apkshare;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by jxyi on 2016/10/23.
 */

public class QRBuilder {

    @Nullable
    public static Bitmap buildQRBitmap(final String text, int width, int height)
    {
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE,
                    width, height, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        if (bitMatrix == null)
        {
            return null;
        }

        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                } else {
                    pixels[y * width + x] = 0xffffffff;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, height, height);

        return bitmap;
    }

}
