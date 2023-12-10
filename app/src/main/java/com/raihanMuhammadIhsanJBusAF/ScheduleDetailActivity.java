package com.raihanMuhammadIhsanJBusAF;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.raihanMuhammadIhsanJBusAF.model.BaseResponse;
import com.raihanMuhammadIhsanJBusAF.model.Bus;
import com.raihanMuhammadIhsanJBusAF.model.BusType;
import com.raihanMuhammadIhsanJBusAF.model.Facility;
import com.raihanMuhammadIhsanJBusAF.model.Payment;
import com.raihanMuhammadIhsanJBusAF.model.Schedule;
import com.raihanMuhammadIhsanJBusAF.model.Station;
import com.raihanMuhammadIhsanJBusAF.request.BaseApiService;
import com.raihanMuhammadIhsanJBusAF.request.UtilsApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleDetailActivity extends AppCompatActivity {

    private BaseApiService mApiService;
    private Context mContext;
    private TextView busName, capacity, price = null;
    private TextView deptStation, arrStation, seatText= null;
    private Spinner scheduleSpinner, seatSpinner;
    private List<Schedule> scheduleList = new ArrayList<>();
    private String selectedSeat = null;
    private Schedule selectedSchedule = null;
    private List<String> scheduleTimeList = new ArrayList<>();
    private List<String> seatAvaibleList = new ArrayList<>();
    private List<String> seatToOrderList = new ArrayList<>();

    private Button makeBookingBtn, addSeatBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){
        }
        mContext = this;
        mApiService = UtilsApi.getApiService();

        busName = findViewById(R.id.busname);
        capacity = findViewById(R.id.capacity);
        price = findViewById(R.id.price);
        busName.setText(MainActivity.selectedBusTemp.name);
        capacity.setText(MainActivity.selectedBusTemp.capacity + " seats");
        price.setText("Rp." + Double.toString(MainActivity.selectedBusTemp.price.price) + " /seat");

        deptStation = findViewById(R.id.departuretxt);
        arrStation = findViewById(R.id.arrivaltxt);
        deptStation.setText("" + MainActivity.selectedBusTemp.departure.stationName);
        arrStation.setText("" + MainActivity.selectedBusTemp.arrival.stationName);

        scheduleSpinner = findViewById(R.id.schedule_dropdown);
        seatSpinner = findViewById(R.id.seat_dropdown);


        scheduleList = MainActivity.selectedBusTemp.schedules;
        for(Schedule sced : scheduleList) {
            scheduleTimeList.add(sced.departureSchedule.toString());
        }
        ArrayAdapter scedBus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, scheduleTimeList);
        scedBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        scheduleSpinner.setAdapter(scedBus);
        AdapterView.OnItemSelectedListener scedOISL = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                // mengisi field selectedBusType sesuai dengan item yang dipilih
                selectedSchedule = scheduleList.get(position);

                for(Map.Entry<String, Boolean> seat : selectedSchedule.seatAvailability.entrySet()) {
                    if(seat.getValue().equals(true)){
                        seatAvaibleList.add(seat.getKey());
                    }
                }
                AdapterView.OnItemSelectedListener seatOISL = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                        // mengisi field selectedBusType sesuai dengan item yang dipilih
                        selectedSeat = seatAvaibleList.get(position);

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
                ArrayAdapter seatBus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, seatAvaibleList);
                seatBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                seatSpinner.setAdapter(seatBus);
                seatSpinner.setOnItemSelectedListener(seatOISL);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        scheduleSpinner.setOnItemSelectedListener(scedOISL);

        addSeatBtn = findViewById(R.id.addbtn);
        seatText = findViewById(R.id.seattoorder);
        addSeatBtn.setOnClickListener(v->{
            seatToOrderList.add(selectedSeat);
            seatText.setText(seatToOrderList.toString());
        });

        makeBookingBtn = findViewById(R.id.makebookingbtn);
        makeBookingBtn.setOnClickListener(view -> {
            handleMakeBooking();
        });
    }
    protected void handleMakeBooking() {
        // handling empty field
        int buyerIdS = LoginActivity.loggedAccount.id;
        int renterIdS = MainActivity.selectedBusTemp.accountId;
        int busIdS = MainActivity.selectedBusTemp.id;
        List<String> busSeatS = new ArrayList<>();
        busSeatS.add(selectedSeat);
        String departureDateS = selectedSchedule.departureSchedule.toString();

        if (selectedSeat == null || selectedSchedule == null) {
            Toast.makeText(mContext, "Field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.makeBooking(buyerIdS, renterIdS, busIdS, seatToOrderList,departureDateS).enqueue(new Callback<BaseResponse<Payment>>() {
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

                // if success finish this activity (back to login activity)
                if (res.success) {
                    LoginActivity.loggedAccount.balance -= MainActivity.selectedBusTemp.price.price *
                    seatToOrderList.size();
                    finish();
                    moveActivity(getApplicationContext(), MainActivity.class);
                    overridePendingTransition(0,0);
                }
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                if (t instanceof IOException) {
                    // Network error
                    Toast.makeText(mContext, "Network error", Toast.LENGTH_SHORT).show();
                } else {
                    // Conversion error (e.g., parsing JSON)
                }
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