
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
	
	// Initialize stuff
	ViewPager 			mViewPager;
	PagerTabStrip		mPagerTabStrip;
	
	// This is used to pass data to pageradapter and later to child fragments
	private final List<String[]> mLinkArrays = new ArrayList<String[]>();
	
	String[]		 	mLinksOptions;
	String[] 			mItSupportOptions;
	String[]			mHousingOptions;
	String[]			mStudentOptions;
	String[]			mCareerOptions;
	String[]			mNewStudentOptions;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mLinksOptions = getResources().getStringArray(R.array.links_options);
		mLinkArrays.add(mLinksOptions);
		
		mItSupportOptions = getResources().getStringArray(R.array.itSupportOptions);
		mLinkArrays.add(mItSupportOptions);
		
		mHousingOptions = getResources().getStringArray(R.array.housingOptions);
		mLinkArrays.add(mHousingOptions);
		
		mStudentOptions = getResources().getStringArray(R.array.studentOptions);
		mLinkArrays.add(mStudentOptions);
		
		mCareerOptions = getResources().getStringArray(R.array.careerOptions);
		mLinkArrays.add(mCareerOptions);
		
		mNewStudentOptions = getResources().getStringArray(R.array.newStudentOptions);
		mLinkArrays.add(mNewStudentOptions);
	}
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    		View v = inflater.inflate(R.layout.links_fragment, container, false);
    
    		mViewPager = (ViewPager) v.findViewById(R.id.pager);
    		mViewPager.setAdapter(buildAdapter());
    		
    		mPagerTabStrip = (PagerTabStrip) v.findViewById(R.id.pager_tab_strip);
    		mPagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.red_mah));
    		mPagerTabStrip.setDrawFullUnderline(true);
    		
    	return v;
	}
    
    private PagerAdapter buildAdapter() {
        return(new CollectionPagerAdapter(getChildFragmentManager(), mLinkArrays));
      } 
}