package com.raihanMuhammadIhsanJBusAF.request;

import com.raihanMuhammadIhsanJBusAF.model.Account;
import com.raihanMuhammadIhsanJBusAF.model.BaseResponse;
import com.raihanMuhammadIhsanJBusAF.model.Bus;
import com.raihanMuhammadIhsanJBusAF.model.BusType;
import com.raihanMuhammadIhsanJBusAF.model.Facility;
import com.raihanMuhammadIhsanJBusAF.model.Renter;
import com.raihanMuhammadIhsanJBusAF.model.Station;

import java.util.List;

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
    @POST("account/{id}/registerRenter")
    Call<BaseResponse<Renter>> registerRenter(
            @Path("id") int id,
            @Query("companyName") String companyName,
            @Query("address") String address,
            @Query("phoneNumber") String phoneNumber
    );
    @GET("bus/getMyBus")
    Call<List<Bus>> getMyBus(
            @Query("accountId") int accountId
    );
    @GET("station/getAll")
    Call<List<Station>> getAllStation();

    @POST("bus/create")
    Call<BaseResponse<Bus>> create(
            @Query("accountId") int accountId,
            @Query("name") String name,
            @Query("capacity") int capacity,
            @Query("facilities") List<Facility> facilities,
            @Query("busType") BusType busType,
            @Query("price") int price,
            @Query("stationDepartureId") int stationDepartureId,
            @Query("stationArrivalId") int stationArrivalId
            );
}

