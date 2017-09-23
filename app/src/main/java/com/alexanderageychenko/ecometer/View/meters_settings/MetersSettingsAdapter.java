package com.alexanderageychenko.ecometer.View.meters_settings;

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
 * Created by alexanderaheychenko 13.09.16.
 */
class MetersSettingsAdapter extends RecyclerView.Adapter<MetersSettingsAdapter.ViewHolder> {
    private List<IMeter> data = new ArrayList<>();
    private Context context;
    private Listener listener;

    MetersSettingsAdapter(Context context) {
        this.context = context;
    }

    public void setData(@Nullable Collection<IMeter> data) {
        this.data.clear();
        if (data != null)
            this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public MetersSettingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_settings, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position >= data.size()){
            bindAdd(holder);
            return;
        }else{
            holder.itemView.setOnClickListener(null);
            holder.edit.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
        }
        final IMeter item = data.get(position);
        holder.name.setText(item.getFullName());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onEditClick(item);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onDeleteClick(item);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.onItemClick(item);
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.onItemClick(item);
            }
        });
        MeterType type = item.getType();
        if (type == null) {
            holder.imageView.setBackgroundColor(context.getResources().getColor(R.color.main_green_A0));
            holder.imageView.setImageResource(R.drawable.ic_public_white_48dp);
        } else {
            switch (type) {
                case GAS: {
                    holder.imageView.setBackgroundColor(context.getResources().getColor(R.color.main_orange));
                    holder.imageView.setImageResource(R.drawable.gas_image);
                    break;
                }
                case WATER: {
                    holder.imageView.setBackgroundColor(context.getResources().getColor(R.color.main_blue));
                    holder.imageView.setImageResource(R.drawable.water_image);
                    break;
                }
                case ELECTRICITY: {
                    holder.imageView.setBackgroundColor(context.getResources().getColor(R.color.main_yellow));
                    holder.imageView.setImageResource(R.drawable.electro_image);
                    break;
                }
            }
        }
    }

    private void bindAdd(ViewHolder holder){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null) {
                    listener.onAddClick();
                }
            }
        });
        holder.name.setText("ADD NEW");
        holder.edit.setVisibility(View.GONE);
        holder.delete.setVisibility(View.GONE);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.onAddClick();
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.onAddClick();
            }
        });

        holder.imageView.setBackgroundColor(context.getResources().getColor(R.color.main_green_A0));
        holder.imageView.setImageResource(R.drawable.ic_public_white_48dp);
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }


    void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onEditClick(IMeter item);

        void onAddClick();

        void onDeleteClick(IMeter item);
        void onItemClick(IMeter item);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, edit, delete;
        TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            edit = (ImageView) itemView.findViewById(R.id.image_edit);
            delete = (ImageView) itemView.findViewById(R.id.image_delete);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
