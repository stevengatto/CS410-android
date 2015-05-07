package com.cs410.android.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.melnykov.fab.FloatingActionButton;
import com.overthink.mechmaid.progress.ProgressableContentFrame;
import com.overthink.mechmaid.util.Toaster;

import java.util.ArrayList;
import java.util.List;

import retrofit.client.Response;

public class CourseSingleActivity extends ActionBarActivity {

    private TextView title, author, category, date;
    private RoundedImageView icon;
    private LinearLayout lessonListParent;
    private ProgressableContentFrame contentFrame;
    private ProgressBar iconProgressBar;
    private String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_single);
        initialize();
    }

    private void initialize() {
        // show progress dialog until the web request finishes
        contentFrame = (ProgressableContentFrame) findViewById(R.id.course_single_progress_content_frame);
        contentFrame.showProgress();

        // find views
        title = (TextView) findViewById(R.id.single_course_title);
        author = (TextView) findViewById(R.id.single_course_author);
        category = (TextView) findViewById(R.id.single_course_category);
        date = (TextView) findViewById(R.id.single_course_date);
        icon = (RoundedImageView) findViewById(R.id.single_course_icon);
        iconProgressBar = (ProgressBar) findViewById(R.id.single_course_icon_progress);
        lessonListParent = (LinearLayout) findViewById(R.id.single_course_lesson_list_parent);

        // go back when up button is pressed
        findViewById(R.id.course_single_up_nav_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // set default onclick listener for floating favorite button
        findViewById(R.id.course_single_favorite_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toaster.showToastFromString(getApplicationContext(), "Course favorited!");
            }
        });

        // make web request to retrieve course information
        courseId = getIntent().getExtras().getString("id");
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
                .load("http://lorempixel.com/200/200/")
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
        for (int i=0; lessons != null && i<lessons.size(); i++) {
            View listItem = getLayoutInflater().inflate(R.layout.item_lesson_list, null);
            listItem.setTag(lessons.get(i)); // set lesson object as tag so we can access id later

            // set on click listener for later
            listItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String lessonId = ((Lesson)v.getTag())._id;
                    Intent intent = new Intent(getApplicationContext(), LessonSingleActivity.class);
                    intent.putExtra("lessonId", lessonId);
                    intent.putExtra("courseId", courseId);
                    startActivity(intent);
//                    finish();
                }
            });

            if (i == 0) {
                // remove top vertical line
                listItem.findViewById(R.id.lesson_list_progress_circle_top_line).setVisibility(View.INVISIBLE);
            } else if (i == lessons.size() - 1) {
                // remove bottom vertical line and extra divider at the end
                listItem.findViewById(R.id.lesson_list_progress_circle_bottom_line).setVisibility(View.INVISIBLE);
                ((RelativeLayout) listItem.findViewById(R.id.lesson_list_bottom_frame))
                        .removeView(listItem.findViewById(R.id.lesson_list_divider));
            }
            if (i<=2) {
                // set background to green circle drawable
                listItem.findViewById(R.id.lesson_list_progress_circle)
                        .setBackground(getResources().getDrawable(R.drawable.circle_green));
            }
            else {

                // set background to red circle drawable
                listItem.findViewById(R.id.lesson_list_progress_circle)
                        .setBackground(getResources().getDrawable(R.drawable.circle_red));
            }
            // set titles
            ((TextView) listItem.findViewById(R.id.lesson_list_title))
                    .setText(i + 1 + ". " + lessons.get(i).title);
            // add lesson item to parent view
            lessonListParent.addView(listItem);
        }
    }

    // create mock lesson data for testing
//    private List<Lesson> getTestLessonsData() {
//        List<Lesson> list = new ArrayList<>();
//        list.add(new Lesson("1", "Buying coffee from the store"));
//        list.add(new Lesson("2", "Putting the coffee into the car"));
//        list.add(new Lesson("3", "Opening the coffee bag safely"));
//        list.add(new Lesson("4", "Measuring the perfect amount of coffee grounds"));
//        list.add(new Lesson("4", "Measuring just enough water"));
//        list.add(new Lesson("5", "Turning on the coffee maker"));
//        list.add(new Lesson("6", "Watching the coffee brew"));
//        list.add(new Lesson("7", "Adding cream and sugar to taste"));
//        list.add(new Lesson("8", "Offering your fresh coffee to a good friend"));
//        list.add(new Lesson("9", "Enjoying your freshly brewed cup of joe"));
//        return list;
//    }

    /**
     * Callback class for Course retrieving web request
     */
    private class CourseSingleCallback extends WebUtils.RetroCallback<Course> {
        public CourseSingleCallback(Context context) {
            super(context);
        }

        @Override
        public void success(Course course, Response response) {
            contentFrame.showContent();
//            course.lessons = getTestLessonsData();
            setDataInViews(course);
        }
    }
}
