package org.milal.wheeliric;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ekgml on 2017-08-14.
 */

public class ReviewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<ReviewItems> data;
    private int layout;
    public ReviewAdapter(Context context, int layout, ArrayList<ReviewItems> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }
    @Override
    public int getCount(){return data.size();}

    @Override
    public long getItemId(int position){return position;}

    @Override
    public String getItem(int position){return data.get(position).getNick();}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        ReviewItems reviewitems=data.get(position);

        ImageView photo=(ImageView)convertView.findViewById(R.id.photoView);
        photo.setImageResource(reviewitems.getPhotoURL());

        TextView nick=(TextView)convertView.findViewById(R.id.nickView);
        nick.setText(reviewitems.getNick());
        return convertView;
    }
}
