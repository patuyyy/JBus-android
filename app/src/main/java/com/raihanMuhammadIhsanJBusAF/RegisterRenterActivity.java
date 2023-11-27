package com.raihanMuhammadIhsanJBusAF;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.raihanMuhammadIhsanJBusAF.model.Account;
import com.raihanMuhammadIhsanJBusAF.model.BaseResponse;
import com.raihanMuhammadIhsanJBusAF.model.Renter;
import com.raihanMuhammadIhsanJBusAF.request.BaseApiService;
import com.raihanMuhammadIhsanJBusAF.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterRenterActivity extends AppCompatActivity {

    private Button registerBtn = null;
    private EditText companyname, address, phonenumber;
    private BaseApiService mApiService;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_renter);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){

        }
        mContext = this;
        mApiService = UtilsApi.getApiService();

        companyname = findViewById(R.id.companyname);
        address = findViewById(R.id.address);
        phonenumber = findViewById(R.id.phonenumber);
        registerBtn = findViewById(R.id.registerbutton);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRegisterRenter();
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
    protected void handleRegisterRenter() {
        // handling empty field
        String companynameS = companyname.getText().toString();
        String addressS = address.getText().toString();
        String phonenumberS = phonenumber.getText().toString();
        int idS = LoginActivity.loggedAccount.id;


        if (companynameS.isEmpty() || addressS.isEmpty() || phonenumberS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.registerRenter(idS, companynameS, addressS, phonenumberS).enqueue(new Callback<BaseResponse<Renter>>() {
            @Override
            public void onResponse(Call<BaseResponse<Renter>> call,
                                   Response<BaseResponse<Renter>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Renter> res = response.body();
                LoginActivity.loggedAccount.company = res.payload;
                // if success finish this activity (back to login activity)
                if (res.success) {
                    finish();
                    moveActivity(getApplicationContext(), AboutMeActivity.class);
                }
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<BaseResponse<Renter>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}