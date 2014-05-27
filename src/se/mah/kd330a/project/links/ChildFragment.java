package se.mah.kd330a.project.links;

import java.util.List;

import se.mah.kd330a.project.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class ChildFragment extends Fragment {

    ArrayAdapter<MainList.LinkList.LinkObject> linksAdapter;
    Listener listener = new Listener();
    List<MainList.LinkList.LinkObject> list;

    public static final String POSITION = "se.mah.kd330a.project.links.POSITION";
    static int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Bundle args = getArguments();
        position = args.getInt("POSITION");
        View v = inflater.inflate(R.layout.links_listview, container, false);
        ListView listView = (ListView) v.findViewById(R.id.list_view);
        linksAdapter = new LinksAdapter(getActivity(), R.layout.link_list_item, getList(position));
        listView.setAdapter(linksAdapter);
        if(position == 0) {
            listView.setOnItemClickListener(listener);
        }
        return v;
    }
    
    private class Listener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            ParentFragment.viewPager.setCurrentItem(arg2+1, false);
        }
    }
    
    private List<MainList.LinkList.LinkObject> getList(int position) {
        return ParentFragment.mainList.list.get(position).linkobjectlist;
    }
}
