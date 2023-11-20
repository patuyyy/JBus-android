package com.raihanMuhammadIhsanJBusAF;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn, registBtn = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){

        }

        registBtn = (Button) findViewById(R.id.registerbutton);
        loginBtn = (Button) findViewById(R.id.loginbutton);

        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewToast(LoginActivity.this, "Menuju ke page register");
                moveActivity(getApplicationContext(), RegisterActivity.class);
            }
        });
        TextView username =(TextView) findViewById(R.id.email);
        TextView password =(TextView) findViewById(R.id.password);

        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbutton);

        //admin and admin

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("ihsan") && password.getText().toString().equals("ihsan")){
                    //correct
                    viewToast(LoginActivity.this, "Berhasil Login!");
                    moveActivity(getApplicationContext(), MainActivity.class);
                }else
                    //incorrect
                    viewToast(LoginActivity.this, "Gagal Login!");
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