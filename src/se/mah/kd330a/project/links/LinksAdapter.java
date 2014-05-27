package se.mah.kd330a.project.links;

import java.util.List;

import se.mah.kd330a.project.R;
import se.mah.kd330a.project.R.id;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LinksAdapter extends ArrayAdapter<MainList.LinkList.LinkObject> {
    
    Activity context;
    List<MainList.LinkList.LinkObject> list;
    final int viewPagerPosition = ChildFragment.position;
    int TYPE;
    
    public LinksAdapter(Activity context, int resource, List<MainList.LinkList.LinkObject> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
    }
    
    static class ViewHolder {
        TextView title;
        TextView categoryTitle;
        TextView categorySubtitle;
        ImageView categoryIcon;
        ImageButton phone;
    }
    
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    
    @Override
    public int getItemViewType(int position) {
        if(list.get(position).telnr != null) {
            TYPE = 0; // row has telnr
        } else {
            TYPE = 1;
        }
        return TYPE;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if(row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.link_list_item, null); 
            if(viewPagerPosition == 0) {
                row.setBackgroundResource(R.drawable.links_states);
            }
            holder = new ViewHolder();
            holder.title = (TextView) row.findViewById(R.id.link_title);
            holder.categoryTitle = (TextView) row.findViewById(R.id.category_title);
            holder.categorySubtitle = (TextView) row.findViewById(R.id.category_subtitle);
            holder.categoryIcon = (ImageView) row.findViewById(R.id.category_icon);
            holder.phone = (ImageButton) row.findViewById(R.id.phone);
            row.setTag(holder);
        }
        
        holder = (ViewHolder) row.getTag();
        String title = list.get(position).title;
        
        if(viewPagerPosition == 0) {
            holder.categoryTitle.setText(title);
            holder.categorySubtitle.setText(list.get(position).subtitle);
            holder.phone.setVisibility(View.GONE);
            holder.categoryIcon.setImageDrawable(ParentFragment.categoryIconList.get(position));
        } else {
            holder.title.setText(title);
            if(TYPE == 0) {
                holder.phone.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        String phone = list.get(position).telnr;
                        startDialActivity(phone);
                    }
                });
            } else {
                holder.phone.setVisibility(View.GONE);
            }
            holder.title.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    String url = list.get(position).url;
                    openWebSite(url);
                }
            });
        }
        
        
        return row;
    }
    
    private void startDialActivity(String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phone));
        context.startActivity(intent);
    }
    
    private void openWebSite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }
}
