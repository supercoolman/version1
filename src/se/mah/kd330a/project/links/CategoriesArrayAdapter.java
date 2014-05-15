package se.mah.kd330a.project.links;

import se.mah.kd330a.project.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoriesArrayAdapter extends ArrayAdapter<String> {
    
    Activity context;
    String[] linkTitle;
    String[] linkSubTitle;
    TypedArray images;
    
    public CategoriesArrayAdapter(Activity context, int resource, String[] linkTitle, String[] linkSubTitle) {
        super(context, resource, linkTitle);
        this.context = context;
        this.linkTitle = linkTitle;
        this.linkSubTitle = linkSubTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        
        LayoutInflater inflater = context.getLayoutInflater();
        row = inflater.inflate(R.layout.link_category_item, null);
        
        TextView textView_1 = (TextView) row.findViewById(R.id.list_text_view_1);
        textView_1.setText(linkTitle[position]);
        TextView textView_2 = (TextView) row.findViewById(R.id.list_text_view_2);
        textView_2.setText(linkSubTitle[position]);
        
        //loads the icons for each category from the images array
        ImageView imageView = (ImageView) row.findViewById(R.id.category_image);
        images = FragmentLinks.images;
        imageView.setImageDrawable(images.getDrawable(position+1));
        return row;
    }
}
