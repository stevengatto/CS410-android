package com.cs410.android.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.cs410.android.R;
import com.cs410.android.model.Course;
import com.cs410.android.model.Lesson;
import com.cs410.android.util.AccountUtils;
import com.cs410.android.util.WebUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.overthink.mechmaid.progress.ProgressableContentFrame;

import java.util.ArrayList;
import java.util.List;

import retrofit.client.Response;

public class CourseSingleActivity extends ActionBarActivity {

    private TextView title, author, category, date;
    private RoundedImageView icon;
    private LinearLayout lessonListParent;
    private ProgressableContentFrame contentFrame;
    private ProgressBar iconProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course_single);
        initialize();
    }

    private void initialize() {
        contentFrame = (ProgressableContentFrame) findViewById(R.id.course_single_progress_content_frame);
        contentFrame.showProgress();

        title = (TextView) findViewById(R.id.single_course_title);
        author = (TextView) findViewById(R.id.single_course_author);
        category = (TextView) findViewById(R.id.single_course_category);
        date = (TextView) findViewById(R.id.single_course_date);
        icon = (RoundedImageView) findViewById(R.id.single_course_icon);
        iconProgressBar = (ProgressBar) findViewById(R.id.single_course_icon_progress);
        lessonListParent = (LinearLayout) findViewById(R.id.single_course_lesson_list_parent);

        findViewById(R.id.course_single_up_nav_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String courseId = getIntent().getExtras().getString("id");
        AccountUtils.getUnauthenticatedApiInterface().getCourse(courseId, new CourseSingleCallback(this));
    }

    private void setDataInViews(Course course) {
        // setup data in static views
        title.setText(course.title);
        author.setText(course.author.name);
        category.setText(course.category);
        date.setText(formatDate(course.date));
        iconProgressBar.setVisibility(View.VISIBLE);

        // set rounded image
        Glide.with(getApplicationContext())
                .load("http://loremflickr.com/144/144")
                .centerCrop()
                .into(new GlideDrawableImageViewTarget(icon) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        iconProgressBar.setVisibility(View.GONE);
                    }
                });

        // populate lessons programatically in LinearLayout
        addLessonsToLayout(course.lessons);
    }

    private String formatDate(String date) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December" };
        String[] split = date.split("-");
        return months[Integer.parseInt(split[1].trim())] + " " + split[2].split("T")[0] +
                ", " + split[0];
    }

    private void addLessonsToLayout(List<Lesson> lessons) {
        for (int i=0; i<lessons.size(); i++) {
            TextView lessonTextView = new TextView(this);
            lessonTextView.setText("Lesson " + i + ": " + lessons.get(i).title);
            lessonTextView.setPadding(0, 4, 0, 0);
            lessonTextView.setTextSize(20);
            lessonListParent.addView(lessonTextView);
        }
        lessonListParent.invalidate();
    }

    private List<Lesson> getTestLessonsData() {
        List<Lesson> list = new ArrayList<>();
        list.add(new Lesson("1", "Buying coffee from the store"));
        list.add(new Lesson("2", "Putting the coffee into the car"));
        list.add(new Lesson("3", "Opening the coffee bag safely"));
        list.add(new Lesson("4", "Measuring the perfect amount of coffee grounds"));
        list.add(new Lesson("4", "Measuring just enough water"));
        list.add(new Lesson("5", "Turning on the coffee maker"));
        list.add(new Lesson("6", "Watching the coffee brew"));
        list.add(new Lesson("7", "Adding cream and sugar to taste"));
        list.add(new Lesson("8", "Offering your fresh coffee to a good friend"));
        list.add(new Lesson("9", "Enjoying your freshly brewed cup of joe"));
        return list;
    }

    private class CourseSingleCallback extends WebUtils.RetroCallback<Course> {
        public CourseSingleCallback(Context context) {
            super(context);
        }

        @Override
        public void success(Course course, Response response) {
            contentFrame.showContent();
            course.lessons = getTestLessonsData();
            setDataInViews(course);
        }
    }
}
