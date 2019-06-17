package at.tugraz.ist.swe.cheat.dto;

import android.graphics.Bitmap;

import java.io.Serializable;

public class ProxyBitmap implements Serializable {
    private int[] pixels;
    private int width, height;

    public ProxyBitmap(Bitmap bitmap) {
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        pixels = new int [width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
    }

    public Bitmap getBitmap() {
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    }
}
