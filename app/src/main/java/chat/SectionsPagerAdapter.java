package chat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                ListMessageFromEmployessFragment listMessageFromEmployessFragment = new ListMessageFromEmployessFragment();
                return listMessageFromEmployessFragment;

            case 1:
                EmployeesFragment employeesFragment = new EmployeesFragment();
                return employeesFragment;






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
                return "Messages";

            case 1:
                return "Employees";



            default:
                return null;
        }
    }
}
