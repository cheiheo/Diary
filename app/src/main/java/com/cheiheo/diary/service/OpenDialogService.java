package com.cheiheo.diary.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.cheiheo.diary.bean.Diary;
import com.cheiheo.diary.bean.DiaryList;
import com.cheiheo.diary.utils.GetDate;
import com.cheiheo.diary.utils.SendNotification;

import java.util.Date;

public class OpenDialogService extends Service {

    private static final String  TAG = "chenh";
    //AlertDialog dialog;

    public OpenDialogService() {
//        dialog = new AlertDialog.Builder(getApplicationContext())
//                .setTitle("title")
//                .setMessage("message")
//                .setPositiveButton("ok", null)
//                .setNegativeButton("back", null)
//                .create();
        Log.d(TAG, "service is creating");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // dialog.show();
        int id = intent.getIntExtra("id", -1);
        if (id >= 0) {
            Diary diary = DiaryList.getInstance(getApplicationContext()).getDiary(id);
            SendNotification sendNotification = new SendNotification(getApplicationContext());
            sendNotification.send(diary.getTitle(), diary.getContent());
            // 提醒完毕清除数据库记录
            diary.setTag(null);
            DiaryList.getInstance(getApplicationContext()).setTag(diary);
            Log.d(TAG, "启动时间" + GetDate.getDate(new Date()).toString());
        } else {
            Log.d(TAG, "id小于0");
        }
        stopSelf(); //只执行一次，用完停止服务
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
