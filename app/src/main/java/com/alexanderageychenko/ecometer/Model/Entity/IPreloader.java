package com.alexanderageychenko.ecometer.Model.Entity;

/**
 * Created by alexanderageychenko on 9/17/17.
 */

public interface IPreloader {
        boolean showLoading();

        void hideLoading();

        boolean showLoading(long delay);
}
