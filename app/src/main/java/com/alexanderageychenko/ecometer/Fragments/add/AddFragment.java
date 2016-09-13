package com.alexanderageychenko.ecometer.Fragments.add;

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
import com.alexanderageychenko.ecometer.Model.Meter;
import com.alexanderageychenko.ecometer.R;


/**
 * Created by alexanderageychenko 13.09.16.
 */
public class AddFragment extends ExFragment implements AddAdapter.Listener, View.OnClickListener {
    private static final String TAG = "Home";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AddAdapter addAdapter;
    private Button done;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.home_recycler_view);
        done = (Button) view.findViewById(R.id.done);
        done.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        addAdapter = new AddAdapter(getActivity());
        addAdapter.setListener(this);
        recyclerView.setAdapter(addAdapter);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        addAdapter.setData(Depository.getInstance().getMeters());
    }

    @Override
    public void onPause() {
        Depository.getInstance().setMeters(addAdapter.getItems());
        Depository.getInstance().saveToDB();
        super.onPause();
    }

    @Override
    public void onItemClick(Meter item) {
    }

    @Override
    public void onClick(View view) {
        if (view == done){
            Depository.getInstance().setMeters(addAdapter.getItems());
            Depository.getInstance().saveToDB();
            getActivity().onBackPressed();
        }
    }
}
