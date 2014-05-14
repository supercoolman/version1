package se.mah.kd330a.project.links;

import se.mah.kd330a.project.R;
import android.R.anim;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class LinksArrayAdapter extends ArrayAdapter<String> {
    
    private final Activity context;
    private final String[] linkTitle;

    public LinksArrayAdapter(Activity context, int resource, String[] linkTitle) {
        super(context, resource, linkTitle);
        this.context = context;
        this.linkTitle = linkTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        
        LayoutInflater inflater = context.getLayoutInflater();
        row = inflater.inflate(R.layout.link_list_item, null);
        
        TextView textView_1 = (TextView) row.findViewById(R.id.list_text_view_1);
        textView_1.setText(linkTitle[position]);
        
        ImageButton imageButton = (ImageButton) row.findViewById(R.id.image_button);
        imageButton.setImageResource(R.drawable.ic_action_call);
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(context, "making call", Toast.LENGTH_SHORT);
            }
        });
        return row;
    }
}
