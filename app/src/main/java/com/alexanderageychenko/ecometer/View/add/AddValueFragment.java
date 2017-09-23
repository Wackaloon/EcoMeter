package com.alexanderageychenko.ecometer.View.add;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexanderageychenko.ecometer.Model.Depository.IMetersDepository;
import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.R;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;
import com.alexanderageychenko.ecometer.Model.ExContainers.ExFragment;
import com.alexanderageychenko.ecometer.View.custom_view.MultiplePickerView;

import javax.inject.Inject;


/**
 * Created by alexanderageychenko 13.09.16.
 */
public class AddValueFragment extends ExFragment implements AddValueAdapter.Listener, View.OnClickListener {
    private static final String TAG = "Home";
    private Button done;
    private IMeter meter;
    private TextView meter_text;
    private TextView last_value;
    private TextView last_date;
    private View plus;
    private ImageView meter_image;
    MultiplePickerView mPicker;
    @Inject
    IMetersDepository iMetersDepository;
    private Long meterId = null;

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
        meter = iMetersDepository.getMeter(meterId);
        done = (Button) view.findViewById(R.id.done);
        plus = view.findViewById(R.id.plus);
        plus.setVisibility(View.GONE);
        meter_text = (TextView) view.findViewById(R.id.name);
        meter_text.setText(meter.getFullName());
        last_value = (TextView) view.findViewById(R.id.last_value);
        last_date = (TextView) view.findViewById(R.id.last_date);
        last_date.setText(meter.getLastValueDate());
        last_value.setText(meter.getLastValue());
        meter_image = (ImageView) view.findViewById(R.id.image);
        meter_image.setBackgroundColor(getResources().getColor(meter.getType().getBackgroundResource()));
        meter_image.setImageResource(meter.getType().getImageResource());
        done.setOnClickListener(this);

        mPicker = (MultiplePickerView) view.findViewById(R.id.mPicker);

        String last = meter.getLastValue();
        mPicker.setInitialValue(Long.valueOf(last));
        mPicker.setListener(new MultiplePickerView.OnValueSelected() {
            @Override
            public void valueSelected(Long value) {
                Log.d("MultiplePickerView", "valueSelected: " + value);
                meter.setValue(value);
            }
        });
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

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }
}
