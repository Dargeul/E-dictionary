package com.example.gypc.e_dictionary;

import android.content.Context;
import android.support.annotation.IntegerRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by XUJIJUN on 2017/11/18.
 */

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    public ImageAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mThumIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView)convertView;
        }

        imageView.setImageResource(mThumIds[position]);
        return imageView;
    }

    public static final Integer[] mThumIds = {
            R.drawable.avatar0,
            R.drawable.avatar1,
            R.drawable.avatar2,
            R.drawable.avatar3,
            R.drawable.avatar4,
            R.drawable.avatar5,
            R.drawable.avatar6,
            R.drawable.avatar7,
            R.drawable.avatar8,
            R.drawable.avatar9,
            R.drawable.avatar10,
            R.drawable.avatar11
    };
}
