package se.mah.kd330a.project.schedule.view;

import java.util.ArrayList;

import se.mah.kd330a.project.R;
import se.mah.kd330a.project.schedule.data.ParseData;
import se.mah.kd330a.project.schedule.model.ScheduleWeek;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class FragmentScheduleWeekPager extends Fragment implements OnRefreshListener {

	private static ArrayList<ScheduleWeek> myScheduleInWeeks;
	private ParseData parseData;
	private static int numItems = 0;

	private SwipeRefreshLayout mSwipeRefreshLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		parseData = new ParseData();
		myScheduleInWeeks = parseData.getParsedDataFromKronoxByWeekNew(20);
		
		mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_container);
	    mSwipeRefreshLayout.setOnRefreshListener(this);
	    mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
	            android.R.color.holo_green_light,
	            android.R.color.holo_orange_light,
	            android.R.color.holo_red_light);
		
		if (myScheduleInWeeks != null) {
			numItems = myScheduleInWeeks.size();
			Log.i("onCreate in FragmentScheduleWeekPager",Integer.toString(numItems));
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("onCreateView", "loaded");
		View result = inflater.inflate(R.layout.view_pager_fragment, container,false);
		ViewPager pager = (ViewPager) result.findViewById(R.id.pager);
		ViewPagerAdapter viewPagerAdapter = buildAdapter();
		pager.setAdapter(viewPagerAdapter);
		pager.setCurrentItem(4);
		
		
		PagerTabStrip pagerTabStrip = (PagerTabStrip) result.findViewById(R.id.weeks_pager_tab_stip);
		pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.red_mah));	
		pagerTabStrip.setDrawFullUnderline(true);

		return (result);
	}

	private ViewPagerAdapter buildAdapter() {
		return (new ViewPagerAdapter(getChildFragmentManager()));
	}

	public static class ViewPagerAdapter extends FragmentPagerAdapter {
		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int num) {
			Log.i("getItem", Integer.toString(num));
			if(myScheduleInWeeks != null){
			return FragmentScheduleWeek.newInstance(myScheduleInWeeks.get(num),num);
			} else {
			return new FragmentScheduleDay();
			}
		}
		
		@Override
		public CharSequence getPageTitle(int number){
			String title = "Week "+myScheduleInWeeks.get(number).getWeekNumber();
			return title;
		}
		
		@Override
		public int getCount() {
			return numItems;
		}
	}

	@Override
	public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
		
	}
	

	
}
