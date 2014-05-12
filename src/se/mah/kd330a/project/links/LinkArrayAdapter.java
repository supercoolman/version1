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
    private final String[] linkTitle;
    private final String[] linkSubTitle;
    private int pos;

    public LinkArrayAdapter(Activity context, int resource, String[] linkTitle, String[] linkSubTitle, int pos) {
        super(context, resource, linkTitle);
        this.context = context;
        this.linkTitle = linkTitle;
        this.linkSubTitle = linkSubTitle;
        this.pos = pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        
        LayoutInflater inflater = context.getLayoutInflater();
        row = inflater.inflate(R.layout.link_item, null);
        
        TextView textView_1 = (TextView) row.findViewById(R.id.list_text_view_1);
        textView_1.setText(linkTitle[position]);
        TextView textView_2 = (TextView) row.findViewById(R.id.list_text_view_2);
        if(pos == 0) {
            textView_2.setText(linkSubTitle[position]);
        } else {
            textView_2.setText(" ");
        }
        return row;
    }
}
