package com.raihanMuhammadIhsanJBusAF.request;

import com.raihanMuhammadIhsanJBusAF.model.Account;
import com.raihanMuhammadIhsanJBusAF.model.BaseResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {
    @GET("account/{id}")
    Call<Account> getAccountbyId (@Path("id") int id);

    @POST("account/register")
    Call<BaseResponse<Account>> register(
            @Query("name") String name,
            @Query("email") String email,
            @Query("password") String password
    );
    @POST("account/login")
    Call<BaseResponse<Account>> login(
            @Query("email") String email,
            @Query("password") String password
    );
    @GET("account/{id}")
    Call<BaseResponse<Account>> viewAccount(
            @Path("id") int id
    );
    @POST("account/{id}/topUp")
    Call<BaseResponse<Double>> topUp(
            @Path("id") int id,
            @Query("amount") double amount
    );
}

