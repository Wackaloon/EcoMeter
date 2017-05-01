package com.alexanderageychenko.ecometer.Model.Entity.rest;

import android.content.Context;

import com.alexanderageychenko.ecometer.BuildConfig;
import com.alexanderageychenko.ecometer.MainApplication;
import com.alexanderageychenko.ecometer.ServerType;
import com.alexanderageychenko.ecometer.Tools.MyGsonBuilder;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;
import com.alexanderageychenko.ecometer.Tools.dagger2.Module.MainModule;
import com.alexanderageychenko.ecometer.Tools.dagger2.Module.NetworkModule;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Scheduler;
import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Alexander on 01.05.2017.
 */
public class Rest {
    private static Rest instance;
    protected CookieJar cookieJar;
    HashMap<String, List<Cookie>> cookieStore;
    Retrofit retrofit;
    RestInterface restInterface;
    @Inject
    Context context;
    OkHttpClient okHttpClient;

    @Inject
    @Named(NetworkModule.NetworkScheduler)
    Scheduler networkSheduler;

    //private StompWebSocketClient webSocketClient;

    private String serverName;

    public Rest(Context context) {
        Dagger.get().getInjector().inject(this);
        instance = this;
        init();
    }

    public static synchronized Rest getInstance() {
        if (instance == null)
            newInstance();
        return instance;
    }

    public static synchronized void newInstance() {
        instance = new Rest(MainApplication.getInstance());
    }

    public static synchronized void setIntstance(Rest rest) {
        Rest.instance = rest;
    }

    public static SSLSocketFactory getAllTrustSSLContext() {
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            return sslSocketFactory;
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public RestInterface getRestInterface() {
        return restInterface;
    }

    void init() {
        switch (BuildConfig.SERVER_TYPE) {
            case TEST: {
                serverName = "https://api.your_rest_server.com";
                break;
            }
            case PROD: {
                serverName = "https://api.your_rest_server.com";
                break;
            }
        }

        cookieStore = new HashMap<>();
        cookieJar = new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        };

        okHttpClient = getOkHttpBuilder().build();

        Gson gson = MyGsonBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(serverName)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(networkSheduler))
                .build();

        restInterface = retrofit.create(RestInterface.class);
    }

    protected OkHttpClient.Builder getOkHttpBuilder() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;//hostname.equals(hostName);
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder requestBuilder = chain.request().newBuilder();
                        Boolean haveAgent = false;
                        String agent = null;
                        if (haveAgent) {
                            requestBuilder.addHeader("user_agent", agent);
                        }
                        okhttp3.Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor)
                .cookieJar(cookieJar);
        if (Dagger.get().getType() == MainModule.Type.ANDROID)
            okHttpBuilder.cache(new Cache(new File(context.getCacheDir(), "responses"), 3 * 1024 * 1024));

        if (BuildConfig.SERVER_TYPE != ServerType.PROD) {
            okHttpBuilder.sslSocketFactory(getAllTrustSSLContext());
        }
        return okHttpBuilder;
    }
}
