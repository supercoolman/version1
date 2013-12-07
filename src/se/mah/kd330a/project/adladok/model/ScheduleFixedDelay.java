package se.mah.kd330a.project.adladok.model;

import se.mah.kd330a.project.schedule.data.KronoxCalendar;
import se.mah.kd330a.project.schedule.data.KronoxReader;
import android.content.Context;
import android.util.Log;

public class ScheduleFixedDelay implements Runnable  {
	
	private Context c;
    private String TAG ="ScheduleFixedDelay";
	public ScheduleFixedDelay(Context c){
		this.c = c;
	}
	@Override
	public void run() {
		try
		{
			KronoxReader.update(c.getApplicationContext());
			KronoxCalendar.createCalendar(KronoxReader.getFile(c.getApplicationContext()));
			//Here we should have a listener pattern
			Log.i(TAG, "Updated Calendar");
		}
		catch (Exception f)
		{
			Log.e(TAG, f.toString());
		}
	 }
		
}

