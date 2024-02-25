package com.dk7aditya.firebaseimagerecognitionthroughml.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dk7aditya.firebaseimagerecognitionthroughml.fragments.ActivityFragment;
import com.dk7aditya.firebaseimagerecognitionthroughml.fragments.FeedFragment;
import com.dk7aditya.firebaseimagerecognitionthroughml.fragments.FriendsFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    /*
    The pager adapter Handles the number of pages added.
    This is very useful in extending the Tab View
     */

    private int numOfTabs;

    public PagerAdapter(FragmentManager fm , int numOfTabs){
        super(fm);
        this.numOfTabs = numOfTabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new ActivityFragment();
            case 1:
                return new FriendsFragment();
            case 2:
                return new FeedFragment();
            default:
                return null;
        }



    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}