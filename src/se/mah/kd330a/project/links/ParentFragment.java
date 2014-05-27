
package se.mah.kd330a.project.links;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import se.mah.kd330a.project.R;
import se.mah.kd330a.project.framework.MainActivity;
import se.mah.kd330a.project.links.MainList.LinkList.LinkObject;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

public class ParentFragment extends Fragment {
	
	public static ViewPager viewPager;
	static Activity context;
	PagerTabStrip pagerTabStrip;
	
	public static MainList mainList;
	public static ArrayList<Drawable> categoryIconList = new ArrayList<Drawable>();
	public static ArrayList<Drawable> socialMediaIconList = new ArrayList<Drawable>();
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    context = getActivity();
		super.onCreate(savedInstanceState);
        InputStream iStream = getResources().openRawResource(R.raw.linksatmah);
        Serializer serializer = new Persister();
        try {
            mainList  = serializer.read(MainList.class, iStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getCategoryIcons();
        getSocialMediaIcons();
	}
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    		View v = inflater.inflate(R.layout.links_viewpager, container, false);
    		viewPager = (ViewPager) v.findViewById(R.id.pager);
    		viewPager.setAdapter(buildAdapter());
    		try {
                Field mScroller;
                mScroller = ViewPager.class.getDeclaredField("mScroller");
                mScroller.setAccessible(true);
                DecelerateInterpolator sInterpolator = new DecelerateInterpolator(3);
                FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(), sInterpolator);
                mScroller.set(viewPager, scroller);
            } catch (Exception e) {
            }
    		pagerTabStrip = (PagerTabStrip) v.findViewById(R.id.pager_tab_strip);
    		pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.red_mah));
    		pagerTabStrip.setDrawFullUnderline(true);
    		
    		MainActivity.mDrawerLayout.closeDrawer(MainActivity.mDrawerList);
    	return v;
	}
    
    private PagerAdapter buildAdapter() {
        return(new CollectionPagerAdapter(getChildFragmentManager()));
      }
    
    private ArrayList<Drawable> getCategoryIcons () {
        for(int i = 0; i < mainList.list.size(); i ++) {
            if(mainList.list.get(i).title.equals("Categories")) {
                MainList.LinkList linkList = mainList.list.get(i);
                for(int q = 0; q < linkList.linkobjectlist.size(); q ++) {
                    String iconName = linkList.linkobjectlist.get(q).icon;
                    int iconResId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
                    Drawable icon = context.getResources().getDrawable(iconResId);
                    categoryIconList.add(q, icon);
                }
            }
        }
        return categoryIconList;
    }
    
    private ArrayList<Drawable> getSocialMediaIcons () {
        for(int i = 0; i < mainList.list.size(); i ++) {
            if(mainList.list.get(i).title.equals("Social media")) {
                MainList.LinkList linkList = mainList.list.get(i);
                for(int q = 0; q < linkList.linkobjectlist.size(); q++) {
                    String iconName = linkList.linkobjectlist.get(q).icon;
                    int iconResId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
                    Drawable icon = context.getResources().getDrawable(iconResId);
                    socialMediaIconList.add(q, icon);
                }
            }
        }
        return socialMediaIconList;
    }
}