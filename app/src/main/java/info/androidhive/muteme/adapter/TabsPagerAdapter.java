package info.androidhive.muteme.adapter;


import info.androidhive.muteme.EventsActivity;
import info.androidhive.muteme.LocationActivity;
import info.androidhive.muteme.ProfileActivity;
import info.androidhive.muteme.RecentProfileActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:

			return new LocationActivity();
		case 1:

			return new EventsActivity();
		case 2:

			return new ProfileActivity();
		case 3:

			return new RecentProfileActivity();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}
	public CharSequence getPageTitle(int i){

		if(i == 0){return "Location";}
		if(i == 1){return  "Event";}
		if(i == 2){return  "Profile";}
		else
			return "Recent";

	}

}
