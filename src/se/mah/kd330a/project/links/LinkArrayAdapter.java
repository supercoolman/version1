package se.mah.kd330a.project.links;

import se.mah.kd330a.project.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LinkArrayAdapter extends ArrayAdapter<String> {
    
    private final Activity context;
    private final String[] linkList;

    public LinkArrayAdapter(Activity context, int resource, String[] linkList) {
        super(context, resource, linkList);
        this.context = context;
        this.linkList = linkList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        
        LayoutInflater inflater = context.getLayoutInflater();
        row = inflater.inflate(R.layout.link_item, null);
        
        TextView textView_1 = (TextView) row.findViewById(R.id.list_text_view_1);
        textView_1.setText(linkList[position]);
        TextView textView_2 = (TextView) row.findViewById(R.id.list_text_view_2);
        textView_2.setText(linkList[position]);
        
        return row;
    }
}
