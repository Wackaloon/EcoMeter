package com.alexanderageychenko.ecometer.View.home;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.Model.Entity.MeterType;
import com.alexanderageychenko.ecometer.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Alexander on 16.04.2017
 */
class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private List<IMeter> data = new ArrayList<>();
    private Context context;
    private Listener listener;

    HomeAdapter(Context context) {
        this.context = context;
    }

    public void setData(@Nullable Collection<IMeter> data) {
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final IMeter item = data.get(position);
        holder.name.setText(item.getFullName());
        holder.lastValue.setText(item.getLastValue());
        holder.lastDate.setText(item.getLastValueDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClick(item, holder);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onImageClick(item, holder);
            }
        });
        MeterType type = item.getType();
        if (type == null) {
            holder.imageView.setBackgroundColor(context.getResources().getColor(R.color.main_green_A0));
            holder.imageView.setImageResource(R.drawable.ic_public_white_48dp);
        } else {
            holder.imageView.setBackgroundColor(context.getResources().getColor(type.getBackgroundResource()));
            holder.imageView.setImageResource(type.getImageResource());
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
        void onItemClick(IMeter item, HomeAdapter.ViewHolder viewHolder);

        void onImageClick(IMeter item, HomeAdapter.ViewHolder viewHolder);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, lastValue, lastDate;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            lastValue = (TextView) itemView.findViewById(R.id.last_value);
            lastDate = (TextView) itemView.findViewById(R.id.last_date);
        }
    }
}
