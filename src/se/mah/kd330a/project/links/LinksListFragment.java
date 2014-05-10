package se.mah.kd330a.project.links;


import se.mah.kd330a.project.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class LinksListFragment extends Fragment {
    
    Listener listener = new Listener();

	ArrayAdapter<String> adapter;
	String[] textArray;
	int image;
	
	public static final String TEXT_ARRAY = "se.mah.kd330a.project.links.TEXT_ARRAY";
	public static final String IMAGE = "se.mah.kd330a.project.links.IMAGE";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.links_list_fragment, container, false);
		ListView listView = (ListView) v.findViewById(R.id.list_view);
		
		// Get arguments from FragmentStatePagerAdapter
		Bundle args = getArguments();
		image = args.getInt("IMAGE");
		textArray = args.getStringArray("TEXT_ARRAY");
		
		adapter = new LinkArrayAdapter(getActivity(), R.layout.link_item, textArray, image);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(listener);
		
		return v;
	}
	
	private class Listener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            
        }
	    
	}
	
}
