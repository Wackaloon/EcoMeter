package com.alexanderageychenko.ecometer.Model.Entity.rest;

import com.alexanderageychenko.ecometer.Model.Entity.response.GetMetersList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestInterface {

    @GET("/private/some_api/meters/list/{id}")
    Call<GetMetersList> getMeters(@Path("id") Long id);

}


