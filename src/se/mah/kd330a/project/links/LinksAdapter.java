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

public class LinksAdapter extends ArrayAdapter<String> {
    
    Activity context;
    String[] linkTitle;
    int viewPagerPosition;

    public LinksAdapter(Activity context, int resource, String[] linkTitle) {
        super(context, resource, linkTitle);
        this.context = context;
        this.linkTitle = linkTitle;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        
        LayoutInflater inflater = context.getLayoutInflater();
        row = inflater.inflate(R.layout.link_list_item, null);
        viewPagerPosition = LinksChildFragment.position;
        if(viewPagerPosition == 1) {
            ImageButton imageButton = (ImageButton) row.findViewById(R.id.image_button);
            if(position == 0) {
                imageButton.setBackgroundResource(R.drawable.imagebutton_states);
                imageButton.setImageResource(R.drawable.ic_action_call);
            }
            imageButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Toast.makeText(context, "making call", Toast.LENGTH_SHORT).show();
                }
            });
        }
        TextView textView_1 = (TextView) row.findViewById(R.id.title);
        textView_1.setText(linkTitle[position]);
        return row;
    }
}
