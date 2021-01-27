package com.binzee.foxdevframe.utils.media;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Bitmap工作台
 *
 * @author tong.xw
 * 2021/01/25 14:31
 */
public class BitmapCraft {
    private final Bitmap srcBitmap;
    private ColorMatrix mMatrix = new ColorMatrix();

    public BitmapCraft(Bitmap srcBitmap) {
        this.srcBitmap = srcBitmap;
    }

    public Bitmap create() {
        Bitmap temp = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
        Canvas canvas = new Canvas(temp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColorFilter(new ColorMatrixColorFilter(mMatrix));
        canvas.drawBitmap(srcBitmap, 0, 0, paint);

        return temp;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 参数方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 色调
     */
    public BitmapCraft hue(float hue) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setRotate(0, hue);
        matrix.setRotate(1, hue);
        matrix.setRotate(2, hue);

        mMatrix.postConcat(matrix);
        return this;
    }

    /**
     * 饱和度
     */
    public BitmapCraft saturation(float saturation) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(saturation);
        mMatrix.postConcat(matrix);
        return this;
    }

    /**
     * 亮度
     */
    public BitmapCraft lum(float lum) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setScale(lum, lum, lum, 1);
        mMatrix.postConcat(matrix);
        return this;
    }
}
