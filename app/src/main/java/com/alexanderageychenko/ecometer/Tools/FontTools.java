package com.alexanderageychenko.ecometer.Tools;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.TypedValue;
import android.widget.TextView;

import com.alexanderageychenko.ecometer.R;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;
import com.annimon.stream.Stream;

/**
 * Created by alexanderageychenko on 9/17/17.
 */

public class FontTools {
    public static void setTypeface(TextView tv, FontType font, double letterSpacing, int lineSpacing, int fontSizeRes) {
        setTypeface(Dagger.get().getContext(), tv, font, letterSpacing, lineSpacing, fontSizeRes);
    }

    public static void setTypeface(Context c, TextView tv, FontType font, double letterSpacing, int lineSpacing, int fontSizeRes) {
        if (tv == null) return;
        if (c != null) {
            tv.setTypeface(Typeface.createFromAsset(c.getAssets(), font.getPath()));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) ResTools.getDimenPixelSize(c, fontSizeRes));
            float sizeOf1Dp = (float) ResTools.getDimen(Dagger.get().getContext(), R.dimen.design_dp_1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && letterSpacing != 0) {
                float fontSizeDp = tv.getTextSize();
                double letterspaceInPixel = (letterSpacing * sizeOf1Dp);
                float letterSpaceForTextView = (float) (letterspaceInPixel / fontSizeDp);
                tv.setLetterSpacing(letterSpaceForTextView);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && lineSpacing != 0) {
                float targetLineSpace = lineSpacing * sizeOf1Dp;
                float rawLineSpace = tv.getPaint().getFontSpacing();
                float resultingLineSpaceMultiplier = targetLineSpace / rawLineSpace;
                tv.setLineSpacing(0f, resultingLineSpaceMultiplier);
            }
        }
        tv.requestLayout();
    }

    public static void setTypeface(FontType font, double letterSpacing, int lineSpacing, int fontSizeRes, TextView... tv) {
        Stream.of(tv).filter(view -> view != null).forEach(i -> setTypeface(i, font, letterSpacing, lineSpacing, fontSizeRes));
    }
    public enum FontType {
        SF_UI_TEXT_REGULAR("fonts/sf-san-francisco-ui-text-regular.ttf"),
        SF_UI_TEXT_LIGHT("fonts/sf-san-francisco-ui-text-light.ttf"),
        SF_UI_TEXT_BOLD("fonts/sf-san-francisco-ui-text-bold.ttf"),
        SF_UI_TEXT_MEDIUM("fonts/sf-san-francisco-ui-text-medium.ttf"),
        SF_UI_TEXT_SEMIBOLD("fonts/sf-san-francisco-ui-text-semibold.ttf"),
        SF_UI_DISPLAY_THIN("fonts/sf-san-francisco-ui-display-thin.ttf"),
        SF_UI_DISPLAY_HEAVY("fonts/sf-san-francisco-ui-display-heavy.ttf"),
        SF_UI_DISPLAY_MEDIUM("fonts/sf-san-francisco-ui-display-medium.ttf"),
        SF_UI_DISPLAY_BLACK("fonts/sf-san-francisco-ui-display-black.ttf"),
        SF_UI_DISPLAY_REGULAR("fonts/sf-san-francisco-ui-display-regular.ttf"),
        SF_UI_DISPLAY_LIGHT("fonts/sf-san-francisco-ui-display-light.ttf"),
        SF_UI_DISPLAY_SEMIBOLD("fonts/sf-san-francisco-ui-display-semibold.ttf"),
        SF_UI_DISPLAY_BOLD("fonts/sf-san-francisco-ui-display-bold.ttf")
        ;

        private final String path;

        FontType(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}
