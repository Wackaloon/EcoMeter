package com.alexanderageychenko.ecometer.Fragments.settings;

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
class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {
    private List<Meter> data = new ArrayList<>();
    private Context context;
    private Listener listener;

    SettingsAdapter(Context context) {
        this.context = context;
    }

    public void setData(@Nullable Collection<Meter> data) {
        this.data.clear();
        if (data != null)
            this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public SettingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_settings, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Meter item = data.get(position);
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
        MeterType type = item.getType();
        if (type == null) {
            holder.imageView.setBackgroundColor(context.getResources().getColor(R.color.main_green_A0));
            holder.imageView.setImageResource(R.drawable.ic_public_white_48dp);
        } else {
            switch (type) {
                case GAS: {
                    holder.imageView.setBackgroundColor(context.getResources().getColor(R.color.main_orange));
                    holder.imageView.setImageResource(R.drawable.ic_whatshot_white_48dp);
                    break;
                }
                case WATER: {
                    holder.imageView.setBackgroundColor(context.getResources().getColor(R.color.main_blue));
                    holder.imageView.setImageResource(R.drawable.ic_invert_colors_white_48dp);
                    break;
                }
                case ELECTRICITY: {
                    holder.imageView.setBackgroundColor(context.getResources().getColor(R.color.main_yellow));
                    holder.imageView.setImageResource(R.drawable.ic_flash_on_white_48dp);
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
        void onEditClick(Meter item);
        void onDeleteClick(Meter item);
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