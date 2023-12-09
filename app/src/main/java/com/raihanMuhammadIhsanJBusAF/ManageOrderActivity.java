package com.raihanMuhammadIhsanJBusAF;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.raihanMuhammadIhsanJBusAF.model.Payment;
import com.raihanMuhammadIhsanJBusAF.request.BaseApiService;
import com.raihanMuhammadIhsanJBusAF.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageOrderActivity extends AppCompatActivity {

    private BaseApiService mApiService;
    private Context mContext;
    private ListView paymentListView = null;
    public static Payment selectedPaymentTemp = null;
    private final int accId = LoginActivity.loggedAccount.id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_order);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){
        }

        mContext = this;
        mApiService = UtilsApi.getApiService();

        paymentListView = findViewById(R.id.listorder);
        handleList();

    }
    protected void handleList() {
        // handling empty field
        mApiService.getMyOrder(accId).enqueue(new Callback<List<Payment>>() {
            @Override
            public void onResponse(Call<List<Payment>> call,
                                   Response<List<Payment>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Payment> res = response.body();
                ArrayList<Payment> set = new ArrayList<>(res);
                PaymentArrayAdapter busRenterAdptr = new PaymentArrayAdapter(mContext, res);
                paymentListView.setAdapter(busRenterAdptr);
                paymentListView.setClickable(true);
                paymentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedPaymentTemp = res.get(i);
                        moveActivity(getApplicationContext(), OrderDetailActivity.class);
                    }
                });

                // if success finish this activity (refresh page)
            }
            @Override
            public void onFailure(Call<List<Payment>> call, Throwable t) {
                Log.e("Retrofit", "Failure: " + t.getMessage(), t);
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
    private void viewToast(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
}