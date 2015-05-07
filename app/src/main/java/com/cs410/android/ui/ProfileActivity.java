package com.cs410.android.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.cs410.android.R;
import com.cs410.android.account.Authenticatable;
import com.cs410.android.model.Author;
import com.cs410.android.model.Course;
import com.cs410.android.model.User;
import com.cs410.android.util.AccountUtils;
import com.cs410.android.util.CourseAppApi;
import com.cs410.android.util.WebUtils;
import com.overthink.mechmaid.util.Toaster;

import org.apache.http.auth.AUTH;

import java.util.ArrayList;
import java.util.List;

import retrofit.client.Response;

public class ProfileActivity extends NavigationDrawerActivity implements
        ViewTreeObserver.OnScrollChangedListener, Authenticatable {

    private ScrollView scrollView;
    private LinearLayout enrolledCoursesParent;
    private int minHeaderTranslation;
    private RelativeLayout headerView;
    private ImageView image;
    private TextView name, email;
    private ProgressBar imageProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialize();
    }

    private void initialize() {
        setSupportActionBar((Toolbar) findViewById(R.id.profile_toolbar));
        getSupportActionBar().setTitle("");

        enrolledCoursesParent = (LinearLayout) findViewById(R.id.profile_courses_parent);
        scrollView = (ScrollView) findViewById(R.id.profile_scroll_view);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(this);
        headerView = (RelativeLayout) findViewById(R.id.profile_header_layout);
        image = (ImageView) headerView.findViewById(R.id.profile_header_image);
        name = (TextView) headerView.findViewById(R.id.profile_header_name);
        email = (TextView) findViewById(R.id.profile_email);
        imageProgress = (ProgressBar) findViewById(R.id.profile_header_image_progress);

        int headerHeight = getResources().getDimensionPixelSize(R.dimen.profile_header_height)
                - getResources().getDimensionPixelSize(R.dimen.shadow_thickness);
        int toolbarHeight = getResources().getDimensionPixelSize(R.dimen.profile_toolbar_height);
        minHeaderTranslation = headerHeight - toolbarHeight; // will be negative

        AccountUtils.authenticate(this, this);
    }

    private List<Course> getMockCourses() {
        List<Course> list = new ArrayList<>();
        Author author1 = new Author("Steven Gatto");
        Author author2 = new Author("Matthew Ciampa");
        Author author3 = new Author("Dave Kraytsberg");
        Author author4 = new Author("Joe Cullen");

        list.add(new Course(author1, "How to Tie Your Shoes",
                "Learn how to tie your shoes, fast, easy, and reliably!"));
        list.add(new Course(author3, "Dress Like a Ciamp!",
                "Get your style game up, while still looking kewl in school."));
        list.add(new Course(author2, "Programming 101",
                "How to not suck at programming. This skill cannot be learned."));
        list.add(new Course(author1, "How to Rap",
                "Spit better lines, get the girls, and do drugs."));
        list.add(new Course(author4, "Pretend You Code",
                "Just say buzzwords that people don't understand and you're in."));
        list.add(new Course(author2, "Ironing Pants",
                "No wrinkles and a lot of wasted time. This is ironing 101."));
        list.add(new Course(author1, "Making Grilled Cheese",
                "The best grilled cheese guide on the web. Become a pro today!"));
        list.add(new Course(author4, "Brew your own beer",
                "Get drunk for cheap. It's the best deal there is."));
        list.add(new Course(author4, "Get Girls to Like You",
                "Be yourself, Just kidding that never works!"));
        list.add(new Course(author3, "How to Earn a 4.0",
                "Go to school, do your work, and remember why you're in college."));
        list.add(new Course(author2, "Passing Calculus",
                "Get through calculus with ease! This will get you through it."));
        list.add(new Course(author3, "Remove Makeup from a Pillow",
                "Good luck, there is no way!"));

        return list;
    }

    private void setDataInViews(User user) {
        name.setText(user.name);
        email.setText(user.email);

        // set rounded image
        Glide.with(getApplicationContext())
                .load("http://lorempixel.com/200/200/people/")
                .centerCrop()
                .into(new GlideDrawableImageViewTarget(image) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        imageProgress.setVisibility(View.GONE);
                    }
                });

        for (int i=0; i<user.enrolledCourses.size(); i++) {
            LayoutInflater inflater = getLayoutInflater();
            View courseListItem = inflater.inflate(R.layout.item_course_list, enrolledCoursesParent, false);
            ((TextView)courseListItem.findViewById(R.id.item_course_list_title))
                    .setText(user.enrolledCourses.get(i).title);
            ((TextView)courseListItem.findViewById(R.id.item_course_list_author))
                    .setText(user.enrolledCourses.get(i).author.name);
            ((TextView)courseListItem.findViewById(R.id.item_course_list_desc))
                    .setText(" \u2015 " + user.enrolledCourses.get(i).description);
            ((ImageView)courseListItem.findViewById(R.id.item_course_list_icon))
                    .setImageResource(R.drawable.ic_launcher);
            courseListItem.findViewById(R.id.item_course_list_icon_progress)
                    .setVisibility(View.INVISIBLE);
            Glide.with(getApplicationContext())
                    .load("http://lorempixel.com/200/200/")
                    .centerCrop()
                    .into((ImageView)courseListItem.findViewById(R.id.item_course_list_icon));
            enrolledCoursesParent.addView(courseListItem);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            Toaster.showToastFromString(this, "Sign me out");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollChanged() {
        int scrollY = scrollView.getScrollY();

        // Collapse the header when scrolling, until its height reaches the toolbar height
        headerView.setTranslationY(Math.max(-scrollY, -minHeaderTranslation));

        // Scroll ratio (0 <= ratio <= 1). 1 when fully opened, 0 when fully collapsed
        float ratioComplete = 1 - Math.min(((float) -scrollY / -minHeaderTranslation), 1f);
        image.setAlpha(ratioComplete);
    }

    @Override
    public void onAuthReceived(boolean success, CourseAppApi api, Bundle bundle) {
        if (success) {
            api.getProfile(new ProfileCallback(this));
        }
    }

    /**
     * Callback class for Course retrieving web request
     */
    private class ProfileCallback extends WebUtils.RetroCallback<User> {
        public ProfileCallback(Context context) {
            super(context);
        }

        @Override
        public void success(User user, Response response) {
//            contentFrame.showContent();
            user.enrolledCourses = getMockCourses();
            setDataInViews(user);
        }
    }
}
