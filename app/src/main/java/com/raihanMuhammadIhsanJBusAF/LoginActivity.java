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

import com.google.android.material.button.MaterialButton;
import com.raihanMuhammadIhsanJBusAF.model.Account;
import com.raihanMuhammadIhsanJBusAF.model.BaseResponse;
import com.raihanMuhammadIhsanJBusAF.request.BaseApiService;
import com.raihanMuhammadIhsanJBusAF.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn, registBtn = null;
    public static Account loggedAccount;
    private BaseApiService mApiService;
    private Context mContext;
    private EditText email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){

        }

        mContext = this;
        mApiService = UtilsApi.getApiService();
        // sesuaikan dengan ID yang kalian buat di layout
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginbutton);
        registBtn = (Button) findViewById(R.id.registerbutton);

        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewToast(LoginActivity.this, "Menuju ke page register");
                moveActivity(getApplicationContext(), RegisterActivity.class);
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogin();
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
    protected void handleLogin() {
        // handling empty field
        String emailS = email.getText().toString();
        String passwordS = password.getText().toString();

        if (emailS.isEmpty() || passwordS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.login(emailS, passwordS).enqueue(new Callback<BaseResponse<Account>>() {
            @Override
            public void onResponse(Call<BaseResponse<Account>> call,
                                   Response<BaseResponse<Account>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Account> res = response.body();
                loggedAccount = res.payload;
                // if success finish this activity (go to main activity)
                if (res.success) {
                    moveActivity(getApplicationContext(), MainActivity.class);
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}