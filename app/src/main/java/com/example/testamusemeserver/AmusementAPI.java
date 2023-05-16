package com.example.testamusemeserver;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AmusementAPI {
    String BASE_URL = "http://h304301261.nichost.ru/";

    @GET("getRandAmusement")
    Observable<AmusementItem> getRandAmusement(@Query("themes") String themes);
}
