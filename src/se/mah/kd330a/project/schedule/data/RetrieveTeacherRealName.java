package se.mah.kd330a.project.schedule.data;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import se.mah.kd330a.project.adladok.model.Constants;
import android.os.AsyncTask;
import android.util.Log;

public class RetrieveTeacherRealName extends AsyncTask<String, Integer, String> {
	
	
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		Log.d("RetrieveTeacherRealName", "Starting to fetch Teacher Name");
		
	}

	@Override
	protected String doInBackground(String... id) {
		Log.d("RetrieveTeacherRealName", "Downloading Teacher Name");
		Object objectResult="";
		try {
			SoapObject getTeacherId = new SoapObject(Constants.NAMESPACE, "getTeacherName");
			getTeacherId.addProperty("id", id[0]);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet=true;
			envelope.setOutputSoapObject(getTeacherId);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URL);
			androidHttpTransport.call(Constants.NAMESPACE + "getTeacherName", envelope);
			objectResult = (Object)envelope.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return objectResult.toString();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Log.d("RetrieveTeacherRealName", "Done");
	}

	
	
}
