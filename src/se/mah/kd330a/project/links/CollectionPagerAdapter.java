package se.mah.kd330a.project.links;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CollectionPagerAdapter extends FragmentStatePagerAdapter {

	List<String[]> textArrayList;
	String[] linkOptions;
	Bundle args;

	public CollectionPagerAdapter(FragmentManager fm, List<String[]> textArrayList, String[] linkOptions) {
		super(fm);
		this.textArrayList = textArrayList;
		this.linkOptions = linkOptions;
	}
	
	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new LinksListFragment();
		
		// Bundle some stuff to use in fragments
		args = new Bundle();
		args.putStringArray("TEXT_ARRAY", textArrayList.get(i));

		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return 6;
	}

	@Override
	public CharSequence getPageTitle(int position) {
	    return linkOptions[position];
	}
}
