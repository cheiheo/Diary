package com.cheiheo.diary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cheiheo.diary.bean.Diary;
import com.cheiheo.diary.bean.DiaryList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DiaryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recyclerView = findViewById(R.id.diary_list_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // Test
        // DiaryList.getInstance(getApplicationContext()).deleteAll();
        // DiaryList.createTestListInDB(getApplicationContext());
        adapter = new DiaryAdapter(DiaryList.getInstance(getApplicationContext()).getDiaries()); //DiaryList.getTestList()
        recyclerView.setAdapter(adapter);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                //startActivity(intent);
                ContentActivity.startActivity(MainActivity.this);
            }
        });

        // test();
        MyBroadcastReceiver receiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.cheiheo.diary.TEST");
        registerReceiver(receiver, intentFilter);
    }

    private void test() {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = 6*1000;
        long target = SystemClock.elapsedRealtime() + time;
        Intent intent = new Intent(MainActivity.this, ContentActivity.class);
        PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
        PendingIntent pi2 = PendingIntent.getActivity(MainActivity.this, 1, intent, 0);
        manager.set(AlarmManager.RTC_WAKEUP, target, pi);
        manager.set(AlarmManager.RTC_WAKEUP, target + time, pi2);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, context.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        List<Diary> diaries = DiaryList.getInstance(getApplicationContext()).getDiaries();
        adapter.setDiaries(diaries);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent("com.cheiheo.diary.TEST");
            sendBroadcast(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public List<Diary> getAllDiaryTest() {
        DiaryList.createTestListInDB(getApplicationContext());
        List<Diary> diaries = DiaryList.getInstance(getApplicationContext()).getDiaries();
        return diaries;
    }
}
