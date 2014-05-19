package se.mah.kd330a.project.links;

import se.mah.kd330a.project.R;
import se.mah.kd330a.project.StartActivity;
import android.R.anim;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
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
    String phone = "0709704828";

    public LinksAdapter(Activity context, int resource, String[] linkTitle) {
        super(context, resource, linkTitle);
        this.context = context;
        this.linkTitle = linkTitle;
    }
    
    
    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        row = inflater.inflate(R.layout.link_list_item, null);
        viewPagerPosition = LinksParentFragment.viewPager.getCurrentItem();
        
        ImageButton imageButton = (ImageButton) row.findViewById(R.id.image_button);
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startDialActivity(phone);
            }
        });
        
        TextView textView_1 = (TextView) row.findViewById(R.id.title);
        textView_1.setText(linkTitle[pos]);
        return row;
    }
    
    private void startDialActivity(String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phone));
        context.startActivity(intent);
    }
}
