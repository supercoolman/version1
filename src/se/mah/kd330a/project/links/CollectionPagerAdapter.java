package se.mah.kd330a.project.links;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CollectionPagerAdapter extends FragmentStatePagerAdapter {

	List<String[]> linkArrays;
	int[] linkImgs;
	Bundle args;

	public CollectionPagerAdapter(FragmentManager fm, List<String[]> linkArrays, int[] linkImgs) {
		super(fm);
		this.linkArrays = linkArrays;
		this.linkImgs = linkImgs;
	}
	
	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new LinksListFragment();
		
		// Bundle some stuff to use in fragments
		args = new Bundle();
		args.putStringArray("LINK_LIST", linkArrays.get(i));
		args.putInt("IMAGE", linkImgs[i]);
		
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return 5;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return linkArrays.get(0)[position];
	}
}
