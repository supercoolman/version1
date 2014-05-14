package se.mah.kd330a.project.settings.view;

import java.util.ArrayList;

import se.mah.kd330a.project.R;
import se.mah.kd330a.project.adladok.model.Course;
import se.mah.kd330a.project.adladok.model.Me;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

@SuppressLint("ResourceAsColor")
public class FragmentProfile extends android.support.v4.app.Fragment  {
	SharedPreferences sharedPref;
	private String profilName;
	private String profilId;
	private ArrayList<Course> myCourses;
	private TextView profilNameTextView;
	TextView profilIdTextView;
	TextView currentCourse;
	LinearLayout coursesContent;
	
	public FragmentProfile(){
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater
				.inflate(R.layout.activity_settings, container, false);
			
		
		return rootView;
	}
	
	
}
