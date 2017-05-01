package com.alexanderageychenko.ecometer;

import com.alexanderageychenko.ecometer.Model.Entity.GlobalCallback;
import com.alexanderageychenko.ecometer.Model.Entity.IResponse;
import com.alexanderageychenko.ecometer.Model.Entity.rest.Rest;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;
import com.alexanderageychenko.ecometer.Tools.dagger2.DaggerAppComponent;
import com.alexanderageychenko.ecometer.Tools.dagger2.Module.DepositoryModule;
import com.alexanderageychenko.ecometer.Tools.dagger2.Module.MainModule;
import com.alexanderageychenko.ecometer.Tools.dagger2.Module.UIModule;
import com.alexanderageychenko.ecometer.tools.AnsiColor;
import com.alexanderageychenko.ecometer.tools.NullCheck;
import com.alexanderageychenko.ecometer.tools.TestContext;
import com.alexanderageychenko.ecometer.tools.TestTools;

import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Alexander on 01.05.2017.
 */

public class TestRoot {

    protected Rest restManager;
    private Boolean[] stop = {false};
    private boolean[] fail = {false};

    public TestRoot() {

        Dagger.setAppComponent(DaggerAppComponent.builder()
                .depositoryModule(new DepositoryModule())
                .mainModule(new MainModule(MainModule.Type.TEST))
                .uIModule(new UIModule(new TestContext()))
                .build());

        restManager = new Rest(new TestContext()) {
            @Override
            protected OkHttpClient.Builder getOkHttpBuilder() {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                return new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .hostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String hostname, SSLSession session) {
                                return true;
                            }
                        })
                        .addInterceptor(new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                Request.Builder requestBuilder = chain.request().newBuilder();
                                Boolean haveAgent = false;
                                String agent = null;
                                if (haveAgent) {
                                    requestBuilder.addHeader("user_agent", agent);
                                }
                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        })
                        .addInterceptor(interceptor)
                        .sslSocketFactory(Rest.getAllTrustSSLContext())
                        .cookieJar(cookieJar);
            }
        };
        Rest.setIntstance(restManager);
    }


    @Before
    public void before() {
        fail[0] = false;
        stop[0] = false;
    }

    public class CallbackNullCheckStop<T extends IResponse> extends GlobalCallback<T> {
        @Override
        public void onResponse(Call<T> rawCall, Response<T> response) {
            handleResponse(response);
        }

        @Override
        public void onFailure(Call<T> rawCall, Throwable t) {
            globalError(-1);
        }

        @Override
        protected void globalError(int code) {
            setTestFailedAndStop();
        }

        @Override
        protected void localError(List<Error> error) {
            setTestFailedAndStop();
        }

        @Override
        protected void success(T response) {
            new Check<T>().nullCheck(response, true);
        }
    }

    protected class Check<T> {

        public void nullCheck(T response, boolean stop) {
            fail[0] = NullCheck.check(response, "main");
            if (stop)
                stopTest();
        }

    }

    protected void waitForStopAndCheckResult() {
        TestTools.pause(stop);
        Assert.assertFalse(fail[0]);
    }

    protected void stopTest() {
        stop[0] = true;
    }

    protected void setTestFailedAndStop() {
        fail[0] = true;
        stopTest();
    }

    protected void setTestSuccecedAndStop() {
        fail[0] = false;
        stopTest();
    }


    protected void setTestSuccecedAndStop(String message) {
        System.out.println(AnsiColor.ANSI_YELLOW + message + AnsiColor.ANSI_RESET);
        fail[0] = false;
        stopTest();
    }


    protected void waitTest() {
        TestTools.pause(stop);
    }


    protected boolean testWasNotStoped() {
        return !stop[0];
    }
}
