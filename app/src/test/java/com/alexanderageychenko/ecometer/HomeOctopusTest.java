package com.alexanderageychenko.ecometer;

import com.alexanderageychenko.ecometer.Model.Depository.MetersDepository;
import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.Octopus.home.HomeOctopus;
import com.alexanderageychenko.ecometer.Tools.DefaultMetersFiller;
import com.alexanderageychenko.ecometer.Tools.MyLogger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Alexander on 01.05.2017.
 */

@RunWith(value = BlockJUnit4ClassRunner.class)
public class HomeOctopusTest extends TestRoot {
    @Mock
    MetersDepository metersDepository;
    @InjectMocks
    HomeOctopus homeOctopus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(timeout = 10000)
    public void setMetersList() {

        DefaultMetersFiller filer = new DefaultMetersFiller();
        final ArrayList<IMeter> defaultMeters = new ArrayList<IMeter>(filer.getDefaultMeters());

        when(metersDepository.getMeters())
                .thenReturn(defaultMeters);

        when(metersDepository.getMetersPublisher())
                .thenReturn(Observable.just(defaultMeters)
                        .map(new Function<ArrayList<IMeter>, Collection<IMeter>>() {
                            @Override
                            public Collection<IMeter> apply(ArrayList<IMeter> iMeters) throws Exception {

                                MyLogger.d("HomeOctopusTest", "get meters from observable = " + iMeters.toString());
                                return iMeters;
                            }
                        }));
        homeOctopus.getMetersObservable().subscribe(new Consumer<Collection<IMeter>>() {
            @Override
            public void accept(Collection<IMeter> iMeters) throws Exception {

                MyLogger.d("HomeOctopusTest", "get meters = " + iMeters.toString());
                if (iMeters.isEmpty()) return;
                Assert.assertEquals(new ArrayList<>(iMeters), defaultMeters);
                stopTest();
            }
        });
        MyLogger.d("HomeOctopusTest", "start of octopus");
        homeOctopus.onStart();

        waitForStopAndCheckResult();

        homeOctopus.onStop();

        verify(metersDepository, atLeastOnce()).getMetersPublisher();
    }
}
