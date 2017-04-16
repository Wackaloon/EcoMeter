package com.alexanderageychenko.ecometer.Tools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.animation.Animation;

import com.alexanderageychenko.ecometer.View.ExFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alexanderageychenko 13.09.16.
 */
public class FragmentCroupier {
    private HashMap<String, List<ExFragment>> backStackMap = new HashMap<>();
    private HashMap<Fragment, Integer[]> popAnimMap = new HashMap<>();
    private FragmentManager.OnBackStackChangedListener backStackChangedListener;
    private FragmentManager fragmentManager;
    private int resourceId;
    private String lastLoadTag = "";
    private boolean animationRun = false;
    private Animation.AnimationListener animatorListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            animationRun = true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animationRun = false;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    public FragmentCroupier() {

    }

    public void init(FragmentManager fragmentManager, int resourceId) {
        this.fragmentManager = fragmentManager;
        this.resourceId = resourceId;
    }

    public boolean addFragment(String tag, ExFragment fragment) {
        return addFragment(tag, fragment, false);
    }

    public boolean addFragment(String tag, ExFragment fragment, boolean replace) {
        return addFragment(tag, fragment, replace, 0, 0);
    }

    public boolean addFragment(String tag, ExFragment fragment, boolean replace, int animIn, int animOut) {
        return addFragment(tag, fragment, replace, animIn, animOut, 0, 0);
    }

    public boolean addFragment(String tag, ExFragment fragment, boolean replace, int animIn, int animOut, int popAnimIn, int popAnimOut) {
        fragment.setAnimatorListener(animatorListener);
        if (!animationRun) {
            if (backStackMap.containsKey(tag)) {
                boolean add = true;
                for (Fragment f : backStackMap.get(tag)) {
                    if (f == fragment) {
                        add = false;
                    }
                }
                if (add) {
                    backStackMap.get(tag).add(fragment);
                    popAnimMap.put(fragment, new Integer[]{popAnimIn, popAnimOut});
                }
            } else {
                ArrayList<ExFragment> fragments = new ArrayList<>();
                fragments.add(fragment);
                backStackMap.put(tag, fragments);
                popAnimMap.put(fragment, new Integer[]{popAnimIn, popAnimOut});
            }
            if (replace) {
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(animIn, animOut)
                        .replace(resourceId, fragment)
                        .commit();
                lastLoadTag = tag;
            }
            if (backStackChangedListener != null) backStackChangedListener.onBackStackChanged();
            return true;
        }
        return false;
    }

    public void clearBackStack() {
        clearBackStack(lastLoadTag);
    }

    public void clearAllBackStack() {
        backStackMap.clear();
        popAnimMap.clear();
    }

    public void clearBackStack(String tag) {
        if (backStackMap.containsKey(tag)) {
            for (Fragment f : backStackMap.get(tag)) {
                if (popAnimMap.containsKey(f)) popAnimMap.remove(f);
            }
            backStackMap.remove(tag);
        }
    }

    public boolean loadBackStack(String tag) {
        return loadBackStack(tag, 0, 0);
    }

    public boolean loadBackStack(String tag, int animIn, int animOut) {
        if (!animationRun) {
            if (backStackMap.containsKey(tag)) {
                List<ExFragment> fragmentList = backStackMap.get(tag);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(animIn, animOut);
                fragmentTransaction.replace(resourceId, fragmentList.get(fragmentList.size() - 1));
                fragmentTransaction.commit();
                lastLoadTag = tag;
                if (backStackChangedListener != null) backStackChangedListener.onBackStackChanged();
            }
            return true;
        }
        return false;
    }

    public boolean popBackStack() {
        return popBackStack(lastLoadTag);
    }

    public boolean popBackStack(String tag) {
        return popBackStack(tag, -1, -1);
    }

    public boolean popBackStack(int animIn, int animOut) {
        return popBackStack(lastLoadTag, animIn, animOut);
    }

    public boolean popBackStack(String tag, int animIn, int animOut) {
        if (animationRun)
            return true;
        if (getFragment().popBackStack())
            return true;
        if (backStackMap.containsKey(tag)) {
            List<ExFragment> fragmentList = backStackMap.get(tag);
            if (fragmentList.size() > 1) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment lastFragment = fragmentList.get(fragmentList.size() - 1);
                if (popAnimMap.containsKey(lastFragment)) {
                    if (animIn == -1) animIn = popAnimMap.get(lastFragment)[0];
                    if (animOut == -1) animOut = popAnimMap.get(lastFragment)[1];
                    popAnimMap.remove(lastFragment);
                } else {
                    if (animIn == -1) animIn = 0;
                    if (animOut == -1) animOut = 0;
                }
                fragmentTransaction.setCustomAnimations(animIn, animOut);
                fragmentTransaction.replace(resourceId, fragmentList.get(fragmentList.size() - 2));
                fragmentTransaction.commit();
                fragmentList.remove(fragmentList.size() - 1);
                if (backStackChangedListener != null) backStackChangedListener.onBackStackChanged();
                return true;
            }
        }
        return false;
    }

    public String getLastLoadTag() {
        return lastLoadTag;
    }

    public void setLastLoadTag(String lastLoadTag) {
        this.lastLoadTag = lastLoadTag;
    }

    public int getBackStackCount() {
        return getBackStackCount(lastLoadTag);
    }

    public int getBackStackCount(String tag) {
        if (backStackMap.containsKey(tag)) {
            return backStackMap.get(tag).size();
        }
        return 0;
    }

    public void removeFragment(Fragment fragment) {
        removeFragment(lastLoadTag, fragment);
    }

    public void removeFragment(String tag, Fragment fragment) {
        if (backStackMap.containsKey(tag)) {
            for (int i = 0; i < backStackMap.get(tag).size(); i++) {
                ExFragment f = backStackMap.get(tag).get(i);
                if (f == fragment) {
                    if (popAnimMap.containsKey(f)) {
                        popAnimMap.remove(f);
                    }
                    backStackMap.get(tag).remove(i);
                    i--;
                }
            }
        }
    }

    /**
     * If the stack of the fragments are active, then removes all the intermediate fragments between the first and the last, and will the latter cause getActivity.onBackPressed()
     * If stack segments are not active will remove all fragments except the first.
     *
     * @param tag
     */
    public void moveToFirstFragment(String tag) {
        List<ExFragment> fragments = new ArrayList<>(backStackMap.get(tag));
        for (int i = 0; i < fragments.size(); i++) {
            if (i > 0 && i < fragments.size() - 1) {
                removeFragment(tag, fragments.get(i));
            }
        }
        if (fragments.size() > 1)
            if (tag.equals(lastLoadTag)) {
                fragments.get(fragments.size() - 1).getActivity().onBackPressed();
            } else {
                removeFragment(tag, fragments.get(fragments.size() - 1));
            }
    }

    public ExFragment getFragment() {
        return getFragment(lastLoadTag);
    }

    public ExFragment getFragment(String tag) {
        if (backStackMap.containsKey(tag)) {
            List<ExFragment> fragments = backStackMap.get(tag);
            return fragments.get(fragments.size() - 1);
        }
        return null;
    }

    public void remove() {
        fragmentManager.beginTransaction().remove(getFragment()).commit();
    }

    public void setBackStackChangedListener(FragmentManager.OnBackStackChangedListener backStackChangedListener) {
        this.backStackChangedListener = backStackChangedListener;
    }

    public void changeFragment(ExFragment fragment, ExFragment newFragment) {
        changeFragment(lastLoadTag, fragment, newFragment);
    }

    public void changeFragment(String tag, ExFragment fragment, ExFragment newFragment) {
        if (backStackMap.containsKey(tag)) {
            for (int i = 0; i < backStackMap.get(tag).size(); i++) {
                Fragment f = backStackMap.get(tag).get(i);
                if (f == fragment) {
                    if (popAnimMap.containsKey(f)) {
                        Integer[] anim = popAnimMap.get(f);
                        popAnimMap.remove(f);
                        popAnimMap.put(newFragment, anim);
                    }
                    backStackMap.get(tag).remove(i);
                    backStackMap.get(tag).add(i, newFragment);
                }
            }
        }
    }

    public int getResourceId() {
        return resourceId;
    }

}
