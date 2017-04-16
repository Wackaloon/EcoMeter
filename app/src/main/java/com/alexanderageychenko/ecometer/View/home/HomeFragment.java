package com.alexanderageychenko.ecometer.View.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.Octopus.home.IHomeOctopus;
import com.alexanderageychenko.ecometer.R;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;
import com.alexanderageychenko.ecometer.View.ExFragment;

import java.util.Collection;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


/**
 * Created by alexanderageychenko 13.09.16.
 */
public class HomeFragment extends ExFragment implements HomeAdapter.Listener, IHomeOctopus.IView {
    private static final String TAG = "Home";
    private HomeAdapter homeAdapter;
    @Inject
    IHomeOctopus iHomeOctopus;

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
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.home_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        homeAdapter = new HomeAdapter(getActivity());
        homeAdapter.setListener(this);
        recyclerView.setAdapter(homeAdapter);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        iHomeOctopus.onStart();

    }

    @Override
    public void onStop() {
        iHomeOctopus.onStop();
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onItemClick(IMeter item, HomeAdapter.ViewHolder viewHolder) {
        iHomeOctopus.openMeterDetails(item);
    }

    @Override
    public void onImageClick(IMeter item, HomeAdapter.ViewHolder viewHolder) {
        iHomeOctopus.openAddValueToMeter(item);
    }

    @Override
    public void setMeters(Collection<IMeter> meters) {
        Observable.just(meters)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Collection<IMeter>>() {
                    @Override
                    public void accept(Collection<IMeter> iMeters) throws Exception {
                        homeAdapter.setData(iMeters);
                    }
                });
    }
}
