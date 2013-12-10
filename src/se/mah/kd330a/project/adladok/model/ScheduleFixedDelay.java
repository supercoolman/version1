package se.mah.kd330a.project.adladok.model;

import java.util.Observable;

import se.mah.kd330a.project.adladok.model.Me.MyObservable;
import se.mah.kd330a.project.schedule.data.KronoxCalendar;
import se.mah.kd330a.project.schedule.data.KronoxReader;
import android.content.Context;
import android.util.Log;

public class ScheduleFixedDelay extends Observable implements Runnable  {
	
	public final static long initalDelayInSeconds = 20;
	public final static long delayBetweenCallInSeconds = 300; //5 minutes
	public enum UpdateType {
		   KRONOX,
		   COURSES_and_AD,
		   TEACHER,
		   ALL
		}
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
			setChanged();
			notifyObservers(UpdateType.KRONOX);
			Log.i(TAG, "Updated Calendar");
		}
		catch (Exception f)
		{
			Log.e(TAG, f.toString());
		}
	 }
}

