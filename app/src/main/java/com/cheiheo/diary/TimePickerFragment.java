package com.cheiheo.diary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {

    public static final String ARG = "arg";
    private TimePicker timePicker;
    DialogOnClickOKListener listener;

    public static TimePickerFragment getInstance(Date date, DialogOnClickOKListener listener) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG, date);
        TimePickerFragment fragment = new TimePickerFragment(listener);
        fragment.setArguments(bundle);
        return fragment;
    }

    private TimePickerFragment(DialogOnClickOKListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        View view = LayoutInflater.from(getActivity())
            .inflate(R.layout.fragment_time_picker, null);
        timePicker =view.findViewById(R.id.time_picker);
        timePicker.setHour(hour);
        timePicker.setMinute(minute);

        return new AlertDialog.Builder(getActivity())
                .setTitle("设置时间")
                .setView(view)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();
                        listener.onClick(hour, minute);
                    }
                }).create();
    }

    public interface DialogOnClickOKListener {
        void onClick(int hour, int minute);
    }
}
