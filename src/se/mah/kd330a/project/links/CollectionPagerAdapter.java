package se.mah.kd330a.project.links;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CollectionPagerAdapter extends FragmentStatePagerAdapter {
    
	Bundle args;

	public CollectionPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new ChildFragment();
		args = new Bundle();
		args.putInt("POSITION", i);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return ParentFragment.mainList.list.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
	    String title = ParentFragment.mainList.list.get(position).title;
	        return title;
	}
}
