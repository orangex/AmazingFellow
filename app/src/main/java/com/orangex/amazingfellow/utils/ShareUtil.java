package com.orangex.amazingfellow.utils;

import android.graphics.Bitmap;

/**
 * Created by orangex on 2017/11/30.
 */

public class ShareUtil {
    public static void sharePic(Bitmap bitmapToShare) {
        WXAPI.sharePic(bitmapToShare);
    }
}
