package com.alexanderageychenko.ecometer.Fragments.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alexanderageychenko.ecometer.Data.Depository;
import com.alexanderageychenko.ecometer.Fragments.ExFragment;
import com.alexanderageychenko.ecometer.Logic.DialogBuilder;
import com.alexanderageychenko.ecometer.Model.DetailsEditListener;
import com.alexanderageychenko.ecometer.Model.Meter;
import com.alexanderageychenko.ecometer.Model.MeterPosition;
import com.alexanderageychenko.ecometer.Model.MeterType;
import com.alexanderageychenko.ecometer.Model.MeterValue;
import com.alexanderageychenko.ecometer.R;

import static android.support.design.R.styleable.Spinner;

/**
 * Created by alexanderageychenko on 9/14/16.
 */

public class EditFragment extends ExFragment implements View.OnClickListener, TextView.OnEditorActionListener {
    private static final String ARG_METER_NUMBER = "argMeterNumber";
    private Meter meter = null;
    private android.widget.Spinner type;
    private android.widget.Spinner position;
    private EditText nameInput;
    private boolean create = false;
    private Button done;
    /**
     * Create a new DetailsFragment
     * @param type The string of meter type
     */
    public static EditFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(ARG_METER_NUMBER, type);

        EditFragment fragment = new EditFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        type = (Spinner) view.findViewById(R.id.spinner_type);
        position = (Spinner) view.findViewById(R.id.spinner_position);
        done = (Button) view.findViewById(R.id.done);
        done.setOnClickListener(this);
        Bundle args = getArguments();
        int typeNumber = args.containsKey(ARG_METER_NUMBER) ? args.getInt(ARG_METER_NUMBER) : -1;

        MeterType typeM = null;
        MeterPosition positionM = null;
        if (typeNumber != -1){
            meter = Depository.getInstance().getMeters().get(typeNumber);
            ((TextView) view.findViewById(R.id.title)).setText("Edit Meter");
            create = false;
        }else{
            meter = new Meter(MeterType.WATER, MeterPosition.KITCHEN, null);
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
                meter.setMeterType((MeterType)type.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        position.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                meter.setMeterPosition((MeterPosition)position.getSelectedItem());
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
        if (view == done){
            try {
                if (!nameInput.getText().toString().isEmpty())
                    meter.setName(nameInput.getText().toString());
                else
                    meter.setName(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (create){
                Depository.getInstance().addNewMeter(meter);
            }
            Depository.getInstance().saveToDB();
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
