package se.mah.kd330a.project;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import se.mah.kd330a.project.adladok.model.Me;
import se.mah.kd330a.project.framework.MainActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.EditText;

public class StartActivity extends Activity {
	private final String TAG = "StartActivity";
	private EditText editTextUsername;
	private EditText editTextPassword;
	boolean restoredsuccess = false;
	boolean loginOK =false;
	private String username;
	private String password;
	private static AsyncTask<String, Void, Integer> asyncLoginTask;
	private enum LOGINMESSAGE {SHOW,NOSHOW};
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		((LinearLayout) findViewById(R.id.login_view)).setVisibility(LinearLayout.GONE);
		((LinearLayout) findViewById(R.id.loading_view)).setVisibility(LinearLayout.GONE);		
		restoredsuccess = Me.getInstance().restoreMeFromLocalStorage(this);
		//check if password changed
		Log.i(TAG,"restoredsuccess: "+restoredsuccess);
		if(restoredsuccess){
			username = Me.getInstance().getUserID();
			password = Me.getInstance().getPassword();
			hideLoginView();
		}else{
			showLoginView(LOGINMESSAGE.NOSHOW);
		}
	}

	public void showLoginView(LOGINMESSAGE loginmessage)
	{
		switch(loginmessage){
		case SHOW:
			Toast.makeText(this, "Unable to log you in", Toast.LENGTH_LONG).show();
			break;
		case NOSHOW:
			break;
		default:
			break;
		}
		((View) findViewById(R.id.loading_view)).setVisibility(View.GONE);
		((View) findViewById(R.id.login_view)).setVisibility(View.VISIBLE);
		editTextUsername = (EditText) findViewById(R.id.editText1);
		editTextPassword = (EditText) findViewById(R.id.editText2);
		editTextUsername.setText(Me.getInstance().getUserID());
		// Automatic Login yo
		editTextUsername.setText("testUser1");
		editTextPassword.setText("testUser1");


	}

	public void hideLoginView()
	{
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow((IBinder) findViewById(R.id.login_view).getWindowToken(), 0);
		((View) findViewById(R.id.login_view)).setVisibility(View.GONE);
		((View) findViewById(R.id.loading_view)).setVisibility(View.VISIBLE);
		asyncLoginTask = new AsyncTaskLoginUser().execute(username,password,"hide"); 
	}

	public void loginButtonClicked(View v)
	{
		username = editTextUsername.getText().toString();
		password = editTextPassword.getText().toString();
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow((IBinder) findViewById(R.id.login_view).getWindowToken(), 0);
		((View) findViewById(R.id.login_view)).setVisibility(View.GONE);
		((View) findViewById(R.id.loading_view)).setVisibility(View.VISIBLE);
		asyncLoginTask = new AsyncTaskLoginUser().execute(username,password,"click");
	}
	
	/**
	 * AsyncTask handling login on another thread
	 */
	private class AsyncTaskLoginUser extends AsyncTask<String, Void, Integer> {
	    private static final String NAMESPACE = "http://mahapp.k3.mah.se/";
	    private static final String URL = "http://195.178.234.7/mahapp/userinfo.asmx";
	    
	    @Override
	    protected Integer doInBackground(String... params) {
	    	Integer result = 0; //error;
	    	Log.i(TAG,"Starting Login");
	        //get the info from web service
	    	try {
	        	SoapObject loginrequest = new SoapObject(NAMESPACE, "logInTheUser");
	            loginrequest.addProperty("username", params[0]);
	            loginrequest.addProperty("password", params[1]);
	            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	            envelope.dotNet=true;
	            envelope.setOutputSoapObject(loginrequest);
	            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	            androidHttpTransport.call(NAMESPACE+"logInTheUser", envelope);
	            String rawResult = ((Object)envelope.getResponse()).toString();
	            Log.d(TAG,"Loginresult "+rawResult);
	            boolean loggedIn = rawResult.equals("true")?true:false;
	            if (loggedIn&&params[2].equals("hide")){
	            	result = 1;
	            }else if  (loggedIn&&params[2].equals("click")){
	            	result = 2;
	            }
	            
	        } catch (Exception e) {
	        	Log.e(TAG,"LoginError: not logged in... "+e.getMessage());
	       }
	        return result;
	    }
	    
	    @Override
	    protected void onPostExecute(Integer result) {
	        super.onPostExecute(result);
	        Log.i(TAG,"Logged in with result 0:problem, 1:fromSavedData, 2:fromScreen::: "+result);
	        switch(result){
	        	case 1: //hide
	        		tasksCompleted();
	        		break;
	        	case 2: //click
	        		Me.getInstance().setUserID(username);
	        		Me.getInstance().setPassword(password);
	    			tasksCompleted();
	        		break;
	        	default:
	        		showLoginView(LOGINMESSAGE.SHOW); 			
	        		break;
	        }
	    }
	}

	/**
	 * When all tasks have completed we can go on to the MainActivity
	 */
	public void tasksCompleted()
	{
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Log.i(TAG, "finish(): destroying StartActivity now");
	}
}
