package net.byloth.engine.ui;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

import net.byloth.sky.R;

/**
 * Created by Matteo on 19/11/2015.
 */
public class NumberPickerPreference extends DialogPreference
{
    static final protected String ANDROID_NS = "http://schemas.android.com/apk/res/android";
    static final protected String ATTR_DEFAULT_VALUE = "defaultValue";

    static final protected String PREFERENCE_NS = "http://schemas.android.com/apk/res-auto";
    static final protected String ATTR_MIN_VALUE = "minValue";
    static final protected String ATTR_MAX_VALUE = "maxValue";

    protected int defaultValue;

    protected int minValue;
    protected int maxValue;

    protected NumberPicker numberPicker;

    public NumberPickerPreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        setPersistent(false);
        setDialogLayoutResource(R.layout.number_picker_pref_layout);

        minValue = attrs.getAttributeIntValue(PREFERENCE_NS, ATTR_MIN_VALUE, 0);
        maxValue = attrs.getAttributeIntValue(PREFERENCE_NS, ATTR_MAX_VALUE, 0);

        defaultValue = attrs.getAttributeIntValue(ANDROID_NS, ATTR_DEFAULT_VALUE, 0);

        if ((defaultValue < minValue) || (defaultValue > maxValue))
        {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected void onBindDialogView(View view)
    {
        super.onBindDialogView(view);

        numberPicker = (NumberPicker) view.findViewById(R.id.preference_number_picker);

        numberPicker.setMinValue(minValue);
        numberPicker.setMaxValue(maxValue);

        numberPicker.setValue(defaultValue);
    }
}
