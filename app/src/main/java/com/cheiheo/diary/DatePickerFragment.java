package com.cheiheo.diary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    public static final String ARG = "arg";
    private DatePicker datePicker;
    DialogOnClickOKListener listener;

    public static DatePickerFragment getInstance(Date date, DialogOnClickOKListener listener) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG, date);
        DatePickerFragment fragment = new DatePickerFragment(listener);
        fragment.setArguments(bundle);
        return fragment;
    }

    private DatePickerFragment(DialogOnClickOKListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_date_picker, null);
        datePicker = view.findViewById(R.id.date_picker);
        datePicker.init(year, month, day, null);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("设置时间")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int year = datePicker.getYear();
                                int month = datePicker.getMonth();
                                int day = datePicker.getDayOfMonth();
                                Date backDate = new GregorianCalendar(year, month, day).getTime();
                                listener.onClickOk(backDate);
                            }
                        })
                .create();
    }

    public interface DialogOnClickOKListener {
        void onClickOk(Date date);
    }
}
