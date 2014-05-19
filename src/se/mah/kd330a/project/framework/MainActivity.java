package se.mah.kd330a.project.framework;

import se.mah.kd330a.project.R;
import se.mah.kd330a.project.StartActivity;
import se.mah.kd330a.project.adladok.model.Constants;
import se.mah.kd330a.project.adladok.model.Me;
import se.mah.kd330a.project.find.FragmentFind;
import se.mah.kd330a.project.help.FragmentCredits;
import se.mah.kd330a.project.home.FragmentHome;
import se.mah.kd330a.project.home.data.RSSFeed;
import se.mah.kd330a.project.itsl.FragmentITSL;
import se.mah.kd330a.project.links.LinksParentFragment;
import se.mah.kd330a.project.profile.view.FragmentProfile;
import se.mah.kd330a.project.profile.view.SettingsActivity;
import se.mah.kd330a.project.schedule.view.FragmentScheduleWeekPager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {

	/**
	 * Activity that starts after the StartActivity has finished logging in. 
	 * Holds the "main" app.
	 */

	public static DrawerLayout 			mDrawerLayout;
	public static ListView 				mDrawerList;
	public ActionBarDrawerToggle 	mDrawerToggle;
	private Fragment				mFragment;
	private CharSequence 			mDrawerTitle;
	private CharSequence 			mTitle;
	private String[] 				mMenuTitles;
	private TypedArray 				mMenuIcons;
	private TypedArray 				mMenuColors;
	public RSSFeed 					mNewsFeed;
	private final String 			TAG = MainActivity.class.getName();
    public static final int 		HOME = 0;
	public static final int 		SCHEDULE = 1;
	public static final int 		PROFILE = 2;
	public static final int 		ITSL = 3;
	public static final int 		FIND = 4;
	public static final int 		LINKS = 5;
	public static final int 		HELP = 6;
	public static final int 		LOGOUT = 7;
	private int 					refreshCheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();
		mMenuTitles = getResources().getStringArray(R.array.menu_texts);
		mMenuIcons = getResources().obtainTypedArray(R.array.menu_icons);
		mMenuColors = getResources().obtainTypedArray(R.array.menu_colors);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		mDrawerList.setSelector(R.drawable.menu_selector);

		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new MenuAdapter(this,
				R.layout.drawer_list_item, mMenuTitles, mMenuIcons, mMenuColors));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description for accessibility */
				R.string.drawer_close  /* "close drawer" description for accessibility */
				) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);


		/** 
		 * The fragment to start in.
		 */
		if (savedInstanceState == null) {
			selectItem(0);
		}
	}
	
	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
			Log.d("Drawer", String.valueOf(position));
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG,"OnPause");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG,"OnResume");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch(item.getItemId()) {
		case R.id.action_help:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void selectItem(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		switch (position) {
		case HOME:	
			mFragment = new FragmentHome();
			transaction.addToBackStack(null);
			Log.d("Drawer", "Home");
			break;
		case SCHEDULE:
			mFragment = new FragmentScheduleWeekPager();
			transaction.addToBackStack(null);
			Log.d("Drawer", "Schedule");
			break;
		case ITSL:
			mFragment = new FragmentITSL();
			transaction.addToBackStack(null);
			Log.d("Drawer", "ITSL");
			break;
		case FIND:
			mFragment = new FragmentFind();
			transaction.addToBackStack(null);
			Log.d("Drawer", "Find");
			break;
		case PROFILE:
			mFragment = new FragmentProfile();
			transaction.addToBackStack(null);
			Log.d("Drawer", "Profile");
			break;
		case LINKS:
			mFragment = new LinksParentFragment();
			transaction.addToBackStack(null);
			Log.d("Drawer", "Links");
			break;
		case HELP:
			mFragment = new FragmentCredits();
			transaction.addToBackStack(null);
			Log.d("Drawer", "Credits");
			break;
		case LOGOUT:
			mFragment = new FragmentLogout();
			Log.d("Drawer", "Logout");
			logout();
			break;
		default:	
			mFragment = new FragmentHome();
			Log.d("Drawer", "Default");
		}
		transaction.replace(R.id.content_frame, mFragment);
		transaction.commit();
		refreshCheck = position;
		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mMenuTitles[position]);
	}

	/**
	 * Method for refreshing the currently viewed fragment. 
	 * Use with 
	 * Me.getInstance().startRefresher(FragmentCallback fragmentCallback, Context context) {}
	 * to refresh current fragment.
	 */
	public void refreshCurrent(){
		Fragment fragment = null;
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		switch (refreshCheck) {
		case HOME:	
			fragment = new FragmentHome();
			break;
		case SCHEDULE:
			fragment = new FragmentScheduleWeekPager();
			break;
		case ITSL:
			fragment = new FragmentITSL();
			break;
		case FIND:
			fragment = new FragmentFind();
			break;
		case PROFILE:
			fragment = new FragmentProfile();
			break;
		case LINKS:
			fragment = new LinksParentFragment();
			break;
		case HELP:
			fragment = new FragmentCredits();
			break;
		}
		transaction.replace(R.id.content_frame, fragment, "FRAGMENT");
		transaction.commit();
		Log.i("Refresh","Refreshed");

	}
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}


	public void toNewsFeedOnWeb(View view) {
		Uri uri = Uri.parse(Constants.URL_NEWS_FEED);
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW,
				uri);
		startActivity(launchBrowser);
	}
	
	public void toSchedule(View view) {
		selectItem(this.SCHEDULE);
	}	

	public void toITSL(View view) {
		selectItem(this.ITSL);
	}

	public void toFind(View view) {
		selectItem(this.FIND);
	}
	
    public void logout(){
        Me.getInstance().clearAllIncludingSavedData(this);
        Intent intent = new Intent(this, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        }
}

