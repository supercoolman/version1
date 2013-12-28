package se.mah.kd330a.project.adladok.model;

import java.io.File;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.fortuna.ical4j.model.parameter.ScheduleStatus;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlSerializer;

import se.mah.kd330a.project.R;
import se.mah.kd330a.project.adladok.xmlparser.Parser;
import se.mah.kd330a.project.schedule.data.KronoxCalendar;
import se.mah.kd330a.project.schedule.data.KronoxReader;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;


public class Me{
	private static final long serialVersionUID = 1L;
	//Static variables there is only one Me
	private List<Course> myCourses = new ArrayList<Course>();
	private List<Teacher> myTeachers = new ArrayList<Teacher>();
	private String firstName="";
	private String lastName="";
	private String email="";
	private String dispayName="";
	private boolean isStaff = false;
	private boolean isStudent = false;
    private String TAG ="MeClass";
	private String userID="";
	private String password="";
	private final String ME_FILE_NAME = "MEfile";
	private static Me instanceOfMe;
	private static AsyncTask<String, Void, Integer> asyncLoginTask;
	//private MyObservable observable = new MyObservable(); 
	private ScheduledExecutorService executor_;

//	private final ScheduledThreadPoolExecutor executor_ ;//updater thread
	private ScheduleFixedDelay scheduledTask;
	
	public static Me getInstance(){
		if (instanceOfMe==null){
			instanceOfMe = new Me();
		}
		return instanceOfMe;
	}
	
	private Me() {
//		
//		if (scheduler != null){
//			scheduler = Executors.newSingleThreadScheduledExecutor();
//		}
//		if (executor_ != null){
//			executor_ = new ScheduledThreadPoolExecutor(1);
//		}
//			executor_.setKeepAliveTime(ScheduleFixedDelay.delayBetweenUpdatesInSeconds+1, TimeUnit.SECONDS);
//			executor_.allowCoreThreadTimeOut(true); 
		//Locale.setDefault(Locale.US); Kanske kanske inte
		/*Singleton*/
	}		
	
	 public void startUpdate(Context ctx){
		 setColors(ctx);
		 scheduledUpdater(ctx);
//		 try
//			{
//				Log.i(TAG, "Kronox: Creating calendar from file if saved");
//				KronoxCalendar.createCalendar(KronoxReader.getFile(ctx));
//			}
//			catch (Exception e)
//			{
//				Log.i(TAG, "Kronox: No calendar file saved");
//			}
	 }
	
	 public void stopUpdate(){
		 this.executor_.shutdown();
		 while (!executor_.isTerminated()) {
	        }
		 Log.i(TAG, "ThreadPoolExecutor: Finished all threads");
	        System.out.println("Finished all threads");
		 
	 }

	 public Observable getObservable() {
			return scheduledTask;
	}
//	public MyObservable getObservable() {
//		return observable;
//	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getUserID() {
		return this.userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return this.lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDispayName() {
		return this.dispayName;
	}
	public void setDispayName(String dispayName) {
		this.dispayName = dispayName;
	}

	public boolean isStaff() {
		return this.isStaff;
	}
	public void setIsStaff(boolean isStaff) {
		this.isStaff = isStaff;
	}
	public boolean isStudent() {
		return this.isStudent;
	}
	public void setIsStudent(boolean isStudent) {
		this.isStudent = isStudent;
	}
	
	public String getTeacherName(String teacherID) {
		//search the teachers return name if found if not found call web service but else:
			//Not found call web service
			//Save in arraylist as teacher
			//return the name
		return teacherID;
	}
	
	public void clearAllIncludingSavedData(Context c) {
		 clearAllExcludingSavedData(c);
		 KronoxReader.clearKronox(c);
		 saveMeToLocalStorage(c);
		 Log.i(TAG,"clear all including locally saved data");
	}
	
	public void clearAllExcludingSavedData(Context c) {
		Log.i(TAG,"clear all excluding saved data");
		 clearCourses();
		 firstName="";
		 lastName="";
		 email="";
		 dispayName="";
		 isStaff = false;
		 isStudent = false;
		 userID="";
		 password="";
	}
	
	/**Restores Me and my courses from local storage, use with care since it first clears all data in Me object
	 * Use saveMe first*/
	public boolean restoreMeFromLocalStorage(Context c){
		//Read local storage
		boolean restored = false;
		File file = new File(c.getFilesDir(), ME_FILE_NAME);
		if (file.exists()){
			//Clear content in me:
			String xml = Parser.getXmlFromFile(file);
			Log.i(TAG,"Restored" + xml);
			 try {
				 restored = Parser.updateMeFromADandLADOK(xml);
				 if(this.password.isEmpty()&&this.userID.isEmpty()){
					 restored = false;
				 }
			} catch (Exception e) {restored =false;}
		}
		return restored;
	}
	
	/**Stores Me and my courses on local storage CourseName:
	 * Call this also when changes are made to Me or courses*/
	public void saveMeToLocalStorage(Context c){
		String xml = Parser.writeXml();
		Log.d(TAG,"Saved: " + xml);
		try {
			java.io.FileOutputStream fos = c.openFileOutput(ME_FILE_NAME, Context.MODE_PRIVATE);
			fos.write(xml.getBytes());
			fos.close();
		} catch (Exception e) {
			
		}
	}
	
	
	
	public void clearCourses(){
		if (myCourses.size()>0){
			Log.i(TAG,"clearing courses in memory");
			myCourses.clear();
		}
	}
	
	public void setColors(Context ctx){
		int i=0;
			for (Course c : Me.getInstance().getCourses()) {
				switch (i) {
				case 0:
					c.setColor(ctx.getResources().getColor(R.color.grey_dark));
					break;
				case 1:
					c.setColor(ctx.getResources().getColor(R.color.blue));								
					break;
				case 2:
					c.setColor(ctx.getResources().getColor(R.color.green));
					break;
				case 3:
					c.setColor(ctx.getResources().getColor(R.color.yellow));
					break;
				case 4:
					c.setColor(ctx.getResources().getColor(R.color.orange));
					break;
				case 5:
					c.setColor(ctx.getResources().getColor(R.color.red));
					break;
				case 6:
					c.setColor(ctx.getResources().getColor(R.color.grey));
					break;
				case 7:
					c.setColor(ctx.getResources().getColor(R.color.grey_middle));
					break;
				default:
					break;			
				}
				//Log.d(TAG,"Color: " + c.getColor() + " Set to: " + c.getCourseID());
				i++;
			}
	}
	
	public List<Course> getCourses(){
		return myCourses;
	}
	
	public Course getCourse(String courseID) {
		for(Course c: myCourses){  //overide equals
			if (c.getCourseID().equals(courseID)){
				return c;
			}
		}
		return null;
	}
	
	public void addCourse(Course course) {		
		this.myCourses.add(course);
	}
	
//	public void updateMeFromWebService(){
//		Log.i(TAG,"updateMe");
//		doUpdate(userID, password);
//	}
	
  
//    private static AsyncTask<String, Void, Integer> asyncTask= null;
//  //Only one update at a time
//    //This can partly be moved to ScheduleFixedDelay
//    private void doUpdate(String userID, String password){
//    	if(asyncTask!=null){
//	    	if (asyncTask.getStatus()==AsyncTask.Status.FINISHED){
//	    		Log.i(TAG,"Finished do again");
//	    		asyncTask = new AsyncCallGetUserInfo().execute(userID,password);
//	    	}else{
//	    		Log.i(TAG,"Not finished");
//	    	}
//    	}else{
//    		Log.i(TAG,"Asynctask do null");
//    		asyncTask = new AsyncCallGetUserInfo().execute(userID,password);
//    	}
//    }
//    
//    private class AsyncCallGetUserInfo extends AsyncTask<String, Void, Integer> {
//	        @Override
//	        protected Integer doInBackground(String... params) {
//	        	int result = 0;
//	        	Log.i(TAG,"Starting update");
//	            //get the info from web service
//	        	String userInfoAsXML = getUserInfoAsXML(params[0],params[1]);
//	            //parse the XML it and update the class Me{
//	        	try{
//	        		if(Parser.updateMeFromADandLADOK(userInfoAsXML)){
//	        			result = 1; //success
//	        		}
//	        		Log.i("UserInfo","Result of update 1 is good: "+result);
//	        	}catch(Exception e){
//	        		Log.e("UserInfo","Parser update exception");
//	        	}
//	            return result;
//	        }
//	        @Override
//	        protected void onPostExecute(Integer result) {
//	        	Log.i(TAG,"Update finished");
//		        super.onPostExecute(result);
////			    observable.setChanged();  //Tell that we made changes....
////			    observable.notifyObservers(result);  // Notify all listeners...
//		        Log.i(TAG,"Update finished listeners notified result: "+ result);
//	        }
//	    }
    
	
	 
    private static final String NAMESPACE = "http://mahapp.k3.mah.se/";
    private static final String URL = "http://195.178.234.7/mahapp/userinfo.asmx";
    public String getUserInfoAsXML(String loginID, String password){	   
	     Object result="";
		 try {
	        	SoapObject loginrequest = new SoapObject(NAMESPACE, "getUserInfo");
	            loginrequest.addProperty("username", loginID);
	            loginrequest.addProperty("password", password);
	            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	            envelope.dotNet=true;
	            envelope.setOutputSoapObject(loginrequest);
	            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	            androidHttpTransport.call(NAMESPACE+"getUserInfo", envelope);
	            result = (Object)envelope.getResponse();
	        } catch (Exception e) {
	        	//Log.i(TAG,"LoginError: "+e.getMessage());
	       }
	        return result.toString();
	}
	 

    
  
//    private class AsyncCallLoginUser extends AsyncTask<String, Void, Integer> {
//        @Override
//        protected Integer doInBackground(String... params) {
//        	Object result = 0;
//        	Log.i(TAG,"Starting Login");
//            //get the info from web service
//        	try {
//	        	SoapObject loginrequest = new SoapObject(NAMESPACE, "logInTheUser");
//	            loginrequest.addProperty("username", params[0]);
//	            loginrequest.addProperty("password", params[1]);
//	            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//	            envelope.dotNet=true;
//	            envelope.setOutputSoapObject(loginrequest);
//	            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
//	            androidHttpTransport.call(NAMESPACE+"logInTheUser", envelope);
//	            result = (Object)envelope.getResponse();
//	            Log.d(TAG,"Loginresult "+result.toString());
//	        } catch (Exception e) {
//	        	Log.e(TAG,"LoginError: not logged in... "+e.getMessage());
//	       }
//        	Boolean loggedIn = loginToAD(params[0],params[1]);
//            //parse the XML it and update the class Me{
//            return 2;
//        }
//        
//        @Override
//        protected void onPostExecute(Integer result) {
//        	Log.i(TAG,"Update finished");
//	        super.onPostExecute(result);
//        }
//    }
    
//	 public class MyObservable extends Observable{  //Must be here to get hold on the protected setChanged
//		 @Override
//		protected void setChanged() {
//			super.setChanged();
//		}
//	 }
	 
	 /*updater metod*/
	 private void scheduledUpdater(Context ctx){
		 scheduledTask = new ScheduleFixedDelay(ctx);
		 executor_ = Executors.newSingleThreadScheduledExecutor();
		 try {
				 this.executor_.scheduleWithFixedDelay(scheduledTask, ScheduleFixedDelay.initalDelayInSeconds, ScheduleFixedDelay.delayBetweenUpdatesInSeconds, TimeUnit.SECONDS);//FOR test only 30
				 Log.i(TAG, "UpdateSchedule started");
		 } catch (Exception e) {
				Log.e(TAG,"ScheduelError: "+e.getMessage());
			}
			
	}
}
