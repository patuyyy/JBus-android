package com.raihanMuhammadIhsanJBusAF;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.raihanMuhammadIhsanJBusAF.model.BaseResponse;
import com.raihanMuhammadIhsanJBusAF.model.Bus;
import com.raihanMuhammadIhsanJBusAF.model.Invoice;
import com.raihanMuhammadIhsanJBusAF.model.Payment;
import com.raihanMuhammadIhsanJBusAF.request.BaseApiService;
import com.raihanMuhammadIhsanJBusAF.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private BaseApiService mApiService;
    private Context mContext;
    private Bus selectedBus;
    private TextView busName, seats, price = null;
    private TextView deptStation, buyerName, schedule, status = null;
    private Button acceptBtn, cancelBtn = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){
        }

        mContext = this;
        mApiService = UtilsApi.getApiService();
        handlePayment();

        busName = findViewById(R.id.busname);
        seats = findViewById(R.id.seattxt);
        price = findViewById(R.id.price);
        deptStation = findViewById(R.id.departuretxt);
        buyerName = findViewById(R.id.buyertxt);
        schedule = findViewById(R.id.scheduledata);
        status = findViewById(R.id.paymentstatusdata);
        acceptBtn = findViewById(R.id.acceptbtn);
        cancelBtn = findViewById(R.id.cancelbtn);

        if(ManageOrderActivity.selectedPaymentTemp.status == Invoice.PaymentStatus.FAILED ||
        ManageOrderActivity.selectedPaymentTemp.status == Invoice.PaymentStatus.SUCCESS) {
            acceptBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);
        }
        acceptBtn.setOnClickListener(v->{
            handleAccept();
        });
        cancelBtn.setOnClickListener(v->{
            handleCancel();
        });

    }

    protected void handleCancel() {
        mApiService.cancel(ManageOrderActivity.selectedPaymentTemp.id).enqueue(new Callback<BaseResponse<Payment>>() {
            @Override
            public void onResponse(Call<BaseResponse<Payment>> call,
                                   Response<BaseResponse<Payment>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Payment> res = response.body();
                ManageOrderActivity.selectedPaymentTemp.status = Invoice.PaymentStatus.FAILED;
                // if success finish this activity (refresh page)
                if (res.success) {
                    finish();
                    overridePendingTransition(0,0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void handleAccept() {
        mApiService.accept(ManageOrderActivity.selectedPaymentTemp.id).enqueue(new Callback<BaseResponse<Payment>>() {
            @Override
            public void onResponse(Call<BaseResponse<Payment>> call,
                                   Response<BaseResponse<Payment>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Payment> res = response.body();
                ManageOrderActivity.selectedPaymentTemp.status = Invoice.PaymentStatus.SUCCESS;
                // if success finish this activity (refresh page)
                if (res.success) {
                    LoginActivity.loggedAccount.balance += MainActivity.listBus.get(ManageOrderActivity.selectedPaymentTemp.getBusId()-1).price.price
                    *ManageOrderActivity.selectedPaymentTemp.busSeats.size();
                    finish();
                    overridePendingTransition(0,0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void handlePayment() {
        mApiService.getBus(ManageOrderActivity.selectedPaymentTemp.getBusId()).enqueue(new Callback<Bus>() {
            @Override
            public void onResponse(Call<Bus> call,
                                   Response<Bus> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Bus res = response.body();
                selectedBus = res;
                busName.setText(selectedBus.name);
                seats.setText(ManageOrderActivity.selectedPaymentTemp.busSeats.toString());
                price.setText("Rp." + Double.toString(selectedBus.price.price));
                deptStation.setText(selectedBus.departure.stationName);
                buyerName.setText(""+ManageOrderActivity.selectedPaymentTemp.buyerId);
                schedule.setText(ManageOrderActivity.selectedPaymentTemp.departureDate.toString());
                status.setText(ManageOrderActivity.selectedPaymentTemp.status.toString());
                // if success finish this activity (refresh page)
            }
            @Override
            public void onFailure(Call<Bus> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}