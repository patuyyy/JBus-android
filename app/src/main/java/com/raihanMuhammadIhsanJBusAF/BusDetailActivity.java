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

public class BusDetailActivity extends AppCompatActivity {

    private BaseApiService mApiService;
    private Context mContext;
    private TextView busName, capacity, price = null;
    private TextView busType, deptStation, arrStation= null;
    private CheckBox acCheckBox, wifiCheckBox, toiletCheckBox, lcdCheckBox;
    private CheckBox coolboxCheckBox, lunchCheckBox, baggageCheckBox, electricCheckBox;
    private Button addScheduleBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){
        }

        busName = findViewById(R.id.busname);
        capacity = findViewById(R.id.capacity);
        price = findViewById(R.id.price);
        busName.setText(ManageBusActivity.selectedBus.name);
        capacity.setText(ManageBusActivity.selectedBus.capacity + " seats");
        price.setText("Rp." + Double.toString(ManageBusActivity.selectedBus.price.price) + " /seat");

        busType = findViewById(R.id.busTypetxt);
        deptStation = findViewById(R.id.departuretxt);
        arrStation = findViewById(R.id.arrivaltxt);
        busType.setText("" + ManageBusActivity.selectedBus.busType);
        deptStation.setText("" + ManageBusActivity.selectedBus.departure.stationName);
        arrStation.setText("" + ManageBusActivity.selectedBus.arrival.stationName);

        acCheckBox = findViewById(R.id.acbox);
        wifiCheckBox = findViewById(R.id.wifibox);
        toiletCheckBox = findViewById(R.id.toiletbox);
        lcdCheckBox = findViewById(R.id.lcdtvbox);
        coolboxCheckBox = findViewById(R.id.coolboxbox);
        lunchCheckBox = findViewById(R.id.lunchbox);
        baggageCheckBox = findViewById(R.id.baggagebox);
        electricCheckBox = findViewById(R.id.socketbox);
        if(ManageBusActivity.selectedBus.facilities.contains(Facility.AC)){acCheckBox.setChecked(true);}
        if(ManageBusActivity.selectedBus.facilities.contains(Facility.WIFI)){wifiCheckBox.setChecked(true);}
        if(ManageBusActivity.selectedBus.facilities.contains(Facility.TOILET)){toiletCheckBox.setChecked(true);}
        if(ManageBusActivity.selectedBus.facilities.contains(Facility.LCD_TV)){lcdCheckBox.setChecked(true);}
        if(ManageBusActivity.selectedBus.facilities.contains(Facility.COOL_BOX)){coolboxCheckBox.setChecked(true);}
        if(ManageBusActivity.selectedBus.facilities.contains(Facility.LUNCH)){lunchCheckBox.setChecked(true);}
        if(ManageBusActivity.selectedBus.facilities.contains(Facility.LARGE_BAGGAGE)){baggageCheckBox.setChecked(true);}
        if(ManageBusActivity.selectedBus.facilities.contains(Facility.ELECTRIC_SOCKET)){electricCheckBox.setChecked(true);}

        addScheduleBtn = findViewById(R.id.addscedbtn);
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
}