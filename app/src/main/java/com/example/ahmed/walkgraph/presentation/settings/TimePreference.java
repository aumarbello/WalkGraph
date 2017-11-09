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
 *
 * Custom TimePreference To Allow user select time.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class TimePreference extends DialogPreference {
    /**
     * TimePicker Field and Constructors
     */

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

    /**
     * Persist the selected time to sharedPreference
     * and notifies dependent shared preferences.
     * @param hour selected.
     * @param minute selected.
     */
    private void setTime(int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        persistLong(calendar.getTimeInMillis());

        notifyChanged();
        notifyDependencyChange(shouldDisableDependents());
    }


    /**
     * Prepares the preference dialog to be displayed
     * by adding the timePicker as it's view.
     * @param builder AlertDialog builder to be used for customization.
     */
    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);

        builder.setView(timePicker);
    }

    /**
     * Called when the dialog is closed, checks if the dialog was closed
     * as a result of the user selecting the OK button
     * and not cancelling or touching outside.
     *
     * If the time selected is persisted only if the dialog was closed
     * as a result of the user clicking OK.
     * @param positiveResult if dialog was closed by clicking OK.
     */
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult){
            setTime(timePicker.getHour(), timePicker.getMinute());
        }
    }

    /**
     * Gets the default value of the shared preference.
     * @param a - typedArray to retrieve the value from.
     * @param index where the value is saved.
     * @return the Default value of the pref - long in this case.
     */
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return Long.valueOf(a.getString(index));
    }

    /**
     * Sets the initial value of the preference.
     * @param restorePersistedValue true if the value is retrieved from SharedPreference.
     * @param defaultValue of the Preference.
     */
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
        if(restorePersistedValue){
            Calendar calendar = Calendar.getInstance();
            long savedTime = getPersistedLong(calendar.getTimeInMillis());
            calendar.setTimeInMillis(savedTime);

            setTimePicker(calendar);
        }else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis((long) defaultValue);

            int [] fields = setTimePicker(calendar);
            //persists only if the value is not from sharedPreference.
            setTime(fields[0], fields[1]);
        }
    }

    /**
     * Sets the values of the time picker.
     * @param calendar to get values from.
     * @return int array containing the fields of the calender passed in.
     */
    private int[] setTimePicker(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        timePicker.setHour(hour);
        timePicker.setMinute(minute);

        return new int[]{hour, minute};
    }

    /**
     * States when to disable dependent preferences.
     *
     * When the previous hour and minute wasn't changed they are disabled.
     *
     * @return whether the dependent preferences should be disabled or not.
     */
    @Override
    public boolean shouldDisableDependents() {
        Calendar nowCalender = Calendar.getInstance();
        long persistedLong = getPersistedLong(nowCalender.getTimeInMillis());

        Calendar persistedCalender = Calendar.getInstance();
        persistedCalender.setTimeInMillis(persistedLong);
        boolean isEqual =  nowCalender.get(Calendar.HOUR_OF_DAY)
                == persistedCalender.get(Calendar.HOUR_OF_DAY)
                && nowCalender.get(Calendar.MINUTE)
                == persistedCalender.get(Calendar.MINUTE);
        return isEqual ||  super.shouldDisableDependents();
    }

    /**
     * Saves the instance of the preference, a calendar object with the hour and minute selected.
     * @return of the SaveState.
     */
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

    /**
     * Restoring the state of the preference.
     * @param state of get the saved value from.
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(savedState.timeInMillSecs);
        setTimePicker(calendar);
    }

    /**
     * Class to allow for saving instance of this preference.
     */
    private static class SavedState extends BaseSavedState{
        /**
         * Persistable long field of selected hour and minute and constructors.
         */
        long timeInMillSecs;
        public SavedState(Parcel source) {
            super(source);

            timeInMillSecs = source.readLong();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Writes the values of this preference to a parcel.
         * @param dest to write value to.
         * @param flags associated with parcel.
         */
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
