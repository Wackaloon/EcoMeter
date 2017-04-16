package com.alexanderageychenko.ecometer.View.add;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexanderageychenko.ecometer.View.ExFragment;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;
import com.alexanderageychenko.ecometer.Model.Depository.IMetersDepository;
import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.R;

import javax.inject.Inject;


/**
 * Created by alexanderageychenko 13.09.16.
 */
public class AddValueFragment extends ExFragment implements AddValueAdapter.Listener, View.OnClickListener, TextWatcher {
    private static final String TAG = "Home";
    private Button done;
    private IMeter meter;
    private TextView meter_text;
    private TextView last_value;
    private TextView last_date;
    private EditText new_value;
    private ImageView meter_image;
    @Inject
    IMetersDepository iMetersDepository;

    public AddValueFragment() {
        Dagger.get().getInjector().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        meter = iMetersDepository.getSelectedMeter();
        done = (Button) view.findViewById(R.id.done);
        meter_text = (TextView) view.findViewById(R.id.name);
        meter_text.setText(meter.getFullName());
        last_value = (TextView) view.findViewById(R.id.last_value);
        last_date = (TextView) view.findViewById(R.id.last_date);
        last_date.setText(meter.getLastValueDate());
        last_value.setText(meter.getLastValue());
        meter_image = (ImageView) view.findViewById(R.id.image);
        meter_image.setBackgroundColor(getResources().getColor(meter.getType().getBackgroundResource()));
        meter_image.setImageResource(meter.getType().getImageResource());
        new_value = (EditText) view.findViewById(R.id.new_value_input);
        done.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        String last = meter.getLastValue();
        new_value.setText(last);
        new_value.addTextChangedListener(this);
    }

    @Override
    public void onPause() {
        new_value.removeTextChangedListener(this);
        new_value.setText("");
        super.onPause();

    }

    @Override
    public void onItemClick(IMeter item) {
    }

    @Override
    public void onClick(View view) {
        if (view == done) {
            meter.applyValue();
            iMetersDepository.addMeter(meter);
            getActivity().onBackPressed();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        try {
            meter.setValue(Long.parseLong(new_value.getText().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
