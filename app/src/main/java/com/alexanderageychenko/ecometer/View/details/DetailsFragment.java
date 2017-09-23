package com.alexanderageychenko.ecometer.View.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexanderageychenko.ecometer.MainActivity;
import com.alexanderageychenko.ecometer.Model.Entity.MeterType;
import com.alexanderageychenko.ecometer.Model.Entity.MeterValue;
import com.alexanderageychenko.ecometer.Model.Listener.DetailsEditListener;
import com.alexanderageychenko.ecometer.Presenter.details.IDetailsIPresenter;
import com.alexanderageychenko.ecometer.R;
import com.alexanderageychenko.ecometer.Tools.DialogBuilder;
import com.alexanderageychenko.ecometer.Tools.RxTools;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;
import com.alexanderageychenko.ecometer.Tools.dagger2.Module.AppRxSchedulers;
import com.alexanderageychenko.ecometer.Model.ExContainers.ExFragment;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by alexanderageychenko on 9/14/16.
 */

public class DetailsFragment extends ExFragment implements DetailsAdapter.Listener {
    private ImageView imageView;
    private RecyclerView recyclerView;
    private DetailsAdapter detailsAdapter;
    private TextView name, valuePerDay, valuePerMonth;

    @Inject
    @Named(AppRxSchedulers.UI_THREAD)
    Scheduler UIThread;
    @Inject
    IDetailsIPresenter iDetailsOctopus;
    private Disposable typeSubscriber;
    private Disposable fullnameSubscriber;
    private Disposable valuePerDaySubsriber;
    private Disposable valuePerMonthSubscriber;
    private Disposable valuesSubscriber;
    TypeConsumer typeConsumer = new TypeConsumer();
    FullNameConsumer fullNameConsumer = new FullNameConsumer();
    ValuesConsumer valuesConsumer = new ValuesConsumer();
    private Long meterId;

    public DetailsFragment() {
        Dagger.get().getInjector().inject(this);
    }

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = (ImageView) view.findViewById(R.id.image);

        view.findViewById(R.id.plus).setVisibility(View.GONE);

        name = ((TextView) view.findViewById(R.id.name));
        valuePerDay = ((TextView) view.findViewById(R.id.mean_day_text));
        valuePerMonth = ((TextView) view.findViewById(R.id.mean_month_text));

        recyclerView = (RecyclerView) view.findViewById(R.id.details_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        detailsAdapter = new DetailsAdapter(getActivity());
        detailsAdapter.setListener(this);
        recyclerView.setAdapter(detailsAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onValueClick(MeterValue item) {
        DialogBuilder.getEditDetailsDialog(getActivity(),
                iDetailsOctopus.getMeter(),
                item.getId(),
                new DetailsEditListener() {
                    @Override
                    public void valueWasEdited() {
                        iDetailsOctopus.requestUpdate();
                    }
                })
                .show();
    }

    @Override
    public void onDateClick(MeterValue item) {
        DialogBuilder.getExitEditDateDialog(getActivity(),
                iDetailsOctopus.getMeter(),
                item.getId(),
                new DetailsEditListener() {
                    @Override
                    public void valueWasEdited() {
                        iDetailsOctopus.requestUpdate();
                    }
                })
                .show();
    }

    @Override
    public void onStart() {
        super.onStart();
        startSubscribers();

        ((MainActivity) getActivity()).showBackButtonOnBurger(true);
        iDetailsOctopus.onStart();
    }

    private void startSubscribers() {
        stopSubscribers();
        typeSubscriber = iDetailsOctopus.getMeterTypeObservable()
                .observeOn(UIThread)
                .subscribe(typeConsumer); //this is better if you have same code in different consumers

        fullnameSubscriber = iDetailsOctopus.getMeterFullnameObservable()
                .observeOn(UIThread)
                .subscribe(fullNameConsumer);

        valuePerDaySubsriber = iDetailsOctopus.getMeterValuePerDayObservable()
                .observeOn(UIThread)
                .subscribe(new ValuePerDayConsumer());

        valuePerMonthSubscriber = iDetailsOctopus.getMeterValuePerMonthObservable()
                .observeOn(UIThread)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String value) throws Exception {
                        valuePerMonth.setText(value);
                    }
                });

        valuesSubscriber = iDetailsOctopus.getMeterValuesObservable()
                .observeOn(UIThread)
                .subscribe(meterValues -> { //retrolyambda example
                    detailsAdapter.setData(meterValues);
                    recyclerView.scrollToPosition(0);

                });

    }

    @Override
    public void onStop() {
        stopSubscribers();
        iDetailsOctopus.onStop();
        super.onStop();
    }

    private void stopSubscribers() {
        RxTools.Unsubscriber()
                .unsubscribe(typeSubscriber)
                .unsubscribe(fullnameSubscriber)
                .unsubscribe(valuePerDaySubsriber)
                .unsubscribe(valuePerMonthSubscriber)
                .unsubscribe(valuesSubscriber);
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
        iDetailsOctopus.setMeterId(meterId);
    }

    public Long getMeterId() {
        return meterId;
    }

    private class TypeConsumer implements Consumer<MeterType> {
        @Override
        public void accept(MeterType meterType) throws Exception {
            if (meterType == null) {
                imageView.setBackgroundResource(R.color.main_green_A0);
                imageView.setImageResource(R.drawable.ic_public_white_48dp);
            } else {
                imageView.setBackgroundResource(meterType.getBackgroundResource());
                imageView.setImageResource(meterType.getImageResource());
            }
        }
    }

    private class FullNameConsumer implements Consumer<String> {
        @Override
        public void accept(String fullname) throws Exception {
            name.setText(fullname);
        }
    }

    private class ValuePerDayConsumer implements Consumer<String> {
        @Override
        public void accept(String value) throws Exception {
            valuePerDay.setText(value);
        }
    }


    private class ValuesConsumer implements Consumer<ArrayList<MeterValue>> {
        @Override
        public void accept(ArrayList<MeterValue> meterValues) throws Exception {
            detailsAdapter.setData(meterValues);
            recyclerView.scrollToPosition(0);
        }
    }
}
