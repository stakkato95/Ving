package com.github.stakkato95.ving.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.github.stakkato95.ving.adapter.DialogsAdapter;
import com.github.stakkato95.ving.adapter.ZCursorAdapter;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.fragment.assist.FragmentId;
import com.github.stakkato95.ving.loader.DataLoader;
import com.github.stakkato95.ving.processor.DatabaseProcessor;
import com.github.stakkato95.ving.source.VkDataSource;
import com.github.stakkato95.ving.utils.FragmentUtils;

import java.net.UnknownHostException;

/**
 * Created by Artyom on 18.01.2015.
 */
public abstract class ZListFragment extends ListFragment implements DataLoader.DatabaseCallback, LoaderManager.LoaderCallbacks<Cursor> {

    public static interface ClickCallback {
        void showDetails(FragmentId fragmentId, String requestField);
    }

    private Context mContext;
    private ListView mListView;
    private ZCursorAdapter mZAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mErrorText;
    private ProgressBar mProgressBar;
    private View mFooter;

    private static final int CURSOR_LOADER = 0;
    private LoaderManager mLoaderManager;
    private ContentResolver mContentResolver;
    private Uri mUri;
    private String[] mProjection;
    private String[] mProjectionOffline;

    private DataLoader mDataLoader;
    private DatabaseProcessor mProcessor;
    private VkDataSource mVkDataSource;
    private int mRequestOffset;
    private String mRequestUrl;

    public ZListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mContentResolver = mContext.getContentResolver();
        mLoaderManager = getActivity().getSupportLoaderManager();
        mDataLoader = new DataLoader();
        mVkDataSource = VkDataSource.get(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zlist, container, false);

        mErrorText = (TextView) view.findViewById(R.id.loading_error_text_view);
        mProgressBar = (ProgressBar) view.findViewById(android.R.id.progress);
        mFooter = View.inflate(mContext, R.layout.view_footer, null);
        mFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataLoader.getDataToDatabase(new DataLoader.DatabaseCallback() {

                    @Override
                    public void onLoadingFinished() {
                        mFooter.findViewById(android.R.id.progress).setVisibility(View.GONE);
                        mFooter.findViewById(android.R.id.text1).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingStarted() {
                        mFooter.findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
                        mFooter.findViewById(android.R.id.text1).setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingError(Exception e) {
                        mFooter.findViewById(android.R.id.progress).setVisibility(View.GONE);
                        mFooter.findViewById(android.R.id.text1).setVisibility(View.VISIBLE);
                        onError(e);
                    }
                }, getRequestUrl(mRequestOffset), mVkDataSource, mProcessor);
                mRequestOffset += Api.GET_COUNT;
            }
        });


        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.addFooterView(mFooter);
        mListView.setFooterDividersEnabled(false);
        mZAdapter = getAdapter();
        mListView.setAdapter(mZAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFooter.setVisibility(View.GONE);
                mListView.setFooterDividersEnabled(false);
                loadData();
            }
        });

        mUri = getContentUri();
        mRequestUrl = getRequestUrl();
        mProcessor = getProcessor();
        mProjection = getProjection();
        mProjectionOffline = getProjectionOffline();

        loadData();
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String requestField = (String) v.getTag(DialogsAdapter.ID_KEY) + id;
        ClickCallback callback = getCallback();
        if (callback != null) {
            callback.showDetails(getFragmentId(), requestField);
        }
    }

    private ClickCallback getCallback() {
        return FragmentUtils.findFirstResponderFor(this, ClickCallback.class);
    }


    private String getRequestUrl(int offset) {
        return mRequestUrl + "&count=" + Api.GET_COUNT + "&offset=" + offset;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = CoreApplication.get(mContext, mContext.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }


    @Override
    public void onLoadingStarted() {
        if (mSwipeRefreshLayout != null && !mSwipeRefreshLayout.isRefreshing()) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadingFinished() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mProgressBar.setVisibility(View.GONE);

        startAsynchLoad();
    }

    @Override
    public void onLoadingError(Exception e) {
        mSwipeRefreshLayout.setRefreshing(false);
        onError(e);
        startAsynchLoad();
    }

    private void onError(Exception e) {
        mProgressBar.setVisibility(View.GONE);

        if (UnknownHostException.class.isInstance(e)) {
            Toast.makeText(getActivity(), "Проверьте подключение и\n" + "      повторите попытку", Toast.LENGTH_SHORT).show();
        } else {
            mErrorText.setVisibility(View.VISIBLE);
            mErrorText.setText(e.getMessage());
        }
    }

    private void loadData() {
        cleanDatabaseOut();
        mRequestOffset = 0;
        mDataLoader.getDataToDatabase(this, getRequestUrl(mRequestOffset), mVkDataSource, mProcessor);
        mRequestOffset += Api.GET_COUNT;
    }

    private void startAsynchLoad() {
        if (mLoaderManager.getLoader(CURSOR_LOADER) == null) {
            mLoaderManager.initLoader(CURSOR_LOADER, null, ZListFragment.this);
        } else {
            mLoaderManager.destroyLoader(CURSOR_LOADER);
            mLoaderManager.initLoader(CURSOR_LOADER, null, ZListFragment.this);
        }
    }

    private void cleanDatabaseOut() {
        if (isNetworkAvailable()) {
            mContentResolver.delete(mUri, null, null);
        }
    }

    private int getRealAdapterCount(ListAdapter adapter) {
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

    private void setFooterVisibility() {
        if ((getRealAdapterCount(mZAdapter) % Api.GET_COUNT) == 0) {
            if (mRequestOffset == Api.GET_COUNT) {
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    if (mListView.getFooterViewsCount() == 0) {
                        mListView.addFooterView(mFooter, null, false);
                    }
                    if (mFooter.getVisibility() == View.GONE) {
                        mFooter.setVisibility(View.VISIBLE);
                        mListView.setFooterDividersEnabled(true);
                    }
                }
            }
        } else {
            mListView.removeFooterView(mFooter);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (isNetworkAvailable()) {
            return new CursorLoader(mContext, mUri, mProjection, null, null, null);
        } else {
            return new CursorLoader(mContext, mUri, mProjectionOffline, null, null, null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mZAdapter.swapCursor(data);
        setFooterVisibility();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    public abstract ZCursorAdapter getAdapter();

    public abstract DatabaseProcessor getProcessor();

    public abstract String getRequestUrl();

    public abstract Uri getContentUri();

    public abstract String[] getProjection();

    public abstract String[] getProjectionOffline();

    public abstract FragmentId getFragmentId();

}