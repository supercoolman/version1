
package se.mah.kd330a.project.links;

import java.util.ArrayList;
import java.util.List;

import se.mah.kd330a.project.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentLinks extends Fragment {
	
	ViewPager viewPager;
	PagerTabStrip pagerTabStrip;
	
	// This is used to pass data to pageradapter and later to child fragments
	private final List<String[]> mLinkArrays = new ArrayList<String[]>();
    private final int[] mLinkImgs = new int[]{R.drawable.itsupport_lager, R.drawable.housing, R.drawable.studentservice,
                R.drawable.careerservice, R.drawable.newstudent };
    
    //Gets the information from the string arrays
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLinkArrays.add(getResources().getStringArray(R.array.links_options));
		mLinkArrays.add(getResources().getStringArray(R.array.itSupportOptions));
		mLinkArrays.add(getResources().getStringArray(R.array.housingOptions));
		mLinkArrays.add(getResources().getStringArray(R.array.studentOptions));
		mLinkArrays.add(getResources().getStringArray(R.array.careerOptions));
		mLinkArrays.add(getResources().getStringArray(R.array.newStudentOptions));
	}
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    		View v = inflater.inflate(R.layout.links_fragment, container, false);
    
    		viewPager = (ViewPager) v.findViewById(R.id.pager);
    		viewPager.setAdapter(buildAdapter());
    		
    		pagerTabStrip = (PagerTabStrip) v.findViewById(R.id.pager_tab_strip);
    		pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.red_mah));
    		pagerTabStrip.setDrawFullUnderline(true);
    		
    	return v;
	}
    
    private PagerAdapter buildAdapter() {
        return(new CollectionPagerAdapter(getChildFragmentManager(), mLinkArrays, mLinkImgs));
      } 
}