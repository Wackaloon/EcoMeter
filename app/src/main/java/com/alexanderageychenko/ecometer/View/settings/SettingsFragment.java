package com.alexanderageychenko.ecometer.View.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alexanderageychenko.ecometer.View.ExFragment;
import com.alexanderageychenko.ecometer.Tools.DialogBuilder;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;
import com.alexanderageychenko.ecometer.MainApplication;
import com.alexanderageychenko.ecometer.Model.Listener.DeleteMeterListener;
import com.alexanderageychenko.ecometer.Model.Depository.IMetersDepository;
import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.R;

import javax.inject.Inject;


/**
 * Created by alexanderageychenko 13.09.16.
 */
public class SettingsFragment extends ExFragment implements SettingsAdapter.Listener, View.OnClickListener {
    private static final String TAG = "Home";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SettingsAdapter homeAdapter;
    private Button add;

    @Inject
    IMetersDepository iMetersDepository;

    public SettingsFragment() {
        Dagger.get().getInjector().inject(this);
    }
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
        homeAdapter.setData(iMetersDepository.getMeters());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onEditClick(IMeter item) {
        iMetersDepository.selectMeter(item.getId());
        MainApplication.getInstance().sendBroadcast(new Intent(MainApplication.FILTER_ACTION_NAME)
                .putExtra(MainApplication.SIGNAL_NAME, MainApplication.SIGNAL_TYPE.OPEN_EDIT_METER));
    }

    @Override
    public void onDeleteClick(final IMeter item) {
        DialogBuilder.getDeleteMeterDialog(getActivity(), new DeleteMeterListener() {
            @Override
            public void delete() {
                iMetersDepository.getMeters().remove(item);
                homeAdapter.setData(iMetersDepository.getMeters());
            }
        }).show();

    }

    @Override
    public void onItemClick(IMeter item) {
//        iMetersDepository.selectMeter(item.getId());
//        MainApplication.getInstance().sendBroadcast(new Intent(MainApplication.FILTER_ACTION_NAME)
//                .putExtra(MainApplication.SIGNAL_NAME, MainApplication.SIGNAL_TYPE.OPEN_DETAILS));
    }

    @Override
    public void onClick(View view) {
        if (view == add){
            iMetersDepository.selectMeter(null);
            MainApplication.getInstance().sendBroadcast(new Intent(MainApplication.FILTER_ACTION_NAME)
                    .putExtra(MainApplication.SIGNAL_NAME, MainApplication.SIGNAL_TYPE.OPEN_CREATE_METER));
        }
    }
}
