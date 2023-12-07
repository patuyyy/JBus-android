package com.raihanMuhammadIhsanJBusAF;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.raihanMuhammadIhsanJBusAF.model.BaseResponse;
import com.raihanMuhammadIhsanJBusAF.model.Bus;
import com.raihanMuhammadIhsanJBusAF.model.BusType;
import com.raihanMuhammadIhsanJBusAF.model.Facility;
import com.raihanMuhammadIhsanJBusAF.model.Station;
import com.raihanMuhammadIhsanJBusAF.request.BaseApiService;
import com.raihanMuhammadIhsanJBusAF.request.UtilsApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBusScheduleActivity extends AppCompatActivity {

    private EditText date_dropdown, time_dropdown;
    private BaseApiService mApiService;
    private Context mContext;
    private List<Bus> busList = new ArrayList<>();
    private int selectedBus;
    private Spinner busNameSpinner;
    private Button addScheduleBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus_schedule);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){
        }

        mContext = this;
        mApiService = UtilsApi.getApiService();

        busNameSpinner = findViewById(R.id.bus_name_dropdown);
        nameBusHandle();

        date_dropdown = findViewById(R.id.date_dropdown);
        time_dropdown = findViewById(R.id.time_dropdown);
        date_dropdown.setInputType(InputType.TYPE_NULL);
        time_dropdown.setInputType(InputType.TYPE_NULL);

        addScheduleBtn = findViewById(R.id.addbtn);

        date_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(date_dropdown);
            }
        });
        time_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog(time_dropdown);
            }
        });
        addScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAddSchedule();
            }
        });

    }
    protected void handleAddSchedule() {
        // handling empty field
        int idS = selectedBus;
        String timeS = date_dropdown.getText().toString() + " " +
                time_dropdown.getText().toString();

        if (date_dropdown.getText().toString().isEmpty() || time_dropdown.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.addSchedule(idS, timeS).enqueue(new Callback<BaseResponse<Bus>>() {
            @Override
            public void onResponse(Call<BaseResponse<Bus>> call,
                                   Response<BaseResponse<Bus>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Bus> res = response.body();
                // if success finish this activity (back to login activity)
                if (res.success) {
                    finish();
                    overridePendingTransition(0,0);
                    moveActivity(getApplicationContext(), AddBusScheduleActivity.class);
                    overridePendingTransition(0,0);
                }
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void nameBusHandle() {
        mApiService.getMyBus(LoginActivity.loggedAccount.id).enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Bus>> call,
                                   Response<List<Bus>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                busList = response.body(); //simpan response body ke listBus
                ArrayList<String> busNameList = new ArrayList<>();
                for(Bus bus : busList){
                    busNameList.add(bus.name);
                }

                AdapterView.OnItemSelectedListener busOISL = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                        // mengisi field selectedBusType sesuai dengan item yang dipilih
                        selectedBus = busList.get(position).id;
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };

                ArrayAdapter bus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, busNameList);
                bus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                busNameSpinner.setAdapter(bus);
                busNameSpinner.setOnItemSelectedListener(busOISL);

                // if success finish this activity (refresh page)
            }
            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showDateDialog(final EditText date_in) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                date_in.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };

        new DatePickerDialog(AddBusScheduleActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void showTimeDialog(final EditText time_dropdown) {
        final Calendar calendar = Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:00.00");
                time_dropdown.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new TimePickerDialog(AddBusScheduleActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),true).show();
    }
    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
    private void viewToast(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
}