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
	private ScheduleFixedDelay scheduledTask;
	
	public static Me getInstance(){
		if (instanceOfMe==null){
			instanceOfMe = new Me();
		}
		return instanceOfMe;
	}
	
	private Me() {
		/*Singleton*/
	}		
	
	 public void startUpdate(Context ctx){
		 scheduledUpdater(ctx);
	 }

	 public Observable getObservable() {
			return scheduledTask;
	}
	
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
		 String[] files = c.fileList();
			for (String fileName : files) {
				File file = new File(c.getFilesDir(), fileName);
				if (file.exists()){
					file.delete();
					Log.i(TAG,"Deleted: "+fileName);
				}
		 }
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
	
	/**Restores Me and my courses from local storage*/
	public boolean restoreMeFromLocalStorage(Context c){
		//Read local storage
		boolean restored = true;
		File file = new File(c.getFilesDir(), ME_FILE_NAME);
		if (file.exists()){
			//Clear content in me:
			String xml = Parser.getXmlFromFile(file);
			Log.i(TAG,"Restored" + xml);
			 try {
				 Parser.updateMeFromADandLADOK(xml,c);
				 if(this.password.isEmpty()||this.userID.isEmpty()){
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
	
	public List<Course> getCourses(){
		return myCourses;
	}
	
	public Course getCourse(String courseID) {
		for(Course c: myCourses){  //overide equals instead.......
			if (c.getCourseID().equals(courseID)){
				return c;
			}
		}
		return null;
	}
	
	public void addCourse(Course course) {		
		this.myCourses.add(course);
	}
	 
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
	        	Log.i(TAG,"LoginError: "+e.getMessage());
	       }
	       return result.toString();
	}		
	 
	 /**Starts the updating from AD,LADOK and Kronox**/
	 private void scheduledUpdater(Context ctx){
		 scheduledTask = new ScheduleFixedDelay(ctx);
		 try {
			 scheduledTask.run();
			 Log.i(TAG, "UpdateSchedule started");
		 } catch (Exception e) {
				Log.e(TAG,"ScheduelError: "+e.getMessage());
			}
			
	}
}
