package com.alexanderageychenko.ecometer.Fragments.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexanderageychenko.ecometer.Fragments.ExFragment;
import com.alexanderageychenko.ecometer.Data.Depository;
import com.alexanderageychenko.ecometer.Fragments.details.DetailsFragment;
import com.alexanderageychenko.ecometer.MainApplication;
import com.alexanderageychenko.ecometer.Model.DetailsTransition;
import com.alexanderageychenko.ecometer.Model.Meter;
import com.alexanderageychenko.ecometer.R;


/**
 * Created by alexanderageychenko 13.09.16.
 */
public class HomeFragment extends ExFragment implements HomeAdapter.Listener {
    private static final String TAG = "Home";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HomeAdapter homeAdapter;
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
    public void onItemClick(Meter item, HomeAdapter.ViewHolder viewHolder) {
        MainApplication.getInstance().sendBroadcast(new Intent(MainApplication.FILTER_ACTION_NAME)
                .putExtra(MainApplication.SIGNAL_NAME, MainApplication.SIGNAL_TYPE.OPEN_DETAILS)
                .putExtra("id", Depository.getInstance().getMeters().indexOf(item)));
    }
}
