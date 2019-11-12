package com.MartyrPher.smapiandroidinstaller;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows the tabs at the top of the screen
 */
public class TabAdapter extends FragmentStatePagerAdapter {

    //List of the fragments
    private final static List<Fragment> mFragmentList = new ArrayList<>();

    //List of the fragment titles
    private final static List<String> mTitleList = new ArrayList<>();

    /**
     * Constructor that sets the fragment manager
     * @param fragmentManager = The fragment manager
     */
    public TabAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);
    }

    /**
     * Override that gets the item at a specific position
     * @param position = The position of the item
     * @return the position in the fragment list
     */
    @Override
    public Fragment getItem(int position)
    {
        return mFragmentList.get(position);
    }

    /**
     * Add a fragment to the list and add the title
     * @param fragment = The fragment to add
     * @param title = The title to add
     */
    public void addFragment(Fragment fragment, String title)
    {
        mFragmentList.add(fragment);
        mTitleList.add(title);
    }

    /**
     * Get the page title
     * @param position = The position to get
     * @return the position of the title in the list
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return mTitleList.get(position);
    }

    /**
     * Get the count of fragments in the list
     * @return the size of the fragment list
     */
    @Override
    public int getCount()
    {
        return mFragmentList.size();
    }
}
