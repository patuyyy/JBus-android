package com.raihanMuhammadIhsanJBusAF;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.raihanMuhammadIhsanJBusAF.model.Bus;
import com.raihanMuhammadIhsanJBusAF.model.Facility;
import com.raihanMuhammadIhsanJBusAF.request.BaseApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusDetailMainActivity extends AppCompatActivity {

    private BaseApiService mApiService;
    private Context mContext;
    private TextView busName, capacity, price = null;
    private TextView busType, deptStation, arrStation= null;
    private CheckBox acCheckBox, wifiCheckBox, toiletCheckBox, lcdCheckBox;
    private CheckBox coolboxCheckBox, lunchCheckBox, baggageCheckBox, electricCheckBox;
    private Button checkScheduleBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail_main);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){
        }

        busName = findViewById(R.id.busname);
        capacity = findViewById(R.id.capacity);
        price = findViewById(R.id.price);
        busName.setText(MainActivity.selectedBusTemp.name);
        capacity.setText(MainActivity.selectedBusTemp.capacity + " seats");
        price.setText("Rp." + Double.toString(MainActivity.selectedBusTemp.price.price) + " /seat");

        busType = findViewById(R.id.busTypetxt);
        deptStation = findViewById(R.id.departuretxt);
        arrStation = findViewById(R.id.arrivaltxt);
        busType.setText("" + MainActivity.selectedBusTemp.busType);
        deptStation.setText("" + MainActivity.selectedBusTemp.departure.stationName);
        arrStation.setText("" + MainActivity.selectedBusTemp.arrival.stationName);

        acCheckBox = findViewById(R.id.acbox);
        wifiCheckBox = findViewById(R.id.wifibox);
        toiletCheckBox = findViewById(R.id.toiletbox);
        lcdCheckBox = findViewById(R.id.lcdtvbox);
        coolboxCheckBox = findViewById(R.id.coolboxbox);
        lunchCheckBox = findViewById(R.id.lunchbox);
        baggageCheckBox = findViewById(R.id.baggagebox);
        electricCheckBox = findViewById(R.id.socketbox);
        if(MainActivity.selectedBusTemp.facilities.contains(Facility.AC)){acCheckBox.setChecked(true);}
        if(MainActivity.selectedBusTemp.facilities.contains(Facility.WIFI)){wifiCheckBox.setChecked(true);}
        if(MainActivity.selectedBusTemp.facilities.contains(Facility.TOILET)){toiletCheckBox.setChecked(true);}
        if(MainActivity.selectedBusTemp.facilities.contains(Facility.LCD_TV)){lcdCheckBox.setChecked(true);}
        if(MainActivity.selectedBusTemp.facilities.contains(Facility.COOL_BOX)){coolboxCheckBox.setChecked(true);}
        if(MainActivity.selectedBusTemp.facilities.contains(Facility.LUNCH)){lunchCheckBox.setChecked(true);}
        if(MainActivity.selectedBusTemp.facilities.contains(Facility.LARGE_BAGGAGE)){baggageCheckBox.setChecked(true);}
        if(MainActivity.selectedBusTemp.facilities.contains(Facility.ELECTRIC_SOCKET)){electricCheckBox.setChecked(true);}

        checkScheduleBtn = findViewById(R.id.checkschedulebtn);
        checkScheduleBtn.setOnClickListener(view -> {
            moveActivity(getApplicationContext(), ScheduleDetailActivity.class);
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