package com.orangex.amazingfellow.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by orangex on 2017/11/30.
 */

public class BitmapUtil {
    
    private static final int MAX_SIZE = 1024 * 32;
    
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
    
        int options = 100;
        while (output.toByteArray().length > MAX_SIZE && options != 10) {
            output.reset(); //清空output
            bmp.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到output中
            if (needRecycle) {
                bmp.recycle();
            }
            options -= 10;
        }
        
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
}
