package com.github.stakkato95.ving.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;

import com.github.stakkato95.imageloader.ImageLoader;

/**
 * Created by Artyom on 18.01.2015.
 */
public abstract class ZBaseAdapter extends CursorAdapter {

    private final LayoutInflater mLayoutInflater;
    private final ImageLoader mImageLoader;
    protected static final int DEFAULT_IMAGE_RESOURCE = -1;

    public ZBaseAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageLoader = ImageLoader.get(context);
    }

    protected LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    protected ImageLoader getmImageLoader() {
        return mImageLoader;
    }

}