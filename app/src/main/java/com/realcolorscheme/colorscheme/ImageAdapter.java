package com.realcolorscheme.colorscheme;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zliaky on 2017/4/15.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Uri[] uris;
    private List<Bitmap> mBitmapList = new ArrayList<Bitmap>();

    public int getCount() {
        return mBitmapList.size();
//        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public ImageAdapter(Context context, List<Bitmap> bitmapList) {
        mContext = context;
        mBitmapList = bitmapList;
    }

    public void setBitmapList(List<Bitmap> bitmapList) {
//        mBitmapList.clear();
        mBitmapList = bitmapList;
    }

    public int getBitmapListSize() {
        return mBitmapList.size();
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(288, 288));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

//        imageView.setImageResource(mThumbIds[position]);

        imageView.setImageBitmap(mBitmapList.get(position));
        return imageView;

    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7
    };
}