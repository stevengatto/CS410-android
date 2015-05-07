package com.cs410.android.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cs410.android.R;
import com.cs410.android.model.Lesson;
import com.cs410.android.util.AccountUtils;
import com.cs410.android.util.WebUtils;
import com.overthink.mechmaid.util.Toaster;

import retrofit.client.Response;

public class LessonSingleActivity extends ActionBarActivity {

    TextView title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_single);
        initialize();
    }

    void initialize() {
        String courseId = getIntent().getStringExtra("courseId");
        String lessonId = getIntent().getStringExtra("lessonId");
        setSupportActionBar((Toolbar) findViewById(R.id.lesson_toolbar));
        getSupportActionBar().setTitle("Lesssons");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = (TextView) findViewById(R.id.lesson_single_title);
        content = (TextView) findViewById(R.id.lesson_single_content);
        AccountUtils.getUnauthenticatedApiInterface().getLesson(courseId, lessonId,
                new LessonCallback(this));
    }

    private void setDataInViews(Lesson lesson) {
        title.setText(lesson.title);
        content.setText(stripHtmlTags(lesson.content));
    }

    private String stripHtmlTags(String in) {
        String out = "";
        boolean ignoring = false;
        for (int i = 0; i < in.length(); i++) {
            if (!ignoring) {
                if (in.charAt(i) != '<') {
                    out += in.charAt(i);
                } else {
                    ignoring = true;
                }
            }
            else if (in.charAt(i) == '>') {
                ignoring = false;
            }
        }
        return out;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

private class LessonCallback extends WebUtils.RetroCallback<Lesson> {

    public LessonCallback(Context context) {
        super(context);
    }

    @Override
    public void success(Lesson lesson, Response response) {
        setDataInViews(lesson);
    }
}
}
