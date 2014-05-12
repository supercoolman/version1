package se.mah.kd330a.project.links;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CollectionPagerAdapter extends FragmentStatePagerAdapter {

	List<String[]> titleArrayList;
	List<String[]> subTitleArrayList;
	String[] linkOptions;
	Bundle args;

	public CollectionPagerAdapter(FragmentManager fm, List<String[]> titleArrayList, String[] linkOptions, List<String[]> subTitleArrayList) {
		super(fm);
		this.titleArrayList = titleArrayList;
		this.linkOptions = linkOptions;
		this.subTitleArrayList = subTitleArrayList;
	}
	
	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new LinksListFragment();
		
		// Bundle some stuff to use in fragments
		args = new Bundle();
		args.putStringArray("LINK_TITLE", titleArrayList.get(i));
		args.putStringArray("LINK_SUB_TITLE", subTitleArrayList.get(0));
		args.putInt("POSITION", i);

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
