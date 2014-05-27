package se.mah.kd330a.project.home;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import se.mah.kd330a.project.R;
import se.mah.kd330a.project.adladok.model.Constants;
import se.mah.kd330a.project.adladok.model.Course;
import se.mah.kd330a.project.adladok.model.Me;
import se.mah.kd330a.project.framework.MainActivity;
import se.mah.kd330a.project.home.data.RSSFeed;
import se.mah.kd330a.project.itsl.Article;
import se.mah.kd330a.project.itsl.FeedManager;
import se.mah.kd330a.project.schedule.data.KronoxCalendar;
import se.mah.kd330a.project.schedule.data.KronoxReader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class FragmentHome extends Fragment implements FeedManager.FeedManagerDoneListener
{

	private NextClassWidget nextClass;
	private ViewGroup rootView;
	private RSSFeed newsFeed;
	private ObjectInputStream in = null;
	private FileInputStream fis = null;
	private boolean profileRegistered = false;
	private FeedManager ITSLfeedManager;
	private String TAG ="FragmentHome";
	
	public FragmentHome() {
		/*
		 * Empty constructor
		 */
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.i("FragmentHome", "OnCreate: ");
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Log.i("FragmentHome", "OnCreateView: ");
		
		
        try {
			KronoxCalendar.createCalendar(KronoxReader.getFile(getActivity().getApplicationContext()));
		} catch (Exception e) {
			Log.e("FragmentHome", e.toString());
		} 
		try {
			nextClass = new NextClassWidget();
			profileRegistered = nextClass.anyClassesToday();
		}
		catch (Exception e) {
			Log.e("FragmentHome", "OnCreate: "+e.toString());
		} 
		
		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_home, container, false);
		setNextKronoxClass(rootView);
		setNewsFeedMah(rootView);
		ITSLfeedManager = new FeedManager(this, getActivity().getApplicationContext());
		//ITSLfeedManager.getFeedList().size()
		Log.i(TAG,"ITSLfeedManager.getFeedList().size()" + ITSLfeedManager.getFeedList().size());
	    if (!ITSLfeedManager.loadCache()) {
			ITSLfeedManager.reset();
			ITSLfeedManager.processFeeds();
		}
	    
	    MainActivity.mDrawerLayout.closeDrawer(MainActivity.mDrawerList);

		return rootView;
	}

	private void setNewsFeedMah(ViewGroup rootView) {
		Log.i(TAG,"setNewsFeedMah: ");
		try {
			fis = getActivity().openFileInput(Constants.MAH_NEWS_SAVED_FILE_NAME);
			in = new ObjectInputStream(fis);
			newsFeed = (RSSFeed) in.readObject();
			in.close();
			fis.close();
			Log.i(TAG, "Items in MAHNews feed: "+ Integer.toString(newsFeed.getItemCount()));
		}
		catch (Exception ex) {
			Log.e(TAG, "Error in get method");
		}

		try {
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			LinearLayout newsFeedMahWidget = (LinearLayout) rootView.findViewById(R.id.news_feed_widget);
			for (int i = 0; i < 1; i++)
			{
				TextView title = (TextView) newsFeedMahWidget.findViewById(R.id.text_latest_news_heading);
				title.setText(newsFeed.getItem(i).getTitle());
				TextView description = (TextView) newsFeedMahWidget.findViewById(R.id.text_latest_news_description);
				description.setText(newsFeed.getItem(i).getDescription());

			}
		}
		catch (Exception ex) {
			Log.e(TAG, "Error in get method");
		}

	}

	private void setNextKronoxClass(ViewGroup rootView)
	{
		
		LinearLayout nextClassWidget = (LinearLayout) rootView.findViewById(R.id.next_class_widget);
		nextClassWidget.setVisibility(LinearLayout.VISIBLE);
		String courseName = nextClass.getCourseName();
		String courseID = nextClass.getCourseId();
		
		if (profileRegistered)
		{
			TextView textNextClassName = (TextView) nextClassWidget.findViewById(R.id.text_next_class_name);
			textNextClassName.setText(courseName);
			TextView textNextClassDate = (TextView) nextClassWidget.findViewById(R.id.text_next_class_date);
			textNextClassDate.setText(nextClass.getDate());
			TextView textNextClassStartTime = (TextView) nextClassWidget.findViewById(R.id.text_next_class_start_time);
			textNextClassStartTime.setText(nextClass.getStartTime());
			TextView textNextClassEndTime = (TextView) nextClassWidget.findViewById(R.id.text_next_class_end_time);
			textNextClassEndTime.setText(nextClass.getEndTime());
			TextView textNextClassLocation = (TextView) nextClassWidget.findViewById(R.id.text_next_class_location);
			textNextClassLocation.setText(nextClass.getLocation());			
		
			View scheduleColor1 = (View) nextClassWidget.findViewById(R.id.home_schedule1);
			View scheduleColor2 = (View) nextClassWidget.findViewById(R.id.home_schedule2);
			if (Me.getInstance().getCourse(courseID)!= null){
				scheduleColor1.setBackgroundColor(Me.getInstance().getCourse(courseID).getColor());
				scheduleColor2.setBackgroundColor(Me.getInstance().getCourse(courseID).getColor());
			}else{
				scheduleColor1.setBackgroundColor(getResources().getColor(R.color.red_mah));
				scheduleColor2.setBackgroundColor(getResources().getColor(R.color.red_mah));
			}
		}
		else
		{
			//nextClassWidget.setVisibility(LinearLayout.GONE);
			//TextView textNextClassDate = (TextView) nextClassWidget.findViewById(R.id.text_next_class_date);
			//textNextClassDate.setText("No classes found updating....");
		}

	}

//ITSL
	@Override
	public void onFeedManagerDone(FeedManager fm, ArrayList<Article> articles)
	{
		try
		{
			//View widget = (View)rootView.findViewById(R.id.itslearning_widget);
			View widget = rootView;
			Article a = articles.get(0);
			int start = a.getArticleCourseCode().indexOf(" - ");
			String courseName="";
			try{
				courseName = a.getArticleCourseCode().substring(start+3,a.getArticleCourseCode().length()-1);
			}catch  (Exception e){
				Log.i(TAG,e.getMessage());
			}
			Log.i(TAG,"CourseName:"+ courseName+":");
			int color = this.getResources().getColor(R.color.red_mah);
			for (Course c : Me.getInstance().getCourses())
			{
				Log.i(TAG,"course::"+c.getDisplaynameSv()+ "::AcourseNAME::"+courseName);
				if (c.getDisplaynameSv().contains(courseName)||c.getDisplaynameEn().contains(courseName)){
					Log.i(TAG," Color" + c.getColor()+ "course"+c.getDisplaynameSv()+ " Artcode "+a.getArticleCourseCode());
					color=c.getColor();
					break;
				}
			}	
			((TextView)widget.findViewById(R.id.text_itsl_title)).setText(a.getArticleHeader());
			((TextView)widget.findViewById(R.id.text_itsl_date)).setText(a.getArticleDate());
			((TextView)widget.findViewById(R.id.text_itsl_content)).setText(a.getArticleText());
			((View)widget.findViewById(R.id.home_itsl1)).setBackgroundColor(color);
			((View)widget.findViewById(R.id.home_itsl2)).setBackgroundColor(color);
		}
		catch(Exception e)
		{
			Log.e(TAG, "onFeedManagerDone(): " + e.toString());
		}
	}

	@Override
	public void onFeedManagerProgress(FeedManager fm, int progress, int max)
	{
		// TODO Auto-generated method stub

	}
	
}
