package net.byloth.engine.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;

import net.byloth.sky.R;

/**
 * Created by Matteo on 19/11/2015.
 */
public class NumberPickerPreference extends DialogPreference
{
    static final private String ANDROID_NS = "http://schemas.android.com/apk/res/android";
    static final private String ATTR_DEFAULT_VALUE = "defaultValue";

    static final private String PREFERENCE_NS = "http://schemas.android.com/apk/res-auto";
    static final private String ATTR_MIN_VALUE = "minValue";
    static final private String ATTR_MAX_VALUE = "maxValue";
    static final private String ATTR_STEP = "step";

    private int minValue;
    private int maxValue;

    private int defaultValue;

    private int step;

    private String[] displayedValues;

    private NumberPicker numberPicker;

    public NumberPickerPreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        setDialogLayoutResource(R.layout.number_picker_pref_layout);

        minValue = attrs.getAttributeIntValue(PREFERENCE_NS, ATTR_MIN_VALUE, 0);
        maxValue = attrs.getAttributeIntValue(PREFERENCE_NS, ATTR_MAX_VALUE, 100);

        if (minValue > maxValue)
        {
            throw new IllegalArgumentException();
        }

        defaultValue = attrs.getAttributeIntValue(ANDROID_NS, ATTR_DEFAULT_VALUE, 0);

        if ((defaultValue < minValue) || (defaultValue > maxValue))
        {
            throw new IllegalArgumentException();
        }

        step = attrs.getAttributeIntValue(PREFERENCE_NS, ATTR_STEP, 1);

        if (step < 1)
        {
            throw new IllegalArgumentException();
        }
        else if (step > 1)
        {
            int valuesCount = (maxValue / step) + 1;

            displayedValues = new String[valuesCount];

            for (int index = 0; index < valuesCount; index += 1)
            {
                int value = minValue + (step * index);

                displayedValues[index] = String.valueOf(value);
            }

            minValue = minValue / step;
            maxValue = maxValue / step;

            defaultValue = defaultValue / step;
        }
    }

    @Override
    protected void onBindDialogView(View view)
    {
        super.onBindDialogView(view);

        int persistedValue = getPersistedInt(defaultValue);

        numberPicker = (NumberPicker) view.findViewById(R.id.preference_number_picker);

        numberPicker.setMinValue(minValue);
        numberPicker.setMaxValue(maxValue);

        numberPicker.setValue(persistedValue);

        if (displayedValues != null)
        {
            numberPicker.setDisplayedValues(displayedValues);
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult)
    {
        persistInt(numberPicker.getValue());
    }
}
