package com.alexanderageychenko.ecometer.View.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alexanderageychenko.ecometer.View.ExFragment;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;
import com.alexanderageychenko.ecometer.Model.Depository.IMetersDepository;
import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.Model.Entity.Meter;
import com.alexanderageychenko.ecometer.Model.Entity.MeterPosition;
import com.alexanderageychenko.ecometer.Model.Entity.MeterType;
import com.alexanderageychenko.ecometer.R;

import javax.inject.Inject;

/**
 * Created by alexanderageychenko on 9/14/16.
 */

public class EditMeterFragment extends ExFragment implements View.OnClickListener, TextView.OnEditorActionListener {
    private static final String ARG_METER_NUMBER = "argMeterNumber";
    private IMeter meter = null;
    private android.widget.Spinner type;
    private android.widget.Spinner position;
    private EditText nameInput;
    private boolean create = false;
    private Button done;
    @Inject
    IMetersDepository iMetersDepository;
    private Long meterId = null;

    public EditMeterFragment() {
        Dagger.get().getInjector().inject(this);
    }

    public static EditMeterFragment newInstance() {
        EditMeterFragment fragment = new EditMeterFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        type = (Spinner) view.findViewById(R.id.spinner_type);
        position = (Spinner) view.findViewById(R.id.spinner_position);
        done = (Button) view.findViewById(R.id.done);
        done.setOnClickListener(this);

        MeterType typeM = null;
        MeterPosition positionM = null;
        if (meterId != null) {
            meter = iMetersDepository.getMeter(meterId);
            ((TextView) view.findViewById(R.id.title)).setText("Edit Meter");
            create = false;
        } else {
            meter = new Meter(MeterType.WATER, MeterPosition.KITCHEN, null, (long) iMetersDepository.getMeters().size() + 1);
            ((TextView) view.findViewById(R.id.title)).setText("Create New Meter");
            create = true;
        }
        typeM = meter.getType();
        positionM = meter.getPosition();

        ArrayAdapter<MeterType> adapterType = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, MeterType.values());
        ArrayAdapter<MeterPosition> adapterPosition = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, MeterPosition.values());
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterPosition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapterType);
        position.setAdapter(adapterPosition);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                meter.setMeterType((MeterType) type.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        position.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                meter.setMeterPosition((MeterPosition) position.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        type.setSelection(typeM.ordinal());
        position.setSelection(positionM.ordinal());
        nameInput = (EditText) view.findViewById(R.id.name_input);
        nameInput.setText(meter.getName());
        nameInput.setOnEditorActionListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view == done) {
            try {
                if (!nameInput.getText().toString().isEmpty())
                    meter.setName(nameInput.getText().toString());
                else
                    meter.setName(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (create) {
                iMetersDepository.addMeter(meter);
            }
            getActivity().onBackPressed();
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            nameInput.clearFocus();
        }
        return false;
    }
}
