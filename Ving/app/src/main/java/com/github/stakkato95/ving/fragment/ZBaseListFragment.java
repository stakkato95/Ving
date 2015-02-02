package com.github.stakkato95.ving.fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.stakkato95.ving.CoreApplication;
import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.adapter.ZCursorAdapter;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.loader.DataLoader;
import com.github.stakkato95.ving.processor.DatabaseProcessor;
import com.github.stakkato95.ving.processor.Processor;
import com.github.stakkato95.ving.source.VkDataSource;

import java.net.UnknownHostException;

/**
 * Created by Artyom on 02.02.2015.
 */
public abstract class ZBaseListFragment extends ListFragment implements DataLoader.DatabaseCallback, LoaderManager.LoaderCallbacks<Cursor> {

    protected ListView mListView;
    protected ZCursorAdapter mZAdapter;
    protected TextView mErrorText;
    protected ProgressBar mProgressBar;
    protected View mFooder;

    private static final int CURSOR_LOADER = 0;
    private LoaderManager mLoaderManager;
    private ContentResolver mContentResolver;
    protected Uri mUri;

    private DataLoader mDataLoader;
    private DatabaseProcessor mProcessor;
    private VkDataSource mVkDataSource;
    protected String mRequestUrl;
    private int mRequestOffset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentResolver = getActivity().getContentResolver();
        mLoaderManager = getActivity().getSupportLoaderManager();
        mDataLoader = new DataLoader();
        mVkDataSource = VkDataSource.get(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(),container,false);
        whileOnCreateView(view);

        mListView = (ListView)view.findViewById(android.R.id.list);
        mZAdapter = getAdapter();
        mListView.setAdapter(mZAdapter);

        mErrorText = (TextView) view.findViewById(R.id.loading_error_text_view);
        mProgressBar = (ProgressBar) view.findViewById(android.R.id.progress);

        getFooder().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataLoader.getDataToDatabase(new DataLoader.DatabaseCallback() {

                    @Override
                    public void onLoadingFinished() {
                        mFooder.findViewById(android.R.id.progress).setVisibility(View.GONE);
                        mFooder.findViewById(android.R.id.text1).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingStarted() {
                        mFooder.findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
                        mFooder.findViewById(android.R.id.text1).setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingError(Exception e) {
                        getFooder().findViewById(android.R.id.progress).setVisibility(View.GONE);
                        getFooder().findViewById(android.R.id.text1).setVisibility(View.VISIBLE);
                        onError(e);
                    }
                }, getRequestUrl(mRequestOffset), mVkDataSource, mProcessor);
                mRequestOffset += Api.GET_COUNT;
            }
        });

        mUri = getContentUri();
        mRequestUrl = getRequestUrl();
        mProcessor = getProcessor();

        return view;
    }

    protected void loadData() {
        cleanDatabaseOut();
        mRequestOffset = 0;
        mDataLoader.getDataToDatabase(this, getRequestUrl(mRequestOffset), mVkDataSource, mProcessor);
        mRequestOffset += Api.GET_COUNT;
    }

    protected void cleanDatabaseOut() {
        if (isNetworkAvailable()) {
            mContentResolver.delete(mUri, null, null);
        }
    }

    protected boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = CoreApplication.get(getActivity(), getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    protected String getRequestUrl(int offset) {
        return mRequestUrl + "&count=" + Api.GET_COUNT + "&offset=" + offset;
    }

    protected void onError(Exception e) {
        mProgressBar.setVisibility(View.GONE);

        if (UnknownHostException.class.isInstance(e)) {
            Toast.makeText(getActivity(), "Проверьте подключение и\n" + "      повторите попытку", Toast.LENGTH_SHORT).show();
        } else {
            mErrorText.setVisibility(View.VISIBLE);
            mErrorText.setText(e.getMessage());
        }
    }

    protected int getRealAdapterCount(ListAdapter adapter) {
        if (adapter == null) {
            return 0;
        }
        int count = adapter.getCount();
        if (adapter instanceof HeaderViewListAdapter) {
            HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) adapter;
            count = count - headerViewListAdapter.getFootersCount() - headerViewListAdapter.getHeadersCount();
        }
        return count;
    }

    private void startAsynchLoad() {
        if (mLoaderManager.getLoader(CURSOR_LOADER) == null) {
            mLoaderManager.initLoader(CURSOR_LOADER, null, ZBaseListFragment.this);
        } else {
            mLoaderManager.destroyLoader(CURSOR_LOADER);
            mLoaderManager.initLoader(CURSOR_LOADER, null, ZBaseListFragment.this);
        }
    }

    @Override
    public void onLoadingError(Exception e) {
        onError(e);
        startAsynchLoad();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

    public abstract int getLayout();

    public abstract void whileOnCreateView(View view);

    public abstract ZCursorAdapter getAdapter();

    public abstract DatabaseProcessor getProcessor();

    public abstract String getRequestUrl();

    public abstract Uri getContentUri();

    public abstract View getFooder();

}
