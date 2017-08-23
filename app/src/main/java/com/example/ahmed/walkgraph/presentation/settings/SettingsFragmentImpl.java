package com.example.ahmed.walkgraph.presentation.settings;

import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.ahmed.walkgraph.App;
import com.example.ahmed.walkgraph.R;


import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ahmed on 8/9/17.
 */

public class SettingsFragmentImpl extends Fragment implements SettingsFragment {
    public interface SettingsCallBack{
        void switchToList();
        void switchToMap();
    }

    private SettingsCallBack callBack;
    private Unbinder unbinder;
    @Inject
    SettingsPresenterImpl presenter;

    @BindView(R.id.start_time_button)
    Button startButton;

    @BindView(R.id.stop_time_button)
    Button stopTimeButton;

    @BindView(R.id.notification_time_button)
    Button notificationTimeButton;

    @BindView(R.id.interval_selector)
    Spinner intervalSpinner;

    @BindView(R.id.days_spinner)
    Spinner daysSpinner;

    @BindView(R.id.animate_switch)
    Switch animateSwitch;

    @BindView(R.id.bottom_nav)
    BottomNavigationView navigationView;


    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        ((App)getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_settings, parent, false);
        unbinder = ButterKnife.bind(this, view);
        updatePreferences();
        setNavListener();
        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof SettingsCallBack){
            callBack = (SettingsCallBack) context;
        }else
            throw new RuntimeException(context.toString() + "Must implement SettingsCallBack");
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void saveAnimation() {
        presenter.saveAnimation(animateSwitch.isChecked());
    }

    @Override
    public void saveGraphKeepTime() {
        ArrayAdapter<CharSequence> graphKeepAdapter;
        graphKeepAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.graph_keep, android.R.layout.simple_spinner_item);
        graphKeepAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        daysSpinner.setAdapter(graphKeepAdapter);
        intervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        presenter.saveGraphKeepTime(1);
                        break;
                    case 1:
                        presenter.saveGraphKeepTime(2);
                        break;
                    case 2:
                        presenter.saveGraphKeepTime(3);
                        break;
                    case 3:
                        presenter.saveGraphKeepTime(4);
                        break;
                    case 4:
                        presenter.saveGraphKeepTime(5);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void saveGraphNotificationTime() {
        notificationTimeButton.setOnClickListener(view ->
            setNotificationTime()
        );
    }

    @Override
    public void savePollingStartTime() {
        startButton.setOnClickListener(view -> setStartTime());
    }

    @Override
    public void savePollingStopTime() {
        stopTimeButton.setOnClickListener(view -> setEndTime());
    }

    @Override
    public void calculateAndSaveLocationFrequency() {
        presenter.calculateAndSaveLocationFrequency();
    }

    @Override
    public void pollingInterval() {
        ArrayAdapter<CharSequence> intervalAdapter;
        intervalAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.polling_interval, android.R.layout.simple_spinner_item);
        intervalAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        intervalSpinner.setAdapter(intervalAdapter);
        intervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        presenter.pollingInterval(15);
                        break;
                    case 1:
                        presenter.pollingInterval(30);
                        break;
                    case 2:
                        presenter.pollingInterval(45);
                        break;
                    case 3:
                        presenter.pollingInterval(60);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void updatePreferences() {
        saveAnimation();
        saveGraphKeepTime();
        saveGraphNotificationTime();
        savePollingStartTime();
        savePollingStopTime();
        calculateAndSaveLocationFrequency();
        pollingInterval();
    }

    @Override
    public void setNavListener() {
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.list:
                    callBack.switchToList();
                    break;
                case R.id.map:
                    callBack.switchToMap();
                    break;
            }
            return false;
        });
    }

    @Override
    public void setStartTime() {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(getActivity(), (timePicker, hour, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);

            SimpleDateFormat sdf = new SimpleDateFormat
                    ("h:mm a", Locale.US);
            String dateString = sdf.format(calendar.getTime());

            startButton.setText(dateString);
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        dialog.show();
        presenter.savePollingStartTime(calendar.getTimeInMillis());
    }

    @Override
    public void setEndTime() {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(getActivity(), (timePicker, hour, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);

            SimpleDateFormat sdf = new SimpleDateFormat
                    ("h:mm a", Locale.US);
            String dateString = sdf.format(calendar.getTime());

            stopTimeButton.setText(dateString);
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        dialog.show();
        presenter.savePollingStartTime(calendar.getTimeInMillis());
    }

    @Override
    public void setNotificationTime() {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(getActivity(), (timePicker, hour, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);

            SimpleDateFormat sdf = new SimpleDateFormat
                    ("h:mm a", Locale.US);
            String dateString = sdf.format(calendar.getTime());

            notificationTimeButton.setText(dateString);
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        dialog.show();
        presenter.saveGraphNotificationTime(calendar.getTimeInMillis());
    }


}
