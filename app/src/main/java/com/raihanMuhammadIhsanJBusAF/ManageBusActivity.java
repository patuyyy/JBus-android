package com.raihanMuhammadIhsanJBusAF;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.raihanMuhammadIhsanJBusAF.model.BaseResponse;
import com.raihanMuhammadIhsanJBusAF.model.Bus;
import com.raihanMuhammadIhsanJBusAF.request.BaseApiService;
import com.raihanMuhammadIhsanJBusAF.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageBusActivity extends AppCompatActivity {
    //private List<Bus> listBus = new ArrayList<>();
    private ListView busListView = null;
    private BaseApiService mApiService;
    private Context mContext;
    private Button addBusBtn, addScheduleBtn = null;
    public static Bus selectedBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bus);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){
        }
        mContext = this;
        mApiService = UtilsApi.getApiService();
        handleList();

        // hubungkan komponen dengan ID nya
        busListView = findViewById(R.id.listbus);
        addBusBtn = findViewById(R.id.addbusbtn);
        addBusBtn.setOnClickListener(view -> {
            moveActivity(getApplicationContext(), AddBusActivity.class);
        });
        addScheduleBtn = findViewById(R.id.addscheduleBtn);
        addScheduleBtn.setOnClickListener(view -> {
            moveActivity(getApplicationContext(), AddBusScheduleActivity.class);
        });

    }
    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
    private void viewToast(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
    protected void handleList() {
        // handling empty field
        int idS = LoginActivity.loggedAccount.id;

        mApiService.getMyBus(idS).enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call,
                                   Response<List<Bus>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Bus> res = response.body();
                ArrayList<Bus> set = new ArrayList<>(res);
                BusRenterArrayAdapter busRenterAdptr = new BusRenterArrayAdapter(mContext, res);
                busListView.setAdapter(busRenterAdptr);
                busListView.setClickable(true);
                busListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedBus = res.get(i);
                        moveActivity(getApplicationContext(), BusDetailActivity.class);
                    }
                });

                // if success finish this activity (refresh page)
            }
            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}