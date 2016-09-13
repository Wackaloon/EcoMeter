package com.alexanderageychenko.ecometer.Fragments;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;


/**
 * Created by alexanderaheychenko on 13.09.16.
 */
public abstract class ExFragment extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener {
    protected int real_width, real_height;

    Animator.AnimatorListener animatorListener;

    private Float elevator;

    public int getReal_width() {
        return real_width;
    }

    public int getReal_height() {
        return real_height;
    }

    public ExFragment setAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (elevator != null) ViewCompat.setElevation(view, elevator);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onGlobalLayout() {
        if (getView() != null) {
            real_width = getView().getWidth();
            real_height = getView().getHeight();
        }
    }

    public void setElevator(Float e) {
        elevator = e;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("DEBUG", "onAttach");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("DEBUG", "onActivityCreated");
    }

    @Override
    public void onDetach() {
        Log.d("DEBUG", "onDetach");
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        super.onStop();
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        Animator anim = super.onCreateAnimator(transit, enter, nextAnim);
        if (nextAnim != 0) {
            anim = AnimatorInflater.loadAnimator(getActivity(),
                    nextAnim);
            if (animatorListener != null)
                anim.addListener(animatorListener);
        }
        return anim;
    }

    public boolean popBackStack() {
        return false;
    }

    protected Drawable getDrawable(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return getResources().getDrawable(id, getActivity().getTheme());
        } else {
            //noinspection deprecation
            return getResources().getDrawable(id);
        }
    }

    protected int getColor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(id, getActivity().getTheme());
        } else {
            //noinspection deprecation
            return getResources().getColor(id);
        }
    }

}
