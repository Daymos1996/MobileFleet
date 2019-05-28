package courses;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import chat.EmployeesFragment;
import chat.ListMessageFromEmployessFragment;

public class CoursesSectionsPagerAdapter extends FragmentPagerAdapter {
    public CoursesSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                TodayCoursesFragment todayCoursesFragment = new TodayCoursesFragment();
                return todayCoursesFragment;

            case 1:
                HistoryCoursesFragment historyCoursesFragment = new HistoryCoursesFragment();
                return historyCoursesFragment;

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 2;
    }
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Today Courses";

            case 1:
                return "History";

            default:
                return null;
        }
    }

}
