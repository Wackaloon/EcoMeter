package com.alexanderageychenko.ecometer.View.details;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexanderageychenko.ecometer.Model.Entity.MeterValue;
import com.alexanderageychenko.ecometer.R;

import java.util.ArrayList;

/**
 * Created by alexanderaheychenko 13.09.16.
 */
class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {
    private ArrayList<MeterValue> data = new ArrayList<>();
    private Context context;
    private Listener listener;

    DetailsAdapter(Context context) {
        this.context = context;
    }

    public void setData(@Nullable ArrayList<MeterValue> data) {
        this.data.clear();
        if (data != null)
            this.data.addAll(data);

        notifyDataSetChanged();
    }

    @Override
    public DetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.list_item_details, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MeterValue item = data.get(position);
        holder.lastValue.setText(item.getValueString());
        holder.lastDate.setText(item.getDateString());
        holder.lastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onDateClick(item);
            }
        });
        holder.lastValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onValueClick(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onValueClick(MeterValue item);

        void onDateClick(MeterValue item);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView lastValue, lastDate;

        ViewHolder(View itemView) {
            super(itemView);
            lastValue = (TextView) itemView.findViewById(R.id.last_value);
            lastDate = (TextView) itemView.findViewById(R.id.last_date);
        }
    }
}
