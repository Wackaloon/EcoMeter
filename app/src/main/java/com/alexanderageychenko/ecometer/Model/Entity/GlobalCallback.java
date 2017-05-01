package com.alexanderageychenko.ecometer.Model.Entity;

import com.alexanderageychenko.ecometer.Tools.FabricLoggingHandler;
import com.alexanderageychenko.ecometer.Tools.MyLogger;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public abstract class GlobalCallback<T extends IResponse> implements Callback<T> {
    private static Executor executor = Executors.newFixedThreadPool(3);
    protected Call<T> call;
    protected String url;

    @Override
    public void onResponse(Call<T> rawCall, Response<T> response) {
        this.call = rawCall;
        try {
            if (rawCall != null) {
                this.url = rawCall.request().url().toString();
            } else {
                this.url = response.raw().request().url().toString();
            }
        } catch (Exception e) {
            this.url = "Error getting url, call == null, request == null";
        }
        Observable.just(response)
                .observeOn(Schedulers.from(executor))
                .subscribe(new Action1<Response<T>>() {
                    @Override
                    public void call(Response<T> tResponse) {
                        handleResponse(tResponse);
                    }
                });
    }

    protected void handleResponse(Response<T> tResponse) {
        if (tResponse == null) {
            globalError(ErrorCode.NULL_RESPONSE.getNumber());
            return;
        }
        if (tResponse.body() == null) {
            globalError(ErrorCode.NULL_RESPONSE.getNumber());
            return;
        }
        int code = tResponse.code();
        if (code == ErrorCode.OK.getNumber()) {
            code = tResponse.body().getHttpCode();
            if (code != ErrorCode.OK.getNumber()) {
                globalError(code);
            } else if (!tResponse.body().getError().isEmpty()) {
                MyLogger.d("DEBUG", "N_REST_LOCAL_ERROR " + url + " | " + tResponse.body().getError().get(0).getMessage());
                localError(tResponse.body().getError());
            } else {
                MyLogger.d("DEBUG", "N_REST_OK " + url);
                success(tResponse.body());
            }
        } else {
            globalError(code);
        }
    }

    @Override
    public void onFailure(Call<T> rawCall, Throwable t) {
        this.call = rawCall;
        try {
            if (rawCall != null) {
                this.url = rawCall.request().url().toString();
            } else {
                this.url = "Error getting url, call == null";
            }
        } catch (Exception e) {
            this.url = "Error getting url, call == null";
        }
        Observable.just(t)
                .observeOn(Schedulers.from(executor))
                .subscribe(new Subscriber<Throwable>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Throwable t) {
                        if (t.getClass() == UnknownHostException.class
                                || t.getClass() == ConnectException.class
                                || t.getClass() == java.net.SocketTimeoutException.class) {

                            globalError(ErrorCode.CONNECTION_EXCEPTION.getNumber());

                        } else
                            globalError(ErrorCode.UNKNOWN_ERROR.getNumber());

                        String message = t.getMessage();
                        if (message == null) message = "null_message";
                        MyLogger.d("DEBUG", "N_REST_GLOBAL_ERROR message = " + message);
                        FabricLoggingHandler.log("onFailure: message = " + message + " url = " + url);
                    }
                });
    }

    protected void globalError(int code) {
        ErrorCode erCode = ErrorCode.get(code);
        if (erCode != null) {
            globalError(erCode);
            MyLogger.d("DEBUG", "N_REST_GLOBAL_ERROR " + erCode.name() + " | " + url);
        } else {
            FabricLoggingHandler.log("GLOBAL ERROR: http code = " + code + " url = " + url);
            MyLogger.d("DEBUG", "N_REST_GLOBAL_ERROR " + code + " | " + url);
        }
    }

    private void globalError(ErrorCode code) {
        if (code == null) code = ErrorCode.SERVER_ISSUE;
        String errorTitle = code.name() + ": " + code.getNumber() + " url =";
        FabricLoggingHandler.log(errorTitle + url);
    }

    protected abstract void localError(List<Error> error);

    protected abstract void success(T response);

    public enum ErrorCode {
        OK(200),
        NULL_RESPONSE(598),
        NULL_ITEMS_RESPONSE(599),
        SERVER_ISSUE(500),
        BAD_GATEWAY(502),
        NOT_FOUND(404),
        UNKNOWN_ERROR(997),
        CONNECTION_EXCEPTION(987),
        AUTHORIZATION(403);
        private int code;

        ErrorCode(int number) {
            this.code = number;
        }


        static public ErrorCode get(int code) {
            for (ErrorCode error : ErrorCode.values()) {
                if (error.getNumber() == code) {
                    return error;
                }
            }
            return null;
        }

        public int getNumber() {
            return code;
        }
    }
}
