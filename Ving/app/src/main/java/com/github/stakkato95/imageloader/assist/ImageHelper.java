package com.github.stakkato95.imageloader.assist;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

/**
 * Created by Artyom on 24.01.2015.
 */
public class ImageHelper {

    private static final int PAINT_COLOR = 0xff424242;

    public static Bitmap getRounded(Bitmap bitmap) {
        Bitmap output = null;
        if (bitmap != null) {
            //ARGB_8888 32-bit bitmap
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            paint.setColor(PAINT_COLOR);
            //enables antialiasing
            paint.setAntiAlias(true);
            //set coordinates for rectangle
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            canvas.drawCircle((float)bitmap.getWidth() / 2, (float)bitmap.getHeight() / 2, (float)bitmap.getWidth() / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            //1st rest - subset of the bitmap, 2st - the rect in which bmp got to be inserted
            canvas.drawBitmap(bitmap, null, rect, paint);
        }
        return output;
    }
}