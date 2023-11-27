package com.raihanMuhammadIhsanJBusAF;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import com.raihanMuhammadIhsanJBusAF.model.Bus;

public class BusRenterArrayAdapter extends ArrayAdapter<Bus> {

    // invoke the suitable constructor of the ArrayAdapter class
    public BusRenterArrayAdapter(@NonNull Context context, List<Bus> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.bus_view_renter, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Bus currentBus = getItem(position);

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView busText = currentItemView.findViewById(R.id.text_view);
        ImageView calendar = currentItemView.findViewById(R.id.calendar);
        busText.setText(currentBus.name);

        return currentItemView;
    }
}

