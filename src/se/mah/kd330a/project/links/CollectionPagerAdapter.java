package se.mah.kd330a.project.links;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CollectionPagerAdapter extends FragmentStatePagerAdapter {

	List<String[]>  mLinkArrays;
	Bundle 			args;

	public CollectionPagerAdapter(FragmentManager fm, List<String[]> linkArrays) {
		super(fm);
		this.mLinkArrays = linkArrays;
	}
	
	// Create fragments when swiping, destroy them dynamically
	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new LinksListFragment();
		
		// Bundle some stuff to use in fragments
		args = new Bundle();
		args.putStringArray("TEXT_ARRAY", mLinkArrays.get(i+1));
		args.putInt("POSITION", i);
		
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return 5;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mLinkArrays.get(0)[position];
	}
}
