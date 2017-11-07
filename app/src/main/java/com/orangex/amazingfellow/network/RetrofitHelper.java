package com.orangex.amazingfellow.network;

import com.orangex.amazingfellow.base.AFApplication;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * Created by orangex on 2017/10/28.
 */

public class RetrofitHelper {// TODO: 2017/10/28 静态成员 or 单例，类的构造到底应不应该依赖 context
    private static final int DEFAULT_TIMEOUT = 3;
    private static HashMap<String, Object> sServicemap = new HashMap<>();// TODO: 2017/10/28 hashmap 优化？
    
    public static  <S> S getService(Class<S> serviceClass) {
        if (sServicemap.containsKey(serviceClass.getName())) {
            return (S) sServicemap.get(serviceClass.getName());
        } else {
            Object obj = createService(serviceClass);
            sServicemap.put(serviceClass.getName(), obj);
            return (S) obj;
        }
    }
    
    private static <S> S createService(Class<S> serviceClass) {
        //custom OkHttp
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor);
        //time our
        httpClient.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClient.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClient.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //cache
        File httpCacheDirectory = new File(AFApplication.getAppContext().getCacheDir(), "OkHttpCache");
        httpClient.cache(new Cache(httpCacheDirectory, 10 * 1024 * 1024));
        //Interceptor
//        httpClient.addNetworkInterceptor(new LogInterceptor());
//        httpClient.addInterceptor(new CacheControlInterceptor());
        
        return createService(serviceClass, httpClient.build());
    }
    
    private static <S> S createService(Class<S> serviceClass, OkHttpClient okHttpClient) {
        String baseUrl = "";
        try {
            Field field1 = serviceClass.getField("baseUrl");
            baseUrl = (String) field1.get(serviceClass);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();// TODO: 2017/10/28 log 工具类替换
        } catch (IllegalAccessException e) {
            e.getMessage();
            e.printStackTrace();
        }
    
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit.create(serviceClass);
    }
    
    //    private class LogInterceptor implements Interceptor {
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Request request = chain.request();
//
//            long t1 = System.nanoTime();
//            Timber.i("HttpHelper" + String.format("Sending request %s on %s%n%s",
//                    request.url(), chain.connection(), request.headers()));
//
//            Response response = chain.proceed(request);
//            long t2 = System.nanoTime();
//
//            Timber.i("HttpHelper" + String.format("Received response for %s in %.1fms%n%s",
//                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
//            return response;
//
//            // log Response Body
//            //            if(BuildConfig.DEBUG) {
//            //                String responseBody = response.body().string();
//            //                Log.v("HttpHelper", String.format("Received response for %s in %.1fms%n%s%n%s",
//            //                        response.request().url(), (t2 - t1) / 1e6d, response.headers(), responseBody));
//            //                return response.newBuilder()
//            //                        .body(ResponseBody.create(response.body().contentType(), responseBody))
//            //                        .build();
//            //            } else {
//            //                Log.v("HttpHelper", String.format("Received response for %s in %.1fms%n%s",
//            //                        response.request().url(), (t2 - t1) / 1e6d, response.headers()));
//            //                return response;
//            //            }
//        }
//    }
//
//    private class CacheControlInterceptor implements Interceptor {
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Request request = chain.request();
//            if (!AppUtils.isNetworkConnected(mContext)) {
//                request = request.newBuilder()
//                        .cacheControl(CacheControl.FORCE_CACHE)
//                        .build();
//            }
//
//            Response response = chain.proceed(request);
//
//            if (AppUtils.isNetworkConnected(mContext)) {
//                int maxAge = 60 * 60; // read from cache for 1 minute
//                response.newBuilder()
//                        .removeHeader("Pragma")
//                        .header("Cache-Control", "public, max-age=" + maxAge)
//                        .build();
//            } else {
//                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
//                response.newBuilder()
//                        .removeHeader("Pragma")
//                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                        .build();
//            }
//            return response;
//        }
//    }

    
}
