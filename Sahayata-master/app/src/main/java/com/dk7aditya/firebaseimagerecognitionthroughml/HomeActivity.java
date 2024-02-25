package com.dk7aditya.firebaseimagerecognitionthroughml;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dk7aditya.firebaseimagerecognitionthroughml.adapters.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;


public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.HomeTheme);
        setContentView(R.layout.activity_home);
        setSupportActionBar((Toolbar)findViewById(R.id.sahayataToolBar));
        final TabLayout tabLayout = findViewById(R.id.tabBar);
        TabItem tabActivity = findViewById(R.id.tabActivity);
        TabItem tabFriends = findViewById(R.id.tabFriends);
        TabItem tabFeed = findViewById(R.id.tabFeed);
        final ViewPager viewPager = findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new
                PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(pagerAdapter);

        //If the Tabs are changed by clicking the Tabs
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //If the Tabs are changed by swiping the pages
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ellipsis_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_menu_item_1:
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(HomeActivity.this, LoginActivity.class);
                intToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intToMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intToMain);
                overridePendingTransition(0,0);
                return true;
            case R.id.option_menu_item_2:
                Intent intToNewGroup = new Intent(HomeActivity.this, GetAllUsersForGroupActivity.class);
                startActivity(intToNewGroup);
                overridePendingTransition(0,0);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
       // return super.onOptionsItemSelected(item);
    }
}