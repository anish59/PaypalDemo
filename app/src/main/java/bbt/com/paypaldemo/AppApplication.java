package bbt.com.paypaldemo;

import android.app.Application;
import bbt.com.paypaldemo.helper.Functions;
import bbt.com.paypaldemo.helper.PrefUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AppApplication extends Application {
    public static Gson gson;
    public static Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();
        initGson();
        initRetrofit();
    }

    private void initRetrofit() {
        int cacheSize = 20 * 1024 * 1024; // 20 MB
        Cache cache = new Cache(getCacheDir(), cacheSize);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .cache(cache)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        if (Functions.isConnected(getApplicationContext())) {

                            int maxAge = 60 * 5; // read from cache for 2 minute
                            return originalResponse.newBuilder()
                                    .header("Cache-Control", "public, max-age=" + maxAge)
                                    .build();

                        } else {
                            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                            return originalResponse.newBuilder()
                                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                    .build();
                        }
                    }
                })
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logging)
//                .addInterceptor(new HeaderInterceptor())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Apis.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


    }

    private void initGson() {
        gson = new GsonBuilder()
                .setLenient()
                .create();
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static Gson getGson() {
        return gson;
    }

    private class HeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            String authKey;
            authKey = PrefUtils.getPayPalAccessToken(getApplicationContext());
//            authKey = "_lhiyIgqnleeDMLl249prXgajdTBHiqu";
            Request newRequest = chain.request().newBuilder()
                    .addHeader("auth-key", authKey)
                    .build();
            return chain.proceed(newRequest);
        }
    }

}
