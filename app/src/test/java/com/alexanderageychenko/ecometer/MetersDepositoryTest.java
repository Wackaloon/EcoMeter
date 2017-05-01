package com.alexanderageychenko.ecometer;

import com.alexanderageychenko.ecometer.Model.DataBase.IMetersDAO;
import com.alexanderageychenko.ecometer.Model.Depository.MetersDepository;
import com.alexanderageychenko.ecometer.Model.Entity.IMeter;
import com.alexanderageychenko.ecometer.Model.Entity.response.GetMetersList;
import com.alexanderageychenko.ecometer.Tools.DefaultMetersFiller;

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

import io.reactivex.functions.Consumer;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * Created by Alexander on 01.05.2017.
 */

@RunWith(value = BlockJUnit4ClassRunner.class)
public class MetersDepositoryTest extends TestRoot {
    @Mock
    IMetersDAO iMetersDAO;
    @InjectMocks
    MetersDepository metersDepository;

    @Before
    public void setup() {


        MockitoAnnotations.initMocks(this);
    }

    @Test(timeout = 10000)
    public void setMetersList() {

        DefaultMetersFiller filer = new DefaultMetersFiller();
        final ArrayList<IMeter> defaultMeters = new ArrayList<IMeter>(filer.getDefaultMeters());

        metersDepository.setMeters(defaultMeters);

        verify(iMetersDAO, atLeastOnce()).set(defaultMeters);

        Assert.assertEquals(new ArrayList<>(metersDepository.getMeters()), defaultMeters);

        metersDepository.getMetersPublisher().subscribe(new Consumer<Collection<IMeter>>() {
            @Override
            public void accept(Collection<IMeter> iMeters) throws Exception {
                Assert.assertEquals(new ArrayList<>(iMeters), defaultMeters);
                setTestSuccecedAndStop();
            }
        });
        metersDepository.requestMeters();

        waitForStopAndCheckResult();
    }

    @Test(timeout = 10000)
    public void getMetersList(){
       // auth(); if you need auth dont forget to auth first in every test
        restManager.getRestInterface().getMeters(5L).enqueue(new CallbackNullCheckStop<GetMetersList>());
        waitForStopAndCheckResult();
    }
}
