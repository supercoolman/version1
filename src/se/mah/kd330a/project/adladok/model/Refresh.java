package se.mah.kd330a.project.adladok.model;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import se.mah.kd330a.project.adladok.xmlparser.Parser;
import se.mah.kd330a.project.home.data.DOMParser;
import se.mah.kd330a.project.home.data.RSSFeed;
import se.mah.kd330a.project.schedule.data.KronoxCalendar;
import se.mah.kd330a.project.schedule.data.KronoxReader;
import se.mah.kd330a.project.schedule.view.FragmentScheduleWeek.FragmentCallback;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class Refresh extends AsyncTask<Void, Void, Void> {
	
	private Context mContext;
	private FragmentCallback mFragmentCallback;
	
	public  boolean toggle;
	
	public Refresh(FragmentCallback fragmentCallback) {
		this.mFragmentCallback = fragmentCallback;
	}
	

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Log.d("Async", "Prepare for download");
	}

	@Override
	protected Void doInBackground(Void... params) {
		
		/**
		 * Fetches user info by sending them to a web service.
		 * TODO: This is a huge security risk, since the users credentials are  sent in plaintext. Consider using OAuth or obfuscating the password.
		 */
		
		try{
    		String userInfoAsXML = Me.getInstance().getUserInfoAsXML(Me.getInstance().getUserID(),Me.getInstance().getPassword());
    		if(!userInfoAsXML.isEmpty()&&Parser.updateMeFromADandLADOK(userInfoAsXML, mContext)){
    			Log.i("Async","UserUpdate succesful. Saving to local storage...");
    			Me.getInstance().saveMeToLocalStorage(mContext);
    		}
    	}catch(Exception e){
    		Log.e("Async","AD/LADOK Parser update exception. :C");
    	}
		
		/**
		 * Updates the schedule
		 */
		
		try {
			KronoxReader.update(mContext.getApplicationContext());
			KronoxCalendar.createCalendar(KronoxReader.getFile(mContext.getApplicationContext()));
			Log.d("Async", "Downloading...");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		/**
		 * Update MAH News feed and parse to xml
		 */
		
		try {
			DOMParser domParser = new DOMParser();
			RSSFeed rssFeed = domParser.parseXml(Constants.MAH_NEWS_ADDRESS);
			FileOutputStream fout = mContext.openFileOutput(Constants.MAH_NEWS_SAVED_FILE_NAME, Context.MODE_PRIVATE);
			ObjectOutputStream out = new ObjectOutputStream(fout);
			out.writeObject(rssFeed);
			out.close();
			fout.close();
			Log.d("Async", "Saved MAH News");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		mFragmentCallback.onRefreshCompleted();
		Log.d("Async", "Done.");
	}

	
	

}
