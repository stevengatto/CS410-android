package com.cs410.android.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs410.android.R;
import com.cs410.android.model.Course;
import com.cs410.android.util.AccountUtils;
import com.cs410.android.util.WebUtils;

import java.util.List;

import retrofit.client.Response;

/**
 *
 * Created by user on 2/25/2015.
 */
public class CourseListActivity extends Activity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        initialize();

    }

    private void initialize() {
        recyclerView = (RecyclerView) findViewById(R.id.course_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AccountUtils.getUnauthenticatedApiInterface().getCourses(new CourseListCallback(this));
    }

    private class CourseHolder extends RecyclerView.ViewHolder {

        private TextView title, author, category;

        public CourseHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.item_course_list_title);
            author = (TextView) itemView.findViewById(R.id.item_course_list_author);
            category = (TextView) itemView.findViewById(R.id.item_course_list_category);
        }

        private void bindCourse(Course course) {
            title.setText(course.title);
            author.setText(course.author.name);
            category.setText(course.category);
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
        public CourseHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_course_list, viewGroup, false);
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
            recyclerView.setAdapter(new CourseListAdapter(courses));
        }
    }
}
