package com.alexanderageychenko.ecometer.Model.Entity;

import com.alexanderageychenko.ecometer.R;

/**
 * Created by alexanderageychenko on 9/13/16.
 */

public enum MeterType {
    GAS(R.color.main_orange, R.drawable.gas_image),
    WATER(R.color.main_blue, R.drawable.water_image),
    ELECTRICITY(R.color.main_yellow, R.drawable.electro_image);
    int background;
    int image;

    MeterType(int background, int image) {
        this.background = background;
        this.image = image;
    }

    public int getBackgroundResource() {
        return background;
    }

    public int getImageResource() {
        return image;
    }
}
