package com.alexanderageychenko.ecometer.Tools.Navigator;

/**
 * Created by alexanderageychenko on 16.05.17.
 */

public class MainScreenNavigator extends NavigatorImpl<MainScreenType> {
    @Override
    protected MainScreenType getDefaultType() {
        return MainScreenType.Null;
    }
}
