package com.example.ahmed.walkgraph.presentation.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by ahmed on 11/7/17.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class TimePreference extends DialogPreference {
    private TimePicker timePicker;

    public TimePreference(Context context, AttributeSet attrs,
                          int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        timePicker = new TimePicker(context);
        timePicker.setId(View.generateViewId());

        timePicker.setEnabled(true);
        timePicker.setIs24HourView(true);

        setDialogIcon(null);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr){
        this(context, attrs, defStyleAttr, 0);
    }

    public TimePreference(Context context, AttributeSet attrs){
        this(context, attrs, android.R.style.ThemeOverlay_Material_Light);
    }

    public TimePreference(Context context){
        this(context, null);
    }

    private void setTime(int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        persistLong(calendar.getTimeInMillis());

        notifyChanged();
        notifyDependencyChange(shouldDisableDependents());
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);

        builder.setView(timePicker);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult){
            setTime(timePicker.getHour(), timePicker.getMinute());
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return Long.valueOf(a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
        if(restorePersistedValue){
            Calendar calendar = Calendar.getInstance();
            long savedTime = getPersistedLong(calendar.getTimeInMillis());
            calendar.setTimeInMillis(savedTime);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        }else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis((long) defaultValue);

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            timePicker.setHour(hour);
            timePicker.setMinute(minute);

            setTime(hour, minute);
        }
    }

    @Override
    public boolean shouldDisableDependents() {
        Calendar now = Calendar.getInstance();
        long persistedLong = getPersistedLong(now.getTimeInMillis());
        boolean isEqual =  now.getTimeInMillis() == persistedLong;
        return isEqual ||  super.shouldDisableDependents();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superParcel =  super.onSaveInstanceState();
        if (isPersistent()){
            return superParcel;
        }

        final SavedState currentState = new SavedState(superParcel);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        calendar.set(Calendar.MINUTE, timePicker.getMinute());
        currentState.timeInMillSecs = calendar.getTimeInMillis();
        return currentState;
    }

    private static class SavedState extends BaseSavedState{
        long timeInMillSecs;
        public SavedState(Parcel source) {
            super(source);

            timeInMillSecs = source.readLong();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeLong(timeInMillSecs);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
