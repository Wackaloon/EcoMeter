package com.alexanderageychenko.ecometer.Fragments.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexanderageychenko.ecometer.Data.Depository;
import com.alexanderageychenko.ecometer.Fragments.ExFragment;
import com.alexanderageychenko.ecometer.Logic.DialogBuilder;
import com.alexanderageychenko.ecometer.Model.DetailsEditListener;
import com.alexanderageychenko.ecometer.Model.Meter;
import com.alexanderageychenko.ecometer.Model.MeterType;
import com.alexanderageychenko.ecometer.Model.MeterValue;
import com.alexanderageychenko.ecometer.R;

/**
 * Created by alexanderageychenko on 9/14/16.
 */

public class DetailsFragment extends ExFragment implements DetailsAdapter.Listener {
    private static final String ARG_METER_NUMBER = "argMeterNumber";
    private ImageView imageView;
    private Meter meter = null;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DetailsAdapter detailsAdapter;
    /**
     * Create a new DetailsFragment
     * @param type The string of meter type
     */
    public static DetailsFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(ARG_METER_NUMBER, type);

        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = (ImageView) view.findViewById(R.id.image);

        Bundle args = getArguments();
        int typeNumber = args.containsKey(ARG_METER_NUMBER) ? args.getInt(ARG_METER_NUMBER) : -1;

        MeterType type = null;
        if (typeNumber != -1){
            meter = Depository.getInstance().getMeters().get(typeNumber);
            type = meter.getType();
        }


        if (type == null) {
            imageView.setBackgroundColor(getResources().getColor(R.color.main_green_A0));
            imageView.setImageResource(R.drawable.ic_public_white_48dp);
        } else {
            switch (type) {
                case GAS: {
                    imageView.setBackgroundColor(getResources().getColor(R.color.main_orange));
                    imageView.setImageResource(R.drawable.ic_whatshot_white_48dp);
                    break;
                }
                case WATER: {
                    imageView.setBackgroundColor(getResources().getColor(R.color.main_blue));
                    imageView.setImageResource(R.drawable.ic_invert_colors_white_48dp);
                    break;
                }
                case ELECTRICITY: {
                    imageView.setBackgroundColor(getResources().getColor(R.color.main_yellow));
                    imageView.setImageResource(R.drawable.ic_flash_on_white_48dp);
                    break;
                }
            }
        }

        if (meter != null){
            ((TextView) view.findViewById(R.id.name)).setText(meter.getFullName());
            ((TextView) view.findViewById(R.id.mean_day_text)).setText(meter.getMeanValuePerDayString());
            ((TextView) view.findViewById(R.id.mean_month_text)).setText(meter.getMeanValuePerMonthString());
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.details_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        detailsAdapter = new DetailsAdapter(getActivity());
        detailsAdapter.setListener(this);
        recyclerView.setAdapter(detailsAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        detailsAdapter.setData(meter.getAllValues());
    }

    @Override
    public void onValueClick(MeterValue item) {
        DialogBuilder.getEditDetailsDialog(getActivity(), meter, meter.getAllValues().indexOf(item), new DetailsEditListener() {
            @Override
            public void valueWasEdited() {
                detailsAdapter.setData(meter.getAllValues());
            }
        }).show();
    }

    @Override
    public void onDateClick(MeterValue item) {
        DialogBuilder.getExitEditDateDialog(getActivity(), meter, meter.getAllValues().indexOf(item),new DetailsEditListener() {
            @Override
            public void valueWasEdited() {
                detailsAdapter.setData(meter.getAllValues());
            }
        }).show();
    }
}
