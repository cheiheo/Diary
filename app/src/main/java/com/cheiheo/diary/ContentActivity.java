package com.cheiheo.diary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cheiheo.diary.bean.Diary;
import com.cheiheo.diary.bean.DiaryList;
import com.cheiheo.diary.utils.GetDate;
import com.cheiheo.diary.utils.LinedEditText;

public class ContentActivity extends AppCompatActivity {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String DATE = "date";
    private TextView date;
    private EditText title;
    private LinedEditText content;
    private boolean isNew;

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
        }
    }

    private void init() {
        date = findViewById(R.id.date_tv_content);
        title = findViewById(R.id.title_edt_content);
        content = findViewById(R.id.content_edt_content);
        isNew = getIntent().getBooleanExtra("isNew", true);
    }

    private void initUI() {
        Intent intent = getIntent();
        int id = intent.getIntExtra(ID, -1);
        diary = DiaryList.getInstance(getApplicationContext()).getDiary(id);
        date.setText(diary.getDate());
        title.setText(diary.getTitle());
        content.setText(diary.getContent());
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_delete:{
                if (!isNew) {
                    DiaryList.getInstance(getApplicationContext()).deleteDiary(diary);
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
                    if (title.getText().toString().isEmpty() || content.getText().toString().isEmpty()) {
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
        }

        return super.onOptionsItemSelected(item);
    }
}
