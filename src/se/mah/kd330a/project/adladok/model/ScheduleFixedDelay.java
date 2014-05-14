package se.mah.kd330a.project.adladok.model;

//A change is needed to improve the batterlife.


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;

import se.mah.kd330a.project.adladok.xmlparser.Parser;
import se.mah.kd330a.project.home.data.DOMParser;
import se.mah.kd330a.project.home.data.RSSFeed;
import se.mah.kd330a.project.schedule.data.KronoxCalendar;
import se.mah.kd330a.project.schedule.data.KronoxReader;
import android.content.Context;
import android.util.Log;

public class ScheduleFixedDelay extends Observable implements Runnable  {
	
	private int ADUpdateCount=10; 
	public enum UpdateType {
		   KRONOX, 
		   COURSES_and_AD,
		   MAHNEWS,
		   ALL
		}
	private Context c;
    private String TAG ="ScheduleFixedDelay";
    
	private final static String 	COURSES_FILENAME = "courses_ical";
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static Calendar calendar = Calendar.getInstance();
    
	public ScheduleFixedDelay(Context c){
		this.c = c;
	}
	
	
	@Override
	public void run() {
		File iCal = new File(c.getFilesDir(), COURSES_FILENAME);
		String lastMod = dateFormat.format(new Date(iCal.lastModified()));
		String todayDate = dateFormat.format(new Date(calendar.getTimeInMillis()));	

		Log.i("Updater","Updated");

		

			String[] files = c.fileList();
			for (String s : files) {
				Log.d(TAG,"Filename: "+s);
			}
			//MAHNEWS writes MAHnews to file and notifies listeners
			try{
				DOMParser myParser = new DOMParser();
				RSSFeed feedFromNet = myParser.parseXml(Constants.mahNewsAdress);
				//check if changed
				File file = new File(c.getFilesDir(), Constants.mahNewsSavedFileName);
				if (file.exists()){
					FileInputStream fis = c.openFileInput(Constants.mahNewsSavedFileName);
					ObjectInputStream in = new ObjectInputStream(fis); 
					RSSFeed savedFeed = (RSSFeed) in.readObject();
					in.close();
					fis.close();
					
					if(!savedFeed.getItem(0).getTitle().equalsIgnoreCase(feedFromNet.getItem(0).getTitle())){
						FileOutputStream fout = c.openFileOutput(Constants.mahNewsSavedFileName, Context.MODE_PRIVATE);
						ObjectOutputStream out = new ObjectOutputStream(fout);
						out.writeObject(feedFromNet);
						out.close();
						fout.close();
						setChanged();
						notifyObservers(UpdateType.MAHNEWS);
						Log.i(TAG,"MAHNEWS Updates news and found new item in news feed ");
					}else{
						Log.i(TAG,"MAHNEWS Nothing to update already uptodate ");
					}
				}else{
					FileOutputStream fout = c.openFileOutput(Constants.mahNewsSavedFileName, Context.MODE_PRIVATE);
					ObjectOutputStream out = new ObjectOutputStream(fout);
					out.writeObject(feedFromNet);
					out.close();
					fout.close();
					setChanged();
					notifyObservers(UpdateType.MAHNEWS);
					Log.i(TAG,"MAHNEWS No file saved locally saving new file");
				}
			}catch(Exception e1){
				Log.i(TAG,e1.toString());
			
			}
			// updates from AD and LADOK notifies listeners
			if (ADUpdateCount >=10){
				ADUpdateCount=0;
				Log.d(TAG,"Starting AD update");
	        	try{
	        		String userInfoAsXML = Me.getInstance().getUserInfoAsXML(Me.getInstance().getUserID(),Me.getInstance().getPassword());
	        		if(!userInfoAsXML.isEmpty()&&Parser.updateMeFromADandLADOK(userInfoAsXML,c)){
	        			Log.i(TAG,"UserUpdate succesfull saving to local storage");
	        			Me.getInstance().saveMeToLocalStorage(c);
	        			setChanged();
	        			notifyObservers(UpdateType.COURSES_and_AD);
	        		}
	        	}catch(Exception e){
	        		Log.e(TAG,"AD-LADOK Parser update exception");
	        	}
			}else{
				ADUpdateCount++;
			}
			
			//Kronox reads from Kronox with information from registered courses
			try
			{
				KronoxReader.update(c.getApplicationContext());
				KronoxCalendar.createCalendar(KronoxReader.getFile(c.getApplicationContext()));
				setChanged();
				notifyObservers(UpdateType.KRONOX);
				Log.i(TAG, "Updated Calendar from Kronox");
			}catch (Exception f){
					Log.i(TAG, "Kronox: "+f.toString());
			 }
			
			//ITSL
			//????
			
   }
 }

