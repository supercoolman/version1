package se.mah.kd330a.project.adladok.model;

import java.net.URL;

import se.mah.kd330a.project.adladok.model.ScheduleFixedDelay.UpdateType;
import se.mah.kd330a.project.adladok.xmlparser.Parser;
import se.mah.kd330a.project.schedule.data.KronoxCalendar;
import se.mah.kd330a.project.schedule.data.KronoxReader;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class Refresh extends AsyncTask<Void, Void, Void> {
	
	private Context mContext;
	
	public Refresh(Context context) {
		this.mContext = context;
	}
	

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Log.d("Async", "Prepare for download");
	}

	@Override
	protected Void doInBackground(Void... params) {
		
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
			Log.d("Async", e.toString());
		}
		
		
		
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		Log.d("Async", "Done.");
	}

	
	

}
