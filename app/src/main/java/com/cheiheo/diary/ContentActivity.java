package com.cheiheo.diary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.cheiheo.diary.bean.Diary;
import com.cheiheo.diary.bean.DiaryList;
import com.cheiheo.diary.service.OpenDialogService;
import com.cheiheo.diary.utils.GetDate;
import com.cheiheo.diary.utils.LinedEditText;
import com.cheiheo.diary.utils.SendNotification;

import java.util.Date;

public class ContentActivity extends AppCompatActivity {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String DATE = "date";
    private TextView date;
    private EditText title;
    private LinedEditText content;
    private boolean isNew;
    private boolean hasTag;

    private LinearLayout linearLayout;
    private TextView dateOnClock;

    private Diary diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        init();
        if (!isNew) {
            initUI();
        } else {
            date.setText(GetDate.getDate());
            linearLayout.setVisibility(View.GONE);
            //findViewById(R.id.action_clock).setVisibility(View.GONE);
        }
    }

    private void init() {
        date = findViewById(R.id.date_tv_content);
        title = findViewById(R.id.title_edt_content);
        content = findViewById(R.id.content_edt_content);
        isNew = getIntent().getBooleanExtra("isNew", true);
        hasTag = false;
        linearLayout = findViewById(R.id.clock_Linear);
    }

    private void initUI() {
        Intent intent = getIntent();
        int id = intent.getIntExtra(ID, -1);
        diary = DiaryList.getInstance(getApplicationContext()).getDiary(id);
        date.setText(diary.getDate());
        title.setText(diary.getTitle());
        content.setText(diary.getContent());
        // 提醒功能 UI控制
        linearLayout.setVisibility(View.GONE);
        dateOnClock = findViewById(R.id.time_clock_Linear);
        findViewById(R.id.delete_clock_Linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAlarm(diary.getId());
                diary.setTag(null);
                DiaryList.getInstance(getApplicationContext()).setTag(diary);
                linearLayout.setVisibility(View.GONE);
            }
        });
        if (diary.getTag() != null) {
            hasTag = true;
            dateOnClock.setText(diary.getTag());
            linearLayout.setVisibility(View.VISIBLE);
            Log.d("chenh", "has tag");
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ContentActivity.class);
        // intent.putExtra("isNew", true);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int diaryID) { //Diary diary
        Intent intent = new Intent(context, ContentActivity.class);
        intent.putExtra(ID, diaryID);
//        intent.putExtra(ID, diary.getId());
//        intent.putExtra(DATE, diary.getDate());
//        intent.putExtra(TITLE, diary.getTitle());
//        intent.putExtra(CONTENT, diary.getContent());
        intent.putExtra("isNew", false);
        context.startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content, menu);
        if (isNew) {
            menu.getItem(0).setVisible(false);
        }
        //Toast.makeText(ContentActivity.this, menu.getItem(2).toString(), Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_delete:{
                if (!isNew) {
                    DiaryList.getInstance(getApplicationContext()).deleteDiary(diary);
                    SendNotification sendNotification = new SendNotification(getApplicationContext());
                    sendNotification.send("Diary:" + title.getText().toString() ,"已删除");
                }
                ContentActivity.this.finish();
                break;
            }
            case R.id.action_save:{
                if (!isNew) {
                    diary.setDate(GetDate.getDate().toString());
                    diary.setTitle(title.getText().toString());
                    diary.setContent(content.getText().toString());
                    DiaryList.getInstance(getApplicationContext()).updateDiary(diary);
                } else {
                    if (title.getText().toString().isEmpty() && content.getText().toString().isEmpty()) {
                        ContentActivity.this.finish();
                    } else {
                        diary = new Diary(
                                GetDate.getDate().toString(),
                                title.getText().toString(),
                                content.getText().toString()
                        );
                        DiaryList.getInstance(getApplicationContext()).addDiary(diary);
                    }
                }
                ContentActivity.this.finish();
                break;
            }
            case R.id.action_clock:{
                /*Intent intent = new Intent(ContentActivity.this, OpenDialogService.class);
                startService(intent);*/
                //testBroadcard();
                //testService();
                //showTimeDialog();

                if (hasTag) {
                    removeAlarm(diary.getId());
                }
                DatePickerFragment.DialogOnClickOKListener listener = new DatePickerFragment.DialogOnClickOKListener() {
                    @Override
                    public void onClickOk(final Date date) {
                        TimePickerFragment.DialogOnClickOKListener listener2 = new TimePickerFragment.DialogOnClickOKListener() {
                            @Override
                            public void onClick(final int hour, final int minute) {
                                linearLayout.setVisibility(View.VISIBLE);
                                dateOnClock.setText(GetDate.getDate(date, hour, minute).toString());
                                // testService(getCompleteDate(date, hour, minute));
                                setAlarm(getCompleteDate(date, hour, minute), diary.getId());
                                diary.setTag(GetDate.getDate(getCompleteDate(date, hour, minute)).toString());
                                DiaryList.getInstance(getApplicationContext()).setTag(diary); //或者直接调用updateDiary(),但是其它数据也一起更新
                            }
                        };
                        TimePickerFragment dialog2 = TimePickerFragment.getInstance(new Date(), listener2);
                        dialog2.show(getSupportFragmentManager(), "");
                        //Toast.makeText(ContentActivity.this, date.toString(), Toast.LENGTH_SHORT).show();
                    }
                };
                DatePickerFragment dialog = DatePickerFragment.getInstance(new Date(), listener);
                dialog.show(getSupportFragmentManager(), "");

                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 将日期加上小时和分钟
     * @param date
     * @param hour
     * @param min
     * @return 完整的日期
     */
    public Date getCompleteDate(Date date, int hour, int min) {
        long time = date.getTime() + hour*60*60*1000 + min*60*1000;
        return new Date(time);
    }

    /**
     * 设置提醒
     * @param date
     * @param id 以id作为请求码，因为id唯一不会导致冲突
     */
    private void setAlarm(Date date, int id) {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long target = date.getTime();
        Intent intent = new Intent(ContentActivity.this, OpenDialogService.class);
        intent.putExtra(ID, diary.getId());
        PendingIntent pi = PendingIntent.getService(ContentActivity.this, id, intent, 0);
        //manager.setExact(AlarmManager.RTC_WAKEUP, target, pi);
        manager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, target, pi);
    }

    /**
     * 取消提醒
     * @param id
     */
    private void removeAlarm(int id) {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(ContentActivity.this, OpenDialogService.class);
        PendingIntent pi = PendingIntent.getService(ContentActivity.this, id, intent, 0);
        manager.cancel(pi);
    }

    /**=================================
     * 以下是测试方法
     */
    private void testService(Date date) {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //int time = 150*1000;
        //Date now = new Date();
        //long time = date.getTime() - now.getTime();
        //long target = SystemClock.elapsedRealtime() + time;
        long target = date.getTime();
        Log.d("chenh", String.valueOf(target));
        Log.d("chenh", GetDate.getDate(date).toString());
        Intent intent = new Intent(ContentActivity.this, OpenDialogService.class);
        PendingIntent pi = PendingIntent.getService(ContentActivity.this, 1, intent, 0);
        assert manager != null;
        manager.setExact(AlarmManager.RTC_WAKEUP, target, pi);
    }

    private void showTimeDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(ContentActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            }
        }, 1, 1, true);

        timePickerDialog.show();
    }

    private void testBroadcard() {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = 6*1000;
        long target = SystemClock.elapsedRealtime() + time;
        Intent intent = new Intent("com.cheiheo.diary.TEST");
        PendingIntent pi = PendingIntent.getBroadcast(ContentActivity.this, 1, intent, 0);
        manager.set(AlarmManager.RTC_WAKEUP, target, pi);
    }

}
