package se.mah.kd330a.project.adladok.model;

import java.io.File;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlSerializer;

import se.mah.kd330a.project.R;
import se.mah.kd330a.project.adladok.xmlparser.Parser;
import se.mah.kd330a.project.schedule.data.KronoxReader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;


public class Me implements Serializable{
	private static final long serialVersionUID = 1L;
	//Static variables there is only one Me
	private static List<Course> myCourses = new ArrayList<Course>();
	private static List<Teacher> myTeachers = new ArrayList<Teacher>();
	private static String firstName="";
	private static String lastName="";
	private static String email="";
	private static String dispayName="";
	private static boolean isStaff = false;
	private static boolean isStudent = false;
    private static String TAG ="UserInfo";
	private static String userID="";
	private static String password="";
	private static final String SAVE_FILE_NAME = "savefilename";
	public static MyObservable observable = new MyObservable(); 


	public static void setPassword(String password) {
		Me.password = password;
	}
	
	public static String getPassword() {
		return Me.password;
	}
	
	public static String getUserID() {
		return userID;
	}
	public static void setUserID(String userID) {
		Me.userID = userID;
	}
	
	private Me() {
		//prevents anyone from doing instances
	}
	
	public static String getFirstName() {
		return firstName;
	}
	public static void setFirstName(String firstName) {
		Me.firstName = firstName;
	}
	public static String getLastName() {
		return lastName;
	}
	public static void setLastName(String lastName) {
		Me.lastName = lastName;
	}
	public static String getEmail() {
		return email;
	}
	public static void setEmail(String email) {
		Me.email = email;
	}
	public static String getDispayName() {
		return dispayName;
	}
	public static void setDispayName(String dispayName) {
		Me.dispayName = dispayName;
	}

	public static boolean isStaff() {
		return Me.isStaff;
	}
	public static void setIsStaff(boolean isStaff) {
		Me.isStaff = isStaff;
	}
	public static boolean isStudent() {
		return isStudent;
	}
	public static void setIsStudent(boolean isStudent) {
		Me.isStudent = isStudent;
	}
	
	public static String getTeacherName(String teacherID) {
		//search the teachers return name if found if not found call web service but else:
			//Not found call web service
			//Save in arraylist as teacher
			//return the name
		return teacherID;
	}
	
	public static void clearAllIncludingSavedData(Context c) {
		 clearAllExcludingSavedData(c);
		 //clear kronox
		 KronoxReader.clearKronox(c);
		 Log.i(TAG,"clear all including locally saved data");
		 saveMeToLocalStorage(c);
	}
	
	public static void clearAllExcludingSavedData(Context c) {
		// Lars fixa detta. Metoden ska ta bort alla spŒr av anvŠndaren
		Log.i(TAG,"clear all excluding saved data");
		 clearCourses();
		 //clearTeachers();
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
	public static boolean restoreMeFromLocalStorage(Context c){
		//Read local storage
		boolean restored = false;
		File file = new File(c.getFilesDir(), SAVE_FILE_NAME);
		if (file.exists()){
			//Clear content in me:
			String xml = Parser.getXmlFromFile(file);
			Log.i("UserInfo","Restored" + xml);
			 try {
				 restored = Parser.updateMeFromADandLADOK(xml);
				 if(Me.password.isEmpty()&&Me.userID.isEmpty()){
					 restored = false;
				 }
			} catch (Exception e) {restored =false;}
		}
		return restored;
	}
	
	/**Stores Me and my courses on local storage CourseName:
	 * Call this also when changes are made to Me or courses*/
	public static void saveMeToLocalStorage(Context c){
		String xml = Parser.writeXml();
		Log.i("UserInfo","Saved: " + xml);
		try {
			java.io.FileOutputStream fos = c.openFileOutput(SAVE_FILE_NAME, Context.MODE_PRIVATE);
			fos.write(xml.getBytes());
			fos.close();
		} catch (Exception e) {
			
		}
	}
	
	public static void updateMeFromWebService(){
		Log.i(TAG,"updateMe");
		doUpdate(userID, password);
	}
	
	public static void clearCourses(){
		if (myCourses.size()>0){
			Log.i(TAG,"clearing courses in memory");
			myCourses.clear();
		}
	}
	
	public static List<Course> getCourses(){
		return myCourses;
	}
	
	public static Course getCourse(String courseID) {
		for(Course c: myCourses){  //overide equals
			if (c.getCourseID().equals(courseID)){
				return c;
			}
		}
		return null;
	}
	
	@SuppressLint("ResourceAsColor")
	public static void addCourse(Course course) {
		//Set Colors on courses first needs a context so perhaps Singleton....
		Me.myCourses.add(course);
		
	}
//Part handling updateddd
	
    private static final String NAMESPACE = "http://mahapp.k3.mah.se/";
    private static final String URL = "http://195.178.234.7/mahapp/userinfo.asmx";
    private static AsyncTask<String, Void, Integer> asyncTask= null;
  //Only one update at a time
    private static void doUpdate(String userID, String password){
    	if(asyncTask!=null){
	    	if (asyncTask.getStatus()==AsyncTask.Status.FINISHED){
	    		Log.i(TAG,"Finished do again");
	    		asyncTask = new AsyncCallGetUserInfo().execute(userID,password);
	    	}else{
	    		Log.i(TAG,"Not finished");
	    	}
    	}else{
    		Log.i(TAG,"Asynctask do null");
    		asyncTask = new AsyncCallGetUserInfo().execute(userID,password);
    	}
    }
    
    private static class AsyncCallGetUserInfo extends AsyncTask<String, Void, Integer> {
	        @Override
	        protected Integer doInBackground(String... params) {
	        	int result = 0;
	        	Log.i(TAG,"Starting update");
	            //get the info from web service
	        	String userInfoAsXML = getUserInfoAsXML(params[0],params[1]);
	            //parse the XML it and update the class Me{
	        	try{
	        		if(Parser.updateMeFromADandLADOK(userInfoAsXML)){
	        			result = 1; //success
	        		}
	        		Log.i("UserInfo","Result of update 1 is good: "+result);
	        	}catch(Exception e){
	        		Log.e("UserInfo","Parser update exception");
	        	}
	            return result;
	        }
	        @Override
	        protected void onPostExecute(Integer result) {
	        	Log.i(TAG,"Update finished");
		        super.onPostExecute(result);
			    observable.setChanged();  //Tell that we made changes....
			    observable.notifyObservers(result);  // Notify all listeners...
		        Log.i(TAG,"Update finished listeners notified result: "+ result);
	        }
	    }
	 
	 public static String getUserInfoAsXML(String loginID, String password){	   
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
	 
	 ///--- for observer pattern
	 
	 public static class MyObservable extends Observable{  //Must be here to get hold on the protected setChanged
		 @Override
		protected void setChanged() {
			super.setChanged();
		}
	 }
}
