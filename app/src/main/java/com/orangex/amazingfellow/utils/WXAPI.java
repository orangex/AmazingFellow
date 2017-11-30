package com.orangex.amazingfellow.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.orangex.amazingfellow.constant.Config;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by orangex on 2017/11/30.
 */

public class WXAPI {
    private static final int THUMB_SIZE = 150;
    private static IWXAPI sIWXAPI;
    
    public static void regToWx(Context context) {
        sIWXAPI = WXAPIFactory.createWXAPI(context, Config.WX_APP_ID, true);
        sIWXAPI.registerApp(Config.WX_APP_ID);
    }
    
    public static void sharePic(Bitmap bitmap) {
        WXImageObject imageObject = new WXImageObject(bitmap);
        WXMediaMessage message = new WXMediaMessage();
        message.mediaObject = imageObject;
        
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, 80, true);
        bitmap.recycle();
        message.thumbData = BitmapUtil.bmpToByteArray(thumbBmp, true);  // ��������ͼ
    
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = message;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        sIWXAPI.sendReq(req);
        
    }
    
    private static String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
