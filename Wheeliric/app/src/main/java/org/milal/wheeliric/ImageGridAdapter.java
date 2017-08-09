package org.milal.wheeliric;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by Yoojung on 2017-08-03.
 */

public class ImageGridAdapter extends BaseAdapter{

    Context context = null;
    //String[] description = null;
    Bitmap[] bitmaps = null;
    String[] urls = null;

    public ImageGridAdapter(Context context, Bitmap[] bitmaps, String[] urls){
        this.context = context;
        //this.description = description;
        this.bitmaps = bitmaps;
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return (bitmaps != null) ? bitmaps.length : 0;
    }

    @Override
    public Object getItem(int position) {
        return (bitmaps != null) ? bitmaps[position] : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {
            grid = inflater.inflate(R.layout.simple_image, null);
            /*
            TextView textView = (TextView) grid.findViewById(R.id.description);
            textView.setText(description[position]);*/

            Bitmap bitmap = bitmaps[position];
            bitmap = Bitmap.createScaledBitmap(bitmap, 320, 240, false);

            ImageView imageView = (ImageView) grid.findViewById(R.id.imageView);
            imageView.setAdjustViewBounds(true);
            imageView.setImageBitmap(bitmap);
            ImageClickListener imageClickListener = new ImageClickListener(context, bitmaps[position], urls[position]);
            imageView.setOnClickListener(imageClickListener);
        }
        else{
            grid = convertView;
        }

        return grid;
    }
}

class ImageClickListener implements View.OnClickListener{

    Context context = null;
    Bitmap bitmap = null;
    String url = null;

    public ImageClickListener(Context context, Bitmap bitmap, String url){
        this.context = context;
        this.bitmap = bitmap;
        this.url = url;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);

    }
}
