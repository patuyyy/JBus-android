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

public class PaymentDetailActivity extends AppCompatActivity {

    private BaseApiService mApiService;
    private Context mContext;
    private Bus selectedBus;
    private TextView busName, seats, price = null;
    private TextView busType, deptStation, arrStation, schedule, status, rating= null;
    private Button cancelBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){
        }

        mContext = this;
        mApiService = UtilsApi.getApiService();
        handleBus();

        busName = findViewById(R.id.busname);
        seats = findViewById(R.id.seattxt);
        price = findViewById(R.id.price);
        busType = findViewById(R.id.busTypetxt);
        deptStation = findViewById(R.id.departuretxt);
        arrStation = findViewById(R.id.arrivaltxt);
        schedule = findViewById(R.id.scheduledata);
        status = findViewById(R.id.paymentstatusdata);
        rating = findViewById(R.id.ratingdata);


        cancelBtn = findViewById(R.id.cancelbtn);

        if(PaymentActivity.selectedPayment.status == Invoice.PaymentStatus.WAITING){
            cancelBtn.setVisibility(View.VISIBLE);
        }else {
            cancelBtn.setVisibility(View.GONE);
        }
        cancelBtn.setOnClickListener(view -> {
            handleCancel();
        });

    }
    protected void handleCancel() {
        mApiService.cancel(PaymentActivity.selectedPayment.id).enqueue(new Callback<BaseResponse<Payment>>() {
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
                PaymentActivity.selectedPayment.status = Invoice.PaymentStatus.FAILED;
                // if success finish this activity (refresh page)
                if (res.success) {
                    LoginActivity.loggedAccount.balance += MainActivity.listBus.get(PaymentActivity.selectedPayment.getBusId()-1).price.price
                    * PaymentActivity.selectedPayment.busSeats.size();
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
    private void handleBus() {
        mApiService.getBus(PaymentActivity.selectedPayment.getBusId()).enqueue(new Callback<Bus>() {
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
                seats.setText(PaymentActivity.selectedPayment.busSeats.toString());
                price.setText("Rp." + Double.toString(selectedBus.price.price));
                busType.setText(selectedBus.busType.toString());
                deptStation.setText(selectedBus.departure.stationName);
                arrStation.setText(selectedBus.arrival.stationName);
                schedule.setText(PaymentActivity.selectedPayment.departureDate.toString());
                status.setText(PaymentActivity.selectedPayment.status.toString());
                rating.setText(PaymentActivity.selectedPayment.rating.toString());
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