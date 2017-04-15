package com.alexanderageychenko.ecometer.Fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexanderageychenko.ecometer.Fragments.ExFragment;
import com.alexanderageychenko.ecometer.Logic.dagger2.Dagger;
import com.alexanderageychenko.ecometer.MainApplication;
import com.alexanderageychenko.ecometer.Model.Depository.IMetersDepository;
import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.R;

import java.util.Collection;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by alexanderageychenko 13.09.16.
 */
public class HomeFragment extends ExFragment implements HomeAdapter.Listener {
    private static final String TAG = "Home";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HomeAdapter homeAdapter;

    @Inject
    IMetersDepository iMetersDepository;
    private Disposable metersSubscription;

    public HomeFragment() {
        Dagger.get().getInjector().inject(this);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.home_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        homeAdapter = new HomeAdapter(getActivity());
        homeAdapter.setListener(this);
        recyclerView.setAdapter(homeAdapter);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        homeAdapter.setData(iMetersDepository.getMeters());
    }

    @Override
    public void onStart() {
        super.onStart();
        metersSubscription = iMetersDepository.getMetersPublisher().observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Collection<IMeter>>() {
            @Override
            public void accept(Collection<IMeter> iMeters) throws Exception {
                homeAdapter.setData(iMetersDepository.getMeters());
            }
        });
            iMetersDepository.requestMeters();
    }

    @Override
    public void onStop() {
        metersSubscription.dispose();
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onItemClick(IMeter item, HomeAdapter.ViewHolder viewHolder) {
        iMetersDepository.selectMeter(item.getId());
        MainApplication.getInstance().sendBroadcast(new Intent(MainApplication.FILTER_ACTION_NAME)
                .putExtra(MainApplication.SIGNAL_NAME, MainApplication.SIGNAL_TYPE.OPEN_DETAILS));
    }

    @Override
    public void onImageClick(IMeter item, HomeAdapter.ViewHolder viewHolder) {
        iMetersDepository.selectMeter(item.getId());
        MainApplication.getInstance().sendBroadcast(new Intent(MainApplication.FILTER_ACTION_NAME)
                .putExtra(MainApplication.SIGNAL_NAME, MainApplication.SIGNAL_TYPE.OPEN_ADD_VALUE));
    }
}
