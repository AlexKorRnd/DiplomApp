package com.alexkorrnd.diplomapp.presentation.utils;


import android.content.res.Resources;
import android.util.TypedValue;

public class DimensionsUtils {

    public static int toDP(float value) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                Resources.getSystem().getDisplayMetrics()));
    }
}
