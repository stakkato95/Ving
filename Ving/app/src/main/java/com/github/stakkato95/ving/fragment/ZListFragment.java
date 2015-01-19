package com.github.stakkato95.ving.fragment;

import android.net.Uri;
import android.support.v4.app.ListFragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.stakkato95.ving.CoreApplication;
import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.adapter.ZCursorAdapter;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.loader.DataLoader;
import com.github.stakkato95.ving.processor.DatabaseProcessor;
import com.github.stakkato95.ving.source.VkDataSource;

import java.net.UnknownHostException;

/**
 * Created by Artyom on 18.01.2015.
 */
public class ZListFragment extends ListFragment implements DataLoader.DatabaseCallback, LoaderManager.LoaderCallbacks<Cursor> {

    private Context mContext;
    private ListView mListView;
    private ZCursorAdapter mZAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;
    private View mFooter;
    private boolean isPaginationEnabled = true;

    private static final int CURSOR_LOADER = 0;
    private LoaderManager mLoaderManager;
    private ContentResolver mContentResolver;
    private Uri mContentType;
    private String[] mProjection;
    private String[] mProjectionOffline;

    private DatabaseProcessor mProcessor;
    private VkDataSource mVkDataSource;
    private int REQUEST_OFFSET;
    private String mRequestUrl;

    public ZListFragment() {
    }

    public static ZListFragment newInstance(ZCursorAdapter adapter,
                                            DatabaseProcessor processor,
                                            String url,
                                            Uri contentType,
                                            String[] projection,
                                            String[] projectionOffline) {
        ZListFragment configurableFragment = new ZListFragment();
        configurableFragment.setAdapter(adapter);
        configurableFragment.setProcessor(processor);
        configurableFragment.setRequestUrl(url);
        configurableFragment.setContentType(contentType);
        configurableFragment.setProjection(projection);
        configurableFragment.setProjectionOffline(projectionOffline);
        return configurableFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mContentResolver = mContext.getContentResolver();
        cleanDatabaseOut();
        mVkDataSource = VkDataSource.get(mContext);
        mLoaderManager = getActivity().getSupportLoaderManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zlist, container, false);

        mFooter = View.inflate(mContext, R.layout.view_footer, null);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mZAdapter);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int mPreviousTotalItemCount = 0;
            private static final int VISIBLE_THRESHOLD = 5;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                ListAdapter adapter = view.getAdapter();
                int currentAdapterCount = getRealAdapterCount(adapter);

                if (currentAdapterCount == 0) {
                    return;
                }
                if (mPreviousTotalItemCount != totalItemCount && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
                    mPreviousTotalItemCount = totalItemCount;
                    DataLoader loader = new DataLoader(mContext);
                    loader.getDataToDatabase(new DataLoader.DatabaseCallback() {

                        @Override
                        public void onLoadingFinished() {
                            //setFooterVisibility();
                            if (mLoaderManager.getLoader(CURSOR_LOADER) == null) {
                                mLoaderManager.initLoader(CURSOR_LOADER, null, ZListFragment.this);
                            } else {
                                mLoaderManager.destroyLoader(CURSOR_LOADER);
                                mLoaderManager.initLoader(CURSOR_LOADER, null, ZListFragment.this);
                            }
                        }

                        @Override
                        public void onLoadingStarted() {

                        }

                        @Override
                        public void onLoadingError(Exception e) {

                        }
                    }, getRequestUrl(REQUEST_OFFSET), mVkDataSource, mProcessor);
                    REQUEST_OFFSET += Api.GET_COUNT;
                }
            }
        });

        mProgressBar = (ProgressBar) view.findViewById(android.R.id.progress);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isPaginationEnabled = false;
                cleanDatabaseOut();
                loadData();
            }
        });


        loadData();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        if (mLoaderManager.getLoader(CURSOR_LOADER) == null) {
            mLoaderManager.initLoader(CURSOR_LOADER, null, ZListFragment.this);
        } else {
            mLoaderManager.destroyLoader(CURSOR_LOADER);
            mLoaderManager.initLoader(CURSOR_LOADER, null, ZListFragment.this);
        }
    }

    @Override
    public void onLoadingError(Exception e) {
        mSwipeRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);

        if (UnknownHostException.class.isInstance(e)) {
            Toast.makeText(mContext, "Проверьте подключение и\n" + "      повторите попытку", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
        REQUEST_OFFSET = 0;
        DataLoader loader = new DataLoader(mContext);
        loader.getDataToDatabase(this, getRequestUrl(REQUEST_OFFSET), mVkDataSource, mProcessor);
        REQUEST_OFFSET += Api.GET_COUNT;
    }

    private void cleanDatabaseOut() {
        if (isNetworkAvailable()) {
            mContentResolver.delete(mContentType, null, null);
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
        if (isPaginationEnabled) {
            if (REQUEST_OFFSET == Api.GET_COUNT) {
                mListView.addFooterView(mFooter, null, false);
                mListView.setFooterDividersEnabled(true);
            }
        } else {
            mListView.removeFooterView(mFooter);
        }
        isPaginationEnabled = (getRealAdapterCount(mZAdapter) % Api.GET_COUNT) == 0;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (isNetworkAvailable()) {
            return new CursorLoader(mContext, mContentType, mProjection, null, null, null);
        }
        return new CursorLoader(mContext, mContentType, mProjectionOffline, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mZAdapter.swapCursor(data);
        setFooterVisibility();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    private void setAdapter(ZCursorAdapter adapter) {
        mZAdapter = adapter;
    }

    private void setProcessor(DatabaseProcessor processor) {
        mProcessor = processor;
    }

    private void setRequestUrl(String requestUrl) {
        mRequestUrl = requestUrl;
    }

    private void setContentType(Uri contentType) {
        mContentType = contentType;
    }

    private void setProjection(String[] projection) {
        mProjection = projection;
    }

    private void setProjectionOffline(String[] projectionOffline) {
        mProjectionOffline = projectionOffline;
    }

}