
package se.mah.kd330a.project.links;

import java.util.ArrayList;
import java.util.List;

import se.mah.kd330a.project.R;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentLinks extends Fragment {
	
	public static ViewPager viewPager;
	PagerTabStrip pagerTabStrip;
	Context context;
	
	// This is used to pass data to pageradapter and later to child fragments
	private final List<String[]> mTitleArrayList = new ArrayList<String[]>();
	public static TypedArray images;
    
	// Fetches the stringarrays
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		images = context.getResources().obtainTypedArray(R.array.images_links);
	    mTitleArrayList.add(getResources().getStringArray(R.array.links_list_options));
		mTitleArrayList.add(getResources().getStringArray(R.array.student_at_mah));
		mTitleArrayList.add(getResources().getStringArray(R.array.it));
		mTitleArrayList.add(getResources().getStringArray(R.array.library));
		mTitleArrayList.add(getResources().getStringArray(R.array.housing));
		mTitleArrayList.add(getResources().getStringArray(R.array.career_guide));
		mTitleArrayList.add(getResources().getStringArray(R.array.social_media));
	}
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    		View v = inflater.inflate(R.layout.links_fragment, container, false);
    		viewPager = (ViewPager) v.findViewById(R.id.pager);
    		viewPager.setAdapter(buildAdapter());
    		pagerTabStrip = (PagerTabStrip) v.findViewById(R.id.pager_tab_strip);
    		pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.red_mah));
            pagerTabStrip.setTextSpacing(1);
    		pagerTabStrip.setDrawFullUnderline(true);
    		
    	return v;
	}
    
    private PagerAdapter buildAdapter() {
        return(new CollectionPagerAdapter(getChildFragmentManager(), mTitleArrayList, context));
      } 
}