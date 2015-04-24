package com.cs410.android.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.cs410.android.R;
import com.overthink.mechmaid.util.Toaster;

/**
 * Created by steven on 4/21/15.
 */
public class NavigationDrawerActivity extends ActionBarActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ViewNavDrawerHeader navHeader;
    private Runnable runnable;
    private Context context;

    @Override
    public void setContentView(int layoutResID) {
        ViewGroup layout = (ViewGroup) findViewById(R.id.drawer_layout);
        layout.removeView(findViewById(R.id.content_frame));
        View newContent = getLayoutInflater().inflate(layoutResID, null);
        layout.addView(newContent, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_navigation_drawer);
        this.context = this;

        findViewById(R.id.nav_drawer_my_courses).setOnClickListener(this);
        findViewById(R.id.nav_drawer_discover).setOnClickListener(this);
        findViewById(R.id.nav_drawer_profile).setOnClickListener(this);

        navHeader = (ViewNavDrawerHeader) findViewById(R.id.navigation_drawer_header);
        navHeader.setName("Steven Gatto");
        Glide.with(this)
                .load("http://www.abcwpa.org/portals/77/placeholder-photo.png")
                .centerCrop()
                .into(navHeader.getImageView());

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.navigation_drawer_shadow, Gravity.START);

        // Setup toggle for navigation drawer in action bar
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {
            @Override
            public void onDrawerClosed(View view) {
                if (runnable != null) {
                    runnable.run();
                    runnable = null;
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) { }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) { }

            @Override
            public void onDrawerStateChanged(int newState) { }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(final View clickedView) {
        runnable = new Runnable() {
            @Override
            public void run() {
                switch (clickedView.getId()) {
                    case R.id.nav_drawer_my_courses:
                        Toaster.showToastFromString(context, "My Courses");
                        break;
                    case R.id.nav_drawer_discover:
                        startActivity(new Intent(context, CourseListActivity.class));
                        break;
                    case R.id.nav_drawer_profile:
                        startActivity(new Intent(context, ProfileActivity.class));
                        break;
                }
            }
        };
        drawerLayout.closeDrawer(Gravity.START);
    }
}
