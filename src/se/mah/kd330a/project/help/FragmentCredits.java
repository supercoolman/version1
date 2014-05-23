package se.mah.kd330a.project.help;

import se.mah.kd330a.project.R;
import se.mah.kd330a.project.framework.MainActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

// Detta �r about this app fragmentet.
// Vi beh�ver d�pa om paketet och l�gga till IDK13 och versions nummer. 

public class FragmentCredits extends Fragment {

	/** Called when the activity is first created. */
	Scroller myscroll = null;
	TextView tvData = null;
	TextView credtisTv;
	TextView whenMadeTv;
	boolean tread = true;

	public FragmentCredits() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		tread = true;
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_help, container, false);
		tvData = (TextView) rootView.findViewById(R.id.textview);
		credtisTv = (TextView) rootView.findViewById(R.id.loading);
		whenMadeTv = (TextView) rootView.findViewById(R.id.list_course_building_id);
		myscroll = new Scroller(getActivity(), new LinearInterpolator());
		tvData.setScroller(myscroll);
		Scroll();
		
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					while (tread) {
						Log.i("App", "Thread Run");
						sleep(1000);
						getActivity().runOnUiThread(compScroll);
						tread=false;
					}
				} catch (InterruptedException e) {
					tread=false;
					e.printStackTrace();
				}
			}
		};

		thread.start();
		return rootView;
	}
	
	public void onDestroyView() {
		tread=false;
		super.onDestroyView();
	}

	private Runnable compScroll = new Runnable() {
		@Override
		public void run() {
			if (false == myscroll.computeScrollOffset()) {
				Log.i("App", "Scroll Again");
				Scroll();
			}
		}
	};
	
	// H�r �r text funktionen, vi ska �ndra i arrayen,
	// --> idTeanS
	// --> gdTeamS
	
	public void Scroll() {
		String s = "";
		Resources res = getResources();
		
		//Loop the credits
		for (int f = 0; f < 20; f++){
			String[] idk = res.getStringArray(R.array.idk13);

			for (int i = 0; i < idk.length; i++) {
				s = s + "\n" + idk[i];
			}
			
			s = s + "\n";
			String[] idTeamS = res.getStringArray(R.array.Idteam);

			for (int i = 0; i < idTeamS.length; i++) {
				s = s + "\n" + idTeamS[i];
			}

			s = s + "\n";
			String[] gdTeamS = res.getStringArray(R.array.gdteam);

			for (int i = 0; i < gdTeamS.length; i++) {
				s = s + "\n" + gdTeamS[i];
			}

			s = s + "\n";
			String[] techTeamS = res.getStringArray(R.array.techteam);

			for (int i = 0; i < techTeamS.length; i++) {
				s = s + "\n" + techTeamS[i];
			}
		
			s = s + "\n";
			String[] utgivare = res.getStringArray(R.array.utgivare);

			for (int i = 0; i < utgivare.length; i++) {
				s = s + "\n" + utgivare[i];
			}
			s = s + "\n";
		
		}
		
		tvData.setTextSize(14);
		tvData.setGravity(Gravity.CENTER);
		tvData.setText(s);
		// int length = tvData.getLineCount();
		//
		// View v = findViewById(R.id.scroll_container);
		
		//Change duration of scroll. Speed and duration needs to calibrated  (0, -500, speed, duration)
		myscroll.startScroll(0, -500, 0, 20000, 80000);
	}
}