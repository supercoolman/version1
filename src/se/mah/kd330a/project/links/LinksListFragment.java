package se.mah.kd330a.project.links;


import se.mah.kd330a.project.R;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class LinksListFragment extends Fragment {

	ArrayAdapter<String> mAdapter;
	String[]			 mListArray;
	int 		 	     mPosition;
	TypedArray			 mLinkImgs;
	
	public static final String POSITION = "se.mah.kd330a.project.links.POSITION";
	public static final String TEXT_ARRAY = "se.mah.kd330a.project.links.TEXT_ARRAY";

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLinkImgs = getResources().obtainTypedArray(R.array.link_drawables);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.links_list_fragment, container, false);
		ListView listView = (ListView) v.findViewById(R.id.list_view);
		ImageView imageView = (ImageView) v.findViewById(R.id.list_image);
		
		// Get arguments from FragmentStatePagerAdapter
		Bundle args = getArguments();
		mListArray = args.getStringArray("TEXT_ARRAY");
		mPosition = args.getInt("POSITION");
		
		mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mListArray);
		listView.setAdapter(mAdapter);
		imageView.setImageDrawable(mLinkImgs.getDrawable(mPosition));
		
		return v;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mLinkImgs.recycle();
	}
}
