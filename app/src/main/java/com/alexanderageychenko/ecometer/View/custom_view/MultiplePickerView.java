package com.alexanderageychenko.ecometer.View.custom_view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.alexanderageychenko.ecometer.R;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;

import java.util.ArrayList;

/**
 * Created by alexanderageychenko on 7/21/16.
 */
public class MultiplePickerView extends RelativeLayout {
    private LoopView[] loopArray = new LoopView[6];
    boolean typeFrom = true;
    public static final int DEFULT_START = 0;
    public static final int DEFULT_END = 9;
    private OnValueSelected listener;
    static final String[] Months = new String[]{"January", "February",
            "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December"};
    private Long initialValue = 0L;

    public OnValueSelected getListener() {
        return listener;
    }

    public void setListener(OnValueSelected listener) {
        this.listener = listener;
    }

    public MultiplePickerView(Context context) {
        super(context);
        init();
    }

    public MultiplePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultiplePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public MultiplePickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.multiple_picker_layout, this);
        this.loopArray[0] = (LoopView) findViewById(R.id.one);
        this.loopArray[1] = (LoopView) findViewById(R.id.two);
        this.loopArray[2] = (LoopView) findViewById(R.id.three);
        this.loopArray[3] = (LoopView) findViewById(R.id.four);
        this.loopArray[4] = (LoopView) findViewById(R.id.five);
        this.loopArray[5] = (LoopView) findViewById(R.id.six);
        configureViewData(loopArray);

    }

    private void configureViewData(LoopView[] loopView) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = DEFULT_START; i <= DEFULT_END; i++) {
            list.add(Integer.toString(i));
        }
        for (LoopView l : loopArray) {
            l.setListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    callbackDate();
                }
            });
            int value = Dagger.get().getContext().getResources().getDimensionPixelSize(R.dimen.design_dp_5);
            l.setViewPadding(value, 0, value, 0);

        }
        configureView(loopView, list);
    }

    private void configureView(LoopView[] loopViews, ArrayList<String> list) {
        for (LoopView l : loopViews) {
            l.setItems(list);
            l.setInitPosition(0);
            l.setTextSize(getResources().getDimensionPixelSize(R.dimen.fonts_sizes_30));

        }

    }


    public void setInitialValue(Long initialValue) {
        this.initialValue = initialValue;
        int[] newGuess = new int[loopArray.length];
        for (int i = loopArray.length - 1; i >= 0; i--) {
            newGuess[i] = (int) (initialValue % 10);
            initialValue /= 10;
        }
        int i = 0;
        for (LoopView l : loopArray) {
            l.setInitPosition(newGuess[i++]);
        }
    }

    public interface OnValueSelected {
        void valueSelected(Long value);
    }

    public void callbackDate() {
        int i = 0;
        int[] selectedValue = new int[loopArray.length];
        for (LoopView l : loopArray) {
            selectedValue[i++] = Integer.parseInt(l.getSelectedItemString());
        }
        String value = "";
        for (int j : selectedValue) {
            value += j;
        }
        Long longValue = Long.parseLong(value);
        if (listener != null) {
            listener.valueSelected(longValue);
        }

    }
}
