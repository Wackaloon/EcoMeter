package com.alexanderageychenko.ecometer.Octopus;

/**
 * Created by Alexander on 16.04.2017.
 */

public interface Octopus <T extends Octopus.IView>{
    void setIView(T view);
    void onStart();
    void onStop();
    interface IView{

    }
}
