package com.alexanderageychenko.ecometer.Tools.Navigator;

import com.alexanderageychenko.ecometer.MainActivity;
import com.alexanderageychenko.ecometer.R;
import com.alexanderageychenko.ecometer.Tools.RxCustomUtils;
import com.alexanderageychenko.ecometer.View.ExFragment;
import com.alexanderageychenko.ecometer.View.add.AddValueFragment;
import com.alexanderageychenko.ecometer.View.details.DetailsFragment;
import com.alexanderageychenko.ecometer.View.edit.EditMeterFragment;
import com.alexanderageychenko.ecometer.View.home.HomeFragment;
import com.alexanderageychenko.ecometer.View.settings.SettingsFragment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by alexanderageychenko on 17.05.17
 */
public class HomeFragmentTransactor {
    final int idHomeFrame = R.id.content_frame;
    private final ActionFragmentTransaction actionFragmentTransaction = new ActionFragmentTransaction();
    private final ActionFragmentTransactionError actionFragmentTransactionError = new ActionFragmentTransactionError();
    public int statusBarColor;
    HomeFragment mHomeFragment = null;
    private MainActivity mainActivity;
    private Disposable subscriptionNavigator;

    public HomeFragmentTransactor(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void start() {
        ExFragment fragment = null;
        if (mainActivity.homeNavigator.getActiveScreen() != null) {
            fragment = getFragmentByScreenType(new Navigator.Screen<>(MainScreenType.Null, null), mainActivity.homeNavigator.getActiveScreen());
        }
        if (fragment != null) {
            fragment.setUsedNavigator(mainActivity.homeNavigator);
            mainActivity.getSupportFragmentManager().beginTransaction()
                    .replace(idHomeFrame, fragment)
                    .commit();
        } else if ((fragment = getFragment()) != null)
            mainActivity.getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
    }

    public void stop() {
    }

    ExFragment getFragmentByScreenType(Navigator.Screen<MainScreenType> nowScreen, Navigator.Screen<MainScreenType> newScreen) {
        switch (newScreen.type) {
            case MetersListScreen: {
                HomeFragment fragment = new HomeFragment();
                return fragment;
            }
            case AddValueScreen: {
                AddValueFragment fragment = new AddValueFragment();
                fragment.setMeterId((Long) newScreen.hashMap.get("meter_id"));
                return fragment;
            }
            case DetailsScreen: {
                DetailsFragment fragment = new DetailsFragment();
                fragment.setMeterId((Long) newScreen.hashMap.get("meter_id"));
                return fragment;
            }
            case SettingsScreen:{
                return new SettingsFragment();
            }
            case CreateMeterScreen:
            case EditMeterScreen: {
                EditMeterFragment fragment = new EditMeterFragment();
                Long meterId = newScreen.hashMap != null && newScreen.hashMap.containsKey("meter_id") ? (Long) newScreen.hashMap.get("meter_id") : null;
                fragment.setMeterId(meterId);
                return fragment;
            }
        }

        return null;
    }

    public ExFragment getFragment() {
        return (ExFragment) mainActivity.getSupportFragmentManager().findFragmentById(idHomeFrame);
    }

    public void subscribe() {
        subscriptionNavigator = mainActivity.homeNavigator.observableChangeScreen()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(actionFragmentTransaction, actionFragmentTransactionError);
    }

    public void unsubscribe() {
        new RxCustomUtils.Unsubscriber()
                .unsubscribe(subscriptionNavigator);
    }

    private class ActionFragmentTransaction implements Consumer<Navigator.ScreenTransaction<MainScreenType>> {

        @Override
        public void accept(Navigator.ScreenTransaction<MainScreenType> screenTransaction) throws Exception {
//            mainActivity.editTextFocusManager.reset();
            if (screenTransaction.newScreen.type == MainScreenType.Null) {
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(screenTransaction.animIn, screenTransaction.animOut)
                        .remove(mainActivity.getSupportFragmentManager().findFragmentById(idHomeFrame))
                        .commit();
                return;
            }
            if (screenTransaction.nowScreen.type == screenTransaction.newScreen.type) {
                return;
            }
            ExFragment fragment = getFragmentByScreenType(screenTransaction.nowScreen, screenTransaction.newScreen);
            if (fragment == null) return;
            fragment.setUsedNavigator(mainActivity.homeNavigator);

            //mainActivityV2.mainMenu.selectMenuItemByScreenType(screenTransaction.newScreen.type);
            mainActivity.getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(screenTransaction.animIn, screenTransaction.animOut)
                    .replace(idHomeFrame, fragment)
                    .commit();
        }
    }

    private class ActionFragmentTransactionError implements Consumer<Throwable> {
        @Override
        public void accept(Throwable throwable) {
            throwable.printStackTrace();
            subscribe();
        }
    }
}
