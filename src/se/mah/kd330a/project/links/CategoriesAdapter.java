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

public class CategoriesAdapter extends ArrayAdapter<String> {
    
    Activity context;
    String[] linkTitle;
    String[] linkSubTitle;
    TypedArray images;
    
    public CategoriesAdapter(Activity context, int resource, String[] linkTitle) {
        super(context, resource, linkTitle);
        this.context = context;
        this.linkTitle = linkTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        row = inflater.inflate(R.layout.link_category_item, null);
        
        TextView textView_1 = (TextView) row.findViewById(R.id.title);
        textView_1.setText(linkTitle[position]);
        
        linkSubTitle = LinksParentFragment.subTitles;
        TextView textView_2 = (TextView) row.findViewById(R.id.sub_title);
        textView_2.setText(linkSubTitle[position]);
        
        //loads the icons for each category from the images array
        images = LinksParentFragment.images;
        ImageView imageView = (ImageView) row.findViewById(R.id.category_image);
        imageView.setImageDrawable(images.getDrawable(position+1));
        return row;
    }
}
