package com.raihanMuhammadIhsanJBusAF;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.raihanMuhammadIhsanJBusAF.model.Account;
import com.raihanMuhammadIhsanJBusAF.model.BaseResponse;
import com.raihanMuhammadIhsanJBusAF.request.BaseApiService;
import com.raihanMuhammadIhsanJBusAF.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutMeActivity extends AppCompatActivity {

    private BaseApiService mApiService;
    private Context mContext;
    private EditText amountTopup = null;
    private TextView email, username, balance, initial = null;
    private Button topUpButton = null;
    private TextView isrenter, notrenter, registerrenter = null;
    private Button manageBusBtn = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){
        }
        initial = findViewById(R.id.initial);
        email = findViewById(R.id.emaildata);
        username = findViewById(R.id.username);
        balance = findViewById(R.id.balancedata);
        amountTopup = findViewById(R.id.amount);
        initial.setText("" + LoginActivity.loggedAccount.name.charAt(0));
        email.setText(LoginActivity.loggedAccount.email);
        username.setText(LoginActivity.loggedAccount.name);
        balance.setText("Rp. " + Double.toString(LoginActivity.loggedAccount.balance));

        mContext = this;
        mApiService = UtilsApi.getApiService();
        // sesuaikan dengan ID yang kalian buat di layout
        topUpButton = findViewById(R.id.topup);

        isrenter = findViewById(R.id.isrenter);
        notrenter = findViewById(R.id.notrenter);
        registerrenter = findViewById(R.id.registerrenter);
        manageBusBtn = findViewById(R.id.managebus);

        if(LoginActivity.loggedAccount.company == null){
            notrenter.setVisibility(View.VISIBLE);
            registerrenter.setVisibility(View.VISIBLE);
            isrenter.setVisibility(View.GONE);
            manageBusBtn.setVisibility(View.GONE);
        }
        else {
            notrenter.setVisibility(View.GONE);
            registerrenter.setVisibility(View.GONE);
            isrenter.setVisibility(View.VISIBLE);
            manageBusBtn.setVisibility(View.VISIBLE);
        }

        manageBusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveActivity(getApplicationContext(), ManageBusActivity.class);
            }
        });
        registerrenter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                moveActivity(getApplicationContext(), RegisterRenterActivity.class);
                viewToast(AboutMeActivity.this, "Daftar perusahaan kuyy");
                ;
            }
        });

        topUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTopup();
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
    protected void handleTopup() {
        // handling empty field
        String valueCheck = amountTopup.getText().toString();
        double amountS = Double.parseDouble(amountTopup.getText().toString());
        int idS = LoginActivity.loggedAccount.id;

        if (valueCheck.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (amountS == 0) {
            Toast.makeText(mContext, "Gk bisa topUp 0 co",  Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.topUp(idS, amountS).enqueue(new Callback<BaseResponse<Double>>() {
            @Override
            public void onResponse(Call<BaseResponse<Double>> call,
                                   Response<BaseResponse<Double>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Double> res = response.body();
                // if success finish this activity (refresh page)
                if (res.success) {
                    finish();
                    overridePendingTransition(0,0);
                    LoginActivity.loggedAccount.balance += res.payload;
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);

                }
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<BaseResponse<Double>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}