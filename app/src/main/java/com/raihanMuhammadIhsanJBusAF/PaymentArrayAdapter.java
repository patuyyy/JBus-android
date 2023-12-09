package com.raihanMuhammadIhsanJBusAF;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import com.raihanMuhammadIhsanJBusAF.model.Bus;
import com.raihanMuhammadIhsanJBusAF.model.Payment;
import com.raihanMuhammadIhsanJBusAF.request.BaseApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentArrayAdapter extends ArrayAdapter<Payment> {

    private BaseApiService mApiService;
    private Context mContext;
    private Bus selectedBus;
    private Payment currentPayment;

    // invoke the suitable constructor of the ArrayAdapter class
    public PaymentArrayAdapter(@NonNull Context context, List<Payment> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.payment_layout, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        currentPayment = getItem(position);

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView busText = currentItemView.findViewById(R.id.busname);
        TextView departureStation = currentItemView.findViewById(R.id.departure);
        TextView seat = currentItemView.findViewById(R.id.seats);
        TextView status = currentItemView.findViewById(R.id.status);
        busText.setText(MainActivity.listBus.get(currentPayment.getBusId() - 1).name);
        departureStation.setText(currentPayment.departureDate.toString());
        seat.setText(currentPayment.busSeats.toString());
        status.setText(currentPayment.status.toString());

        return currentItemView;
    }
    private void handleBus() {
        mApiService.getBus(currentPayment.getBusId()).enqueue(new Callback<Bus>() {
            @Override
            public void onResponse(Call<Bus> call,
                                   Response<Bus> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Bus res = response.body();
                selectedBus = res;
                // if success finish this activity (refresh page)
            }
            @Override
            public void onFailure(Call<Bus> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

