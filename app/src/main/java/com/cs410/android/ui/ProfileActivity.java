package com.cs410.android.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cs410.android.R;
import com.overthink.mechmaid.util.Toaster;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends NavigationDrawerActivity implements ViewTreeObserver.OnScrollChangedListener {

    private ScrollView scrollView;
    private LinearLayout enrolledCoursesParent, scrollViewContents;
    private int toolbarHeight = 56;
    private int toolbarLeftMargin = 72;
    private int headerHeight, minHeaderTranslation;
    private RelativeLayout headerView;
    private FrameLayout toolbarHeaderFrame;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialize();
    }

    private void initialize() {
        setSupportActionBar((Toolbar) findViewById(R.id.profile_toolbar));
        getSupportActionBar().setTitle("");
        this.context = this;

        enrolledCoursesParent = (LinearLayout) findViewById(R.id.profile_courses_parent);
        scrollViewContents = (LinearLayout) findViewById(R.id.profile_scroll_view_contents);
        scrollView = (ScrollView) findViewById(R.id.profile_scroll_view);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(this);
        headerView = (RelativeLayout) findViewById(R.id.profile_header_layout);

        headerHeight = getResources().getDimensionPixelSize(R.dimen.profile_header_height)
                - getResources().getDimensionPixelSize(R.dimen.shadow_thickness);
        toolbarHeight = getResources().getDimensionPixelSize(R.dimen.profile_toolbar_height);
        minHeaderTranslation = headerHeight - toolbarHeight; // will be negative

        List<String> list = new ArrayList<>();
        list.add("Buying coffee from the store");
        list.add("Putting the coffee into the car");
        list.add("Opening the coffee bag safely");
        list.add("Measuring the perfect amount of coffee grounds");
        list.add("Measuring just enough water");
        list.add("Turning on the coffee maker");
        list.add("Watching the coffee brew");
        list.add("Adding cream and sugar to taste");
        list.add("Offering your fresh coffee to a good friend");
        list.add("Enjoying your freshly brewed cup of joe");
        list.add("Buying coffee from the store");
        list.add("Putting the coffee into the car");
        list.add("Opening the coffee bag safely");
        list.add("Measuring the perfect amount of coffee grounds");
        list.add("Measuring just enough water");
        list.add("Turning on the coffee maker");
        list.add("Watching the coffee brew");
        list.add("Adding cream and sugar to taste");
        list.add("Offering your fresh coffee to a good friend");
        list.add("Enjoying your freshly brewed cup of joe");
        list.add("Buying coffee from the store");
        list.add("Putting the coffee into the car");
        list.add("Opening the coffee bag safely");
        list.add("Measuring the perfect amount of coffee grounds");
        list.add("Measuring just enough water");
        list.add("Turning on the coffee maker");
        list.add("Watching the coffee brew");
        list.add("Adding cream and sugar to taste");
        list.add("Offering your fresh coffee to a good friend");
        list.add("Enjoying your freshly brewed cup of joe");
        list.add("Buying coffee from the store");
        list.add("Putting the coffee into the car");
        list.add("Opening the coffee bag safely");
        list.add("Measuring the perfect amount of coffee grounds");
        list.add("Measuring just enough water");
        list.add("Turning on the coffee maker");
        list.add("Watching the coffee brew");
        list.add("Adding cream and sugar to taste");
        list.add("Offering your fresh coffee to a good friend");
        list.add("Enjoying your freshly brewed cup of joe");

        for (int i=0; i<list.size(); i++) {
            TextView text = new TextView(this);
            text.setText(list.get(i));
            text.setPadding(0, 4, 0, 4);
            enrolledCoursesParent.addView(text);
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

        // This will collapse the header when scrolling, until its height reaches
        // the toolbar height
        headerView.setTranslationY(Math.max(-scrollY, -minHeaderTranslation));

        // Scroll ratio (0 <= ratio <= 1).
        // The ratio value is 0 when the header is completely expanded,
        // 1 when it is completely collapsed
        float offset = 1 - Math.max(
                (float) (-minHeaderTranslation - scrollY) / -minHeaderTranslation, 0f);
    }
}
