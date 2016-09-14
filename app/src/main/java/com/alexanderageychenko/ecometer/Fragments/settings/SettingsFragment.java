package com.alexanderageychenko.ecometer.Fragments.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alexanderageychenko.ecometer.Data.Depository;
import com.alexanderageychenko.ecometer.Fragments.ExFragment;
import com.alexanderageychenko.ecometer.Logic.DialogBuilder;
import com.alexanderageychenko.ecometer.MainApplication;
import com.alexanderageychenko.ecometer.Model.DeleteMeterListener;
import com.alexanderageychenko.ecometer.Model.Meter;
import com.alexanderageychenko.ecometer.R;


/**
 * Created by alexanderageychenko 13.09.16.
 */
public class SettingsFragment extends ExFragment implements SettingsAdapter.Listener, View.OnClickListener {
    private static final String TAG = "Home";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SettingsAdapter homeAdapter;
    private Button add;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.home_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        homeAdapter = new SettingsAdapter(getActivity());
        homeAdapter.setListener(this);
        recyclerView.setAdapter(homeAdapter);
        add = (Button) view.findViewById(R.id.add);
        add.setOnClickListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        homeAdapter.setData(Depository.getInstance().getMeters());

//        Snackbar.make(recyclerView, "Data was refreshed", Snackbar.LENGTH_SHORT)
//                .setAction("Action", null).show();
    }

    @Override
    public void onPause() {
        Depository.getInstance().saveToDB();
        super.onPause();
    }

    @Override
    public void onEditClick(Meter item) {
        MainApplication.getInstance().sendBroadcast(new Intent(MainApplication.FILTER_ACTION_NAME)
                .putExtra(MainApplication.SIGNAL_NAME, MainApplication.SIGNAL_TYPE.OPEN_EDIT_METER)
                .putExtra("id", Depository.getInstance().getMeters().indexOf(item)));
    }

    @Override
    public void onDeleteClick(final Meter item) {
        DialogBuilder.getDeleteMeterDialog(getActivity(), new DeleteMeterListener() {
            @Override
            public void delete() {
                Depository.getInstance().getMeters().remove(item);
                homeAdapter.setData(Depository.getInstance().getMeters());
            }
        }).show();

    }

    @Override
    public void onClick(View view) {
        if (view == add){
            MainApplication.getInstance().sendBroadcast(new Intent(MainApplication.FILTER_ACTION_NAME)
                    .putExtra(MainApplication.SIGNAL_NAME, MainApplication.SIGNAL_TYPE.OPEN_CREATE_METER));
        }
    }
}
