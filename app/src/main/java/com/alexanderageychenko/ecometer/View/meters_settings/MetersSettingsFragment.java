package com.alexanderageychenko.ecometer.View.meters_settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexanderageychenko.ecometer.MainActivity;
import com.alexanderageychenko.ecometer.Model.Depository.IMetersDepository;
import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.Model.Listener.DeleteMeterListener;
import com.alexanderageychenko.ecometer.R;
import com.alexanderageychenko.ecometer.Tools.DialogBuilder;
import com.alexanderageychenko.ecometer.Tools.Navigator.MainNavigator;
import com.alexanderageychenko.ecometer.Tools.Navigator.Navigator;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;
import com.alexanderageychenko.ecometer.Model.ExContainers.ExFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.alexanderageychenko.ecometer.Tools.Navigator.MainScreenType.CreateMeterScreen;
import static com.alexanderageychenko.ecometer.Tools.Navigator.MainScreenType.DetailsScreen;
import static com.alexanderageychenko.ecometer.Tools.Navigator.MainScreenType.EditMeterScreen;


/**
 * Created by alexanderageychenko 13.09.16
 */
public class MetersSettingsFragment extends ExFragment implements MetersSettingsAdapter.Listener {
    private static final String TAG = "Home";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MetersSettingsAdapter homeAdapter;

    @Inject
    IMetersDepository iMetersDepository;
    @Inject
    MainNavigator mainNavigator;
    private Disposable metersSuscriber;

    public MetersSettingsFragment() {
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
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        homeAdapter = new MetersSettingsAdapter(getActivity());
        homeAdapter.setListener(this);
        recyclerView.setAdapter(homeAdapter);
        super.onViewCreated(view, savedInstanceState);
    }

    private Function<Collection<IMeter>, Collection<IMeter>> sortFunc = iMeters -> {
        ArrayList<IMeter> list = new ArrayList<>(iMeters);
        Collections.sort(list, (meter, t1) -> meter.getId().compareTo(t1.getId()));
        return list;
    };
    private Consumer<Collection<IMeter>> consumer = new Consumer<Collection<IMeter>>() {
        @Override
        public void accept(Collection<IMeter> iMeters) throws Exception {
            homeAdapter.setData(iMeters);
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        metersSuscriber = Observable.just(iMetersDepository.getMeters())
                .map(sortFunc)
                .subscribe(consumer);
    }

    @Override
    public void onStop() {
        if (metersSuscriber != null) metersSuscriber.dispose();
        super.onStop();
    }

    @Override
    public void onEditClick(IMeter meter) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("meter_id", meter.getId());
        homeNavigator.openScreenToStack(new Navigator.Screen<>(EditMeterScreen, params), R.anim.no_anim, R.anim.in_left);
    }

    @Override
    public void onAddClick() {
        HashMap<String, Object> params = new HashMap<>();
        homeNavigator.openScreenToStack(new Navigator.Screen<>(CreateMeterScreen, params), R.anim.no_anim, R.anim.in_left);
    }

    @Override
    public void onDeleteClick(final IMeter item) {
        DialogBuilder.getDeleteMeterDialog(getActivity(), () -> {
            iMetersDepository.getMeters().remove(item);
            homeAdapter.setData(iMetersDepository.getMeters());
        }).show();

    }

    @Override
    public void onItemClick(IMeter item) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("meter_id", item.getId());
        homeNavigator.openScreenToStack(new Navigator.Screen<>(DetailsScreen, params), R.anim.no_anim, R.anim.in_left);
    }

}
