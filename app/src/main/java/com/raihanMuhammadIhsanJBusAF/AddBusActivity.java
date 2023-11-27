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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.raihanMuhammadIhsanJBusAF.model.Account;
import com.raihanMuhammadIhsanJBusAF.model.BaseResponse;
import com.raihanMuhammadIhsanJBusAF.model.BusType;
import com.raihanMuhammadIhsanJBusAF.model.Bus;
import com.raihanMuhammadIhsanJBusAF.model.City;
import com.raihanMuhammadIhsanJBusAF.model.Facility;
import com.raihanMuhammadIhsanJBusAF.model.Station;
import com.raihanMuhammadIhsanJBusAF.request.BaseApiService;
import com.raihanMuhammadIhsanJBusAF.request.UtilsApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBusActivity extends AppCompatActivity {

    private BusType[] busType = BusType.values();
    private BusType selectedBusType;
    private Spinner busTypeSpinner, departureSpinner, arrivalSpinner;
    private BaseApiService mApiService;
    private Context mContext;
    private List<Station> stationList = new ArrayList<>();
    private int selectedDeptStationID;
    private int selectedArrStationID;
    private EditText busName, capacity, price = null;
    private CheckBox acCheckBox, wifiCheckBox, toiletCheckBox, lcdCheckBox;
    private CheckBox coolboxCheckBox, lunchCheckBox, baggageCheckBox, electricCheckBox;
    private List<Facility> selectedFacilities = new ArrayList<>();
    private Button addBtn = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);
        try {
            getSupportActionBar().hide();
        }catch (NullPointerException e){
        }
        mContext = this;
        mApiService = UtilsApi.getApiService();

        busName = findViewById(R.id.busname);
        capacity = findViewById(R.id.capacity);
        price = findViewById(R.id.price);
        departureSpinner = findViewById(R.id.departure_dropdown);
        arrivalSpinner = findViewById(R.id.arrival_dropdown);
        acCheckBox = findViewById(R.id.acbox);
        wifiCheckBox = findViewById(R.id.wifibox);
        toiletCheckBox = findViewById(R.id.toiletbox);
        lcdCheckBox = findViewById(R.id.lcdtvbox);
        coolboxCheckBox = findViewById(R.id.coolboxbox);
        lunchCheckBox = findViewById(R.id.lunchbox);
        baggageCheckBox = findViewById(R.id.baggagebox);
        electricCheckBox = findViewById(R.id.socketbox);

        busTypeSpinner = this.findViewById(R.id.bus_type_dropdown);
        ArrayAdapter adBus = new ArrayAdapter(this, android.R.layout.simple_list_item_1, busType);
        adBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        busTypeSpinner.setAdapter(adBus);
        // menambahkan OISL (OnItemSelectedListener) untuk spinnerbf
        busTypeSpinner.setOnItemSelectedListener(busTypeOISL);
        deptArrHandle();

        addBtn = findViewById(R.id.addbtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facilitiesHandle();
                handleAddBus();
            }
        });
    }
    AdapterView.OnItemSelectedListener busTypeOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            // mengisi field selectedBusType sesuai dengan item yang dipilih
            selectedBusType = busType[position];
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    protected void deptArrHandle() {
        mApiService.getAllStation().enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call,
                                   Response<List<Station>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                stationList = response.body(); //simpan response body ke listStation
                ArrayList<String> stationNameList = new ArrayList<>();
                for(Station station : stationList){
                    stationNameList.add(station.stationName);
                }

                AdapterView.OnItemSelectedListener deptOISL = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                        // mengisi field selectedBusType sesuai dengan item yang dipilih
                        selectedDeptStationID = stationList.get(position).id;
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };

                ArrayAdapter deptBus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, stationNameList);
                deptBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                departureSpinner.setAdapter(deptBus);
                departureSpinner.setOnItemSelectedListener(deptOISL);

                AdapterView.OnItemSelectedListener arrOISL = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                        // mengisi field selectedBusType sesuai dengan item yang dipilih
                        selectedArrStationID = stationList.get(position).id;
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };

                ArrayAdapter arrBus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, stationNameList);
                arrBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                arrivalSpinner.setAdapter(arrBus);
                arrivalSpinner.setOnItemSelectedListener(arrOISL);
                // if success finish this activity (refresh page)
            }
            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void facilitiesHandle(){
        selectedFacilities.clear();
        if(acCheckBox.isChecked()){selectedFacilities.add(Facility.AC);}
        if(wifiCheckBox.isChecked()){selectedFacilities.add(Facility.WIFI);}
        if(toiletCheckBox.isChecked()){selectedFacilities.add(Facility.TOILET);}
        if(lcdCheckBox.isChecked()){selectedFacilities.add(Facility.LCD_TV);}
        if(coolboxCheckBox.isChecked()){selectedFacilities.add(Facility.COOL_BOX);}
        if(lunchCheckBox.isChecked()){selectedFacilities.add(Facility.LUNCH);}
        if(baggageCheckBox.isChecked()){selectedFacilities.add(Facility.LARGE_BAGGAGE);}
        if(electricCheckBox.isChecked()){selectedFacilities.add(Facility.ELECTRIC_SOCKET);}
        return;
    }
    protected void handleAddBus() {
        // handling empty field
        int idS = LoginActivity.loggedAccount.id;
        String nameS = busName.getText().toString();
        int capacityS = Integer.valueOf(capacity.getText().toString());
        List<Facility> facilitiesS = selectedFacilities;
        BusType busTypeS = selectedBusType;
        int priceS = Integer.valueOf(price.getText().toString());
        int stationDepartureIdS = selectedDeptStationID;
        int stationArrivalS = selectedArrStationID;


        if (nameS.isEmpty() || capacity.getText().toString().isEmpty() || price.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.create(idS, nameS, capacityS, facilitiesS,
                busTypeS, priceS, stationDepartureIdS,stationArrivalS).enqueue(new Callback<BaseResponse<Bus>>() {
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
                    moveActivity(getApplicationContext(), ManageBusActivity.class);
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
    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
    private void viewToast(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }

}