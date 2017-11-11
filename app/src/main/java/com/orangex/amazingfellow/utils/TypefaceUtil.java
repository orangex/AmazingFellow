package com.orangex.amazingfellow.utils;

import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by orangex on 2017/11/10.
 */

public enum  TypefaceUtil {
        TYPEFACE;
        
        private static Typeface typefaceWawa;
        private static Typeface typeface55;
        
        public void setTypefaceWawa(TextView textView) {
            if (typefaceWawa == null)
                typefaceWawa = Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/WawaSC-Regular.otf");
            textView.setTypeface(typefaceWawa);
        }
        
        public void set55Typeface(TextView textView) {
            if (typeface55 == null)
                typeface55 = Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/HYQiHei-55S.otf");
            textView.setTypeface(typeface55);
        }
}
