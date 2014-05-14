package se.mah.kd330a.project.links;

import java.util.List;

import se.mah.kd330a.project.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CollectionPagerAdapter extends FragmentStatePagerAdapter {

	List<String[]> titleArrayList;
	Context context;
	String[] linkOptions;
	Bundle args;

	public CollectionPagerAdapter(FragmentManager fm, List<String[]> titleArrayList, Context context) {
		super(fm);
		this.titleArrayList = titleArrayList;
		this.context = context;
	}
	
	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new LinksListFragment();

		args = new Bundle();
		args.putStringArray("LINK_TITLE", titleArrayList.get(i));
		args.putStringArray("LINK_SUB_TITLE", context.getResources().getStringArray(R.array.links_list_sub_options));
		args.putInt("POSITION", i);

		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return 7;
	}

	@Override
	public CharSequence getPageTitle(int position) {
	    
	    linkOptions = context.getResources().getStringArray(R.array.links_options);

	   
	  	    return linkOptions[position];
	}
}
