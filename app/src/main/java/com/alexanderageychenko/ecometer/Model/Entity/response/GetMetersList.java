package com.alexanderageychenko.ecometer.Model.Entity.response;

import com.alexanderageychenko.ecometer.Model.Entity.IResponse;
import com.alexanderageychenko.ecometer.Model.Entity.Meter;
import com.alexanderageychenko.ecometer.Tools.annotation.AllowedNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GetMetersList implements IResponse {

    @SerializedName("response")
    @Expose
    public Response response;
    @SerializedName("meta")
    @Expose
    @AllowedNull // field with meta may not present
    public Meta meta;

    public class Response {
        @SerializedName("items")
        @Expose
        public List<Meter> items;


    }
    public class Meta {
        @SerializedName("total")
        @Expose
        public Long total;
        @SerializedName("time")
        @Expose
        public Long time;
        @SerializedName("errors")
        @Expose
        public List<Error> errors;
        @SerializedName("http_code")
        @Expose
        public Integer httpCode;
    }


    @Override
    public List<Error> getError() {
        return meta.errors;
    }

    @Override
    public Long getDate() {
        return meta.time;
    }

    @Override
    public Integer getHttpCode() {
        if (meta.httpCode == null)return 200;
            return meta.httpCode;
    }
}
