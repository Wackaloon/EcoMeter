package com.alexanderageychenko.ecometer.Fragments.add;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
class AddAdapter extends RecyclerView.Adapter<AddAdapter.ViewHolder> {
    private ArrayList<Meter> data = new ArrayList<>();
    private Context context;
    private Listener listener;
    private String blockCharacterSet = "0123456789";
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && !blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    AddAdapter(Context context) {
        this.context = context;
    }

    public void setData(@Nullable Collection<Meter> data) {
        this.data.clear();
        if (data != null)
            this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public AddAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_add, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Meter item = data.get(position);
        holder.name.setText(item.getFullName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        holder.lastValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    try {
                        data.get(holder.getAdapterPosition()).setNewValue(Long.parseLong(holder.lastValue.getText().toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });
        holder.lastValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    data.get(holder.getAdapterPosition()).setNewValue(Long.parseLong(holder.lastValue.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.lastValue.setFilters(new InputFilter[]{filter});
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    void setListener(Listener listener) {
        this.listener = listener;
    }

    public ArrayList<Meter> getItems() {
        for (Meter m : data) {
            m.applyNewValue();
        }
        return data;
    }

    interface Listener {
        void onItemClick(Meter item);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        EditText lastValue;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            lastValue = (EditText) itemView.findViewById(R.id.last_value);
        }
    }
}
