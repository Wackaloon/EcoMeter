package com.alexanderageychenko.ecometer.Fragments.home;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexanderageychenko.ecometer.Model.Meter;
import com.alexanderageychenko.ecometer.Model.MeterType;
import com.alexanderageychenko.ecometer.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by alexanderaheychenko 13.09.16.
 */
class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private List<Meter> data = new ArrayList<>();
    private Context context;
    private Listener listener;

    HomeAdapter(Context context) {
        this.context = context;
    }

    public void setData(@Nullable Collection<Meter> data) {
        this.data.clear();
        if (data != null)
            this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_home, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Meter item = data.get(position);
        holder.name.setText(item.getFullName());
        holder.lastValue.setText(item.getLastValue());
        holder.lastDate.setText(item.getLastValueDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClick(item);
            }
        });
        MeterType type = item.getType();
        if (type == null) {
            holder.imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_public_white_48dp));
        } else {
            switch (type) {
                case GAS: {
                    holder.back.setBackgroundColor(context.getResources().getColor(R.color.main_orange));
                    holder.imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_whatshot_white_48dp));
                    break;
                }
                case WATER: {
                    holder.back.setBackgroundColor(context.getResources().getColor(R.color.main_blue));
                    holder.imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_invert_colors_white_48dp));
                    break;
                }
                case ELECTRICITY: {
                    holder.back.setBackgroundColor(context.getResources().getColor(R.color.main_yellow));
                    holder.imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_flash_on_white_48dp));
                    break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onItemClick(Meter item);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        View back;
        TextView name, lastValue, lastDate, meanValuePerDay, meanValuePerMonth;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            back = itemView.findViewById(R.id.background);
            name = (TextView) itemView.findViewById(R.id.name);
            lastValue = (TextView) itemView.findViewById(R.id.last_value);
            lastDate = (TextView) itemView.findViewById(R.id.last_date);
        }
    }
}
