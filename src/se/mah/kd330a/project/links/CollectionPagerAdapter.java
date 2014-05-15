package se.mah.kd330a.project.links;

import java.util.List;

import se.mah.kd330a.project.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

public class CollectionPagerAdapter extends FragmentStatePagerAdapter {

	List<String[]> titleArrayList;
	Context context;
	String[] linkOptions;
	TypedArray images;
	Drawable myDrawable;
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
		
		
		// fetch the image array
		images=context.getResources().obtainTypedArray(R.array.images_links);
	    
	    linkOptions = context.getResources().getStringArray(R.array.links_options);
	    SpannableStringBuilder sb = new SpannableStringBuilder(""+ linkOptions[position]);

	    myDrawable = images.getDrawable(position);
	    myDrawable.setBounds(0, 0, myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight()); 
	    ImageSpan span = new ImageSpan(myDrawable, ImageSpan.ALIGN_BASELINE); 
	    sb.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
	    
	  	    return sb;
	}
}
