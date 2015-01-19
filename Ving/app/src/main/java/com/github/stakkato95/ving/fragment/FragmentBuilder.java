package com.github.stakkato95.ving.fragment;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.github.stakkato95.ving.adapter.ZCursorAdapter;
import com.github.stakkato95.ving.processor.DatabaseProcessor;

/**
 * Created by Artyom on 19.01.2015.
 */
//TODO удалить нафик
public class FragmentBuilder {

    private ZCursorAdapter mZCursorAdapter;
    private DatabaseProcessor mDatabaseProcessor;
    private String mRequestUrl;
    private Uri mContentType;
    private String[] mProjection;
    private String[] mProjectionOffline;

    public FragmentBuilder setAdapter(@NonNull ZCursorAdapter adapter) {
        mZCursorAdapter = adapter;
        return this;
    }

    public FragmentBuilder setProcessor(@NonNull DatabaseProcessor processor) {
        mDatabaseProcessor = processor;
        return this;
    }

    public FragmentBuilder setRequestUrl(@NonNull String url) {
        mRequestUrl = url;
        return this;
    }

    public FragmentBuilder setContentType(@NonNull Uri contentType) {
        mContentType = contentType;
        return this;
    }

    public FragmentBuilder setProjection(@NonNull String[] projection) {
        mProjection = projection;
        return this;
    }

    public FragmentBuilder setProjectionOffline(@NonNull String[] projectionOffline) {
        mProjectionOffline = projectionOffline;
        return this;
    }

    public ZListFragment createFragment() {
        return ZListFragment.newInstance(mZCursorAdapter, mDatabaseProcessor, mRequestUrl, mContentType, mProjection, mProjectionOffline);
    }

}
