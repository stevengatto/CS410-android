package com.cs410.android.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.cs410.android.R;
import com.cs410.android.model.Course;
import com.cs410.android.util.AccountUtils;
import com.cs410.android.util.WebUtils;
import com.overthink.mechmaid.progress.ProgressableContentFrame;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 *
 * Created by user on 2/25/2015.
 */
public class CourseListActivity extends NavigationDrawerActivity {

    private RecyclerView recyclerView;
    private ProgressableContentFrame contentFrame;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CourseListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        initialize();
    }

    private void initialize() {
        contentFrame = (ProgressableContentFrame) findViewById(R.id.course_list_progress_content_frame);
        contentFrame.showProgress();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.course_list_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(null, "Refresh pull");
                refreshListView();
            }
        });
        swipeRefreshLayout.setColorSchemeColors(
                R.color.refresh_spinner_1,
                R.color.refresh_spinner_2,
                R.color.refresh_spinner_3,
                R.color.refresh_spinner_4);

        recyclerView = (RecyclerView) findViewById(R.id.course_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setSupportActionBar((Toolbar) findViewById(R.id.course_list_toolbar));
        getSupportActionBar().setTitle("Discover");

        // Enable the navigation drawer icon
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AccountUtils.getUnauthenticatedApiInterface().getCourseList(new CourseListCallback(this));
    }

    private void refreshListView() {
        AccountUtils.getUnauthenticatedApiInterface().getCourseList(
                new WebUtils.RetroCallback<List<Course>>(this) {
            @Override
            public void success(List<Course> courses, Response response) {
                swipeRefreshLayout.setRefreshing(false);
                adapter.courseList = courses;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                super.failure(retrofitError);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private class CourseHolder extends RecyclerView.ViewHolder {

        private TextView title, author, description;
        private ImageView icon;
        private ProgressBar iconProgressBar;

        public CourseHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.item_course_list_title);
            author = (TextView) itemView.findViewById(R.id.item_course_list_author);
            description = (TextView) itemView.findViewById(R.id.item_course_list_desc);
            icon = (ImageView) itemView.findViewById(R.id.item_course_list_icon);
            iconProgressBar = (ProgressBar) itemView.findViewById(R.id.item_course_list_icon_progress);
        }

        private void bindCourse(Course course) {
            title.setText(course.title);
            author.setText(course.author.name);
            description.setText(course.description.length() > 0 ? " \u2015 " + course.description : "");
            iconProgressBar.setVisibility(View.VISIBLE);

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
        }
    }

    /**
     * Adapter class
     */
    private class CourseListAdapter extends RecyclerView.Adapter<CourseHolder> {

        private List<Course> courseList;

        public CourseListAdapter(List<Course> courseList) {
            this.courseList = courseList;
        }

        @Override
        public CourseHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_course_list, viewGroup, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = recyclerView.getChildPosition(v);
                    String courseId = courseList.get(position)._id;
                    Intent activityIntent = new Intent(viewGroup.getContext(), CourseSingleActivity.class);
                    activityIntent.putExtra("id", courseId);
                    startActivity(activityIntent);
                }
            });
            return new CourseHolder(view);
        }

        @Override
        public void onBindViewHolder(CourseHolder courseHolder, int i) {
            Course currentCourse = courseList.get(i);
            courseHolder.bindCourse(currentCourse);
        }

        @Override
        public int getItemCount() {
            return courseList.size();
        }
    }

    /**
     * Handle GET course list request
     */
    private class CourseListCallback extends WebUtils.RetroCallback<List<Course>> {

        public CourseListCallback(Context context) {
            super(context);
        }

        @Override
        public void success(List<Course> courses, Response response) {
            contentFrame.showContent();
            recyclerView.setAdapter((adapter = new CourseListAdapter(courses)));
        }
    }
}
