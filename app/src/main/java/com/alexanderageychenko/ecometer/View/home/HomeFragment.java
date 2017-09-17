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
import com.alexanderageychenko.ecometer.Tools.dagger2.Module.AppRxSchedulers;
import com.alexanderageychenko.ecometer.View.ExFragment;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by alexanderageychenko 13.09.16.
 */
public class HomeFragment
        extends ExFragment
        implements HomeAdapter.Listener,
                             View.OnClickListener {
    private HomeAdapter homeAdapter;
    private View settings;
    private View statistics;
    @Inject
    IHomeOctopus iHomeOctopus;
    @Inject
    @Named(AppRxSchedulers.UI_THREAD)
    Scheduler UIThread;
    private Disposable metersSubscriber;

    public HomeFragment() {
        Dagger.get().getInjector().inject(this);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.home_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        homeAdapter = new HomeAdapter(getActivity());
        homeAdapter.setListener(this);
        recyclerView.setAdapter(homeAdapter);

        settings = view.findViewById(R.id.settings);
        settings.setOnClickListener(this);
        statistics = view.findViewById(R.id.statistics);
        statistics.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        startSubscribers();
        iHomeOctopus.onStart();
    }

    @Override
    public void onStop() {
        stopSubscribers();
        iHomeOctopus.onStop();
        super.onStop();
    }

    private void startSubscribers() {
        metersSubscriber = iHomeOctopus.getMetersObservable()
                .observeOn(UIThread)
                .subscribe(new MetersConsumer());
    }

    private void stopSubscribers() {
        if (metersSubscriber != null) {
            metersSubscriber.dispose();
        }
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
    public boolean popBackStack() {
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view == settings){
            iHomeOctopus.openSettings();
        } else if ( view == statistics){
            iHomeOctopus.openStatistics();
        }
    }


    private class MetersConsumer implements Consumer<Collection<IMeter>>{
        @Override
        public void accept(Collection<IMeter> iMeters) throws Exception {
            homeAdapter.setData(iMeters);
        }
    }
}
