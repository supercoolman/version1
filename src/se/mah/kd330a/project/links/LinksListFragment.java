package se.mah.kd330a.project.links;


import se.mah.kd330a.project.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LinksListFragment extends Fragment {

	ArrayAdapter<String> adapter;
	String[] linkList;
	int image;
	
	public static final String LINK_LIST = "se.mah.kd330a.project.links.LINK_LIST";
	public static final String IMAGE = "se.mah.kd330a.project.links.IMAGE";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.links_list_fragment, container, false);
		ListView listView = (ListView) v.findViewById(R.id.list_view);
		
		// Get arguments from FragmentStatePagerAdapter
		Bundle args = getArguments();
		image = args.getInt("IMAGE");
		linkList = args.getStringArray("LINK_LIST");
		
		adapter = new LinkArrayAdapter(getActivity(), R.layout.link_item, linkList, image);
		listView.setAdapter(adapter);
		
		return v;
	}
}
