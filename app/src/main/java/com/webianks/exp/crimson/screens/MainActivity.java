package com.webianks.exp.crimson.screens;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.webianks.exp.crimson.About;
import com.webianks.exp.crimson.R;
import com.webianks.exp.crimson.all_tests.Tests;
import com.webianks.exp.crimson.reports.SaveReports;
import com.webianks.exp.crimson.sleep_better.SleepBetter;
import com.webianks.exp.crimson.ViewPagerAdapter;
import com.webianks.exp.crimson.facts.Facts;
import com.webianks.exp.crimson.tips.HealthyEyeTips;

public class MainActivity extends AppCompatActivity {


    private ViewPager viewPager;
    private TabLayout tabLayout;

    private int[] tabIcons = {R.drawable.ic_format_quote_white_24dp, R.drawable.ic_assignment_white_24dp, R.drawable.ic_snooze_white_24dp, R.drawable.ic_view_agenda_white_24dp};
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

    }

    private void setupTabIcons() {

        try {
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);
            tabLayout.getTabAt(3).setIcon(tabIcons[3]);

        } catch (NullPointerException e) {
            Log.d(TAG, "NPE in tabLayout");
        }

    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Facts(), "Facts");
        adapter.addFragment(new Tests(), "Tests");
        adapter.addFragment(new SleepBetter(), "Sleep");
        adapter.addFragment(new HealthyEyeTips(), "Tips");
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.ic_maps)
            startActivity(new Intent(this,SearchClinics.class));
        else if (id == R.id.ic_reports)
            startActivity(new Intent(this, SaveReports.class));
        else if (id == R.id.ic_reminder)
            startActivity(new Intent(this,CheckupReminders.class));

        return super.onOptionsItemSelected(item);
    }
}
