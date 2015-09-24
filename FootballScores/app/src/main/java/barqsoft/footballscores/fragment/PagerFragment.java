package barqsoft.footballscores.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;

public class PagerFragment extends Fragment {
    private static final String LOG_TAG = PagerFragment.class.getSimpleName();

    public static final int TOTAL_NUM_OF_PAGES = 5;
    public ViewPager viewPager;
    private CustomPageAdapter customPageAdapter;
    private MainScreenFragment[] mainScreenFragments = new MainScreenFragment[TOTAL_NUM_OF_PAGES];

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pager_fragment, container, false);
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        customPageAdapter = new CustomPageAdapter(getChildFragmentManager());
        for (int i = 0; i < TOTAL_NUM_OF_PAGES; i++) {
            Date fragmentDate = new Date(System.currentTimeMillis() + ((i - 2) * 86400000));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            mainScreenFragments[i] = new MainScreenFragment();
            mainScreenFragments[i].setFragmentDate(simpleDateFormat.format(fragmentDate));
        }
        viewPager.setAdapter(customPageAdapter);
        viewPager.setCurrentItem(MainActivity.CURRENT_FRAGMENT);
        return rootView;
    }

    private class CustomPageAdapter extends FragmentStatePagerAdapter {
        @Override
        public Fragment getItem(int i) {
            Log.v(LOG_TAG, "i of getItem(int i): " + i);
            MainActivity.CURRENT_FRAGMENT = i;
            return mainScreenFragments[i];
        }

        @Override
        public int getCount() {
            return TOTAL_NUM_OF_PAGES;
        }

        public CustomPageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return getLocalDayFromDate(getActivity(), System.currentTimeMillis() + ((position - 2) * 86400000));
        }

        public String getLocalDayFromDate(Context context, long dateInMillis) {
            // If the date is today, return the localized version of "Today" instead of the actual
            // day name.
            Time time1 = new Time();
            time1.setToNow();
            int julianDay = Time.getJulianDay(dateInMillis, time1.gmtoff);
            int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), time1.gmtoff);
            if (julianDay == currentJulianDay) {
                return context.getString(R.string.today);
            } else if (julianDay == currentJulianDay + 1) {
                return context.getString(R.string.tomorrow);
            } else if (julianDay == currentJulianDay - 1) {
                return context.getString(R.string.yesterday);
            } else {
                Time time2 = new Time();
                time2.setToNow();
                // Otherwise, the format is just the day of the week (e.g "Wednesday").
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                return simpleDateFormat.format(dateInMillis);
            }
        }
    }
}
