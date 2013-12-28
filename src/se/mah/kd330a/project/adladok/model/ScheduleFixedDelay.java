package se.mah.kd330a.project.adladok.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Observable;

import se.mah.kd330a.project.adladok.xmlparser.Parser;
import se.mah.kd330a.project.home.data.DOMParser;
import se.mah.kd330a.project.home.data.RSSFeed;
import se.mah.kd330a.project.schedule.data.KronoxCalendar;
import se.mah.kd330a.project.schedule.data.KronoxReader;
import android.content.Context;
import android.util.Log;

public class ScheduleFixedDelay extends Observable implements Runnable  {
	
	public final static long initalDelayInSeconds = 1;
	public final static long delayBetweenUpdatesInSeconds = 300; //5  minutes then we take AD every 30 minutes
	//public final static long delayBetweenUpdatesInSeconds = 30; //30sec for testing
	private int ADUpdateCount=10; 
	public enum UpdateType {
		   KRONOX, 
		   COURSES_and_AD,
		   MAHNEWS,
		   ALL
		}
	private Context c;
    private String TAG ="ScheduleFixedDelay";
    
	public ScheduleFixedDelay(Context c){
		this.c = c;
	}
	
	
	@Override
	public void run() {
			String[] files = c.fileList();
			for (String s : files) {
				Log.d(TAG,"Filename: "+s);
			}
			try
			{
				KronoxReader.update(c.getApplicationContext());
				KronoxCalendar.createCalendar(KronoxReader.getFile(c.getApplicationContext()));
				setChanged();
				notifyObservers(UpdateType.KRONOX);
				Log.i(TAG, "Updated Calendar from Kronox");
			}catch (Exception f){
					Log.i(TAG, f.toString());
			 }
			//AD
			if (ADUpdateCount >=10){
				ADUpdateCount=0;
				Log.d(TAG,"Starting AD update");
	        	try{
	        		String userInfoAsXML = Me.getInstance().getUserInfoAsXML(Me.getInstance().getUserID(),Me.getInstance().getPassword());
	        		if(Parser.updateMeFromADandLADOK(userInfoAsXML)){
	        			Log.i(TAG,"UserUpdate succesfull saving to local storage");
	        			Me.getInstance().setColors(c);
	        			Me.getInstance().saveMeToLocalStorage(c);
	        			setChanged();
	        			notifyObservers(UpdateType.COURSES_and_AD);
	        		}
	        	}catch(Exception e){
	        		Log.e(TAG,"LADOK Parser update exception");
	        	}
			}else{
				ADUpdateCount++;
			}
			//MAHNEWS writes news to a file
			
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
			}catch(FileNotFoundException e1){
				Log.i(TAG,e1.toString());
			
			}
			catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}catch(Exception e){
				Log.e(TAG, e.toString());
		    }
			//ITSL
			//????
			
		}
 }

