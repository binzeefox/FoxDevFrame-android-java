package com.binzee.foxdevframe.utils.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.Matrix3f;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.ScriptIntrinsicColorMatrix;

import androidx.annotation.NonNull;

import com.binzee.foxdevframe.FoxCore;

/**
 * 图片处理类
 *
 * @author tong.xw
 * 2021/01/15 17:04
 */
public class RenderScriptTools {

    /**
     * 雾化
     */
    public static Bitmap blur(Context ctx, Bitmap srcBitmap, float radius) {
        final RenderScript rs = RenderScript.create(ctx);
        Bitmap temp = srcBitmap.copy(srcBitmap.getConfig(), true);
        final Allocation input = Allocation.createFromBitmap(rs, srcBitmap);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(temp);
        rs.destroy();
        return temp;
    }

    /**
     * 改变色调
     */
    public static Bitmap hue(Context ctx, Bitmap srcBitmap, double hue) {
        final RenderScript rs = RenderScript.create(ctx);
        Bitmap temp = srcBitmap.copy(srcBitmap.getConfig(), true);
        final Allocation input = Allocation.createFromBitmap(rs, srcBitmap);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicColorMatrix script = ScriptIntrinsicColorMatrix.create(rs);

        float cos = (float) Math.cos(hue);
        float sin = (float) Math.sin(hue);
        Matrix3f mat = new Matrix3f();
        mat.set(0, 0, (float) (.299 + .701 * cos + .168 * sin));
        mat.set(1, 0, (float) (.587 - .587 * cos + .330 * sin));
        mat.set(2, 0, (float) (.114 - .114 * cos - .497 * sin));
        mat.set(0, 1, (float) (.299 - .299 * cos - .328 * sin));
        mat.set(1, 1, (float) (.587 + .413 * cos + .035 * sin));
        mat.set(2, 1, (float) (.114 - .114 * cos + .292 * sin));
        mat.set(0, 2, (float) (.299 - .3 * cos + 1.25 * sin));
        mat.set(1, 2, (float) (.587 - .588 * cos - 1.05 * sin));
        mat.set(2, 2, (float) (.114 + .886 * cos - .203 * sin));
        script.setColorMatrix(mat);
        script.forEach(input, output);
        output.copyTo(temp);
        rs.destroy();
        return temp;
    }
}