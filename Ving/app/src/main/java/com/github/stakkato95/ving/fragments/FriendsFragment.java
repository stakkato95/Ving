package com.github.stakkato95.ving.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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
import com.github.stakkato95.ving.adapter.FriendsAdapter;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.database.FriendsTable;
import com.github.stakkato95.ving.loader.DataLoader;
import com.github.stakkato95.ving.processing.FriendProcessor;
import com.github.stakkato95.ving.provider.VingContentProvider;
import com.github.stakkato95.ving.source.VkDataSource;

import java.net.UnknownHostException;

public class FriendsFragment extends ListFragment implements DataLoader.DatabaseCallback, LoaderManager.LoaderCallbacks<Cursor>, FriendsAdapter.Callback {

    private Context mContext;
    private ListView mListView;
    private FriendsAdapter mFriendAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;
    private View mFooter;
    private boolean isPaginationEnabled = true;

    private static final int CURSOR_LOADER = 0;
    private LoaderManager mLoaderManager;
    private ContentResolver mContentResolver;

    private FriendProcessor mFriendProcessor;
    private VkDataSource mVkDataSource;
    private int REQUEST_OFFSET;

    public FriendsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mContentResolver = mContext.getContentResolver();
        cleanDatabaseOut();
        mFriendProcessor = new FriendProcessor(mContext);
        mVkDataSource = VkDataSource.get(mContext);
        mLoaderManager = getActivity().getSupportLoaderManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        mFooter = View.inflate(mContext, R.layout.view_footer, null);
        mFriendAdapter = new FriendsAdapter(mContext, null, 0, this);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mFriendAdapter);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int mPreviousTotalItemCount = 0;
            private static final int VISIBLE_THRESHOLD = 5;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) { }

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
                            setFooterVisibility();
                            if (mLoaderManager.getLoader(CURSOR_LOADER) == null) {
                                mLoaderManager.initLoader(CURSOR_LOADER, null, FriendsFragment.this);
                            } else {
                                mLoaderManager.destroyLoader(CURSOR_LOADER);
                                mLoaderManager.initLoader(CURSOR_LOADER, null, FriendsFragment.this);
                            }
                        }

                        @Override
                        public void onLoadingStarted() {

                        }

                        @Override
                        public void onLoadingError(Exception e) {

                        }
                    },getRequestUrl(REQUEST_OFFSET),mVkDataSource,mFriendProcessor);
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
        return Api.getFriends(offset);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) CoreApplication.get(mContext, mContext.CONNECTIVITY_SERVICE);
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
            mLoaderManager.initLoader(CURSOR_LOADER, null, FriendsFragment.this);
        } else {
            mLoaderManager.destroyLoader(CURSOR_LOADER);
            mLoaderManager.initLoader(CURSOR_LOADER, null, FriendsFragment.this);
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
        loader.getDataToDatabase(this, getRequestUrl(REQUEST_OFFSET), mVkDataSource, mFriendProcessor);
        REQUEST_OFFSET += Api.GET_COUNT;
    }

    private void cleanDatabaseOut() {
        if(isNetworkAvailable()) {
            mContentResolver.delete(VingContentProvider.FRIENDS_CONTENT_URI,null,null);
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
                mListView.addFooterView(mFooter,null,false);
                mListView.setFooterDividersEnabled(true);
            }
        } else {
            mListView.removeFooterView(mFooter);
        }
        isPaginationEnabled = (getRealAdapterCount(mFriendAdapter) % Api.GET_COUNT) == 0;
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection;
        if (isNetworkAvailable()) {
            projection = FriendsTable.PROJECTION;
        } else {
            projection = FriendsTable.PROJECTION_OFFLINE;
        }
        return new CursorLoader(mContext, VingContentProvider.FRIENDS_CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFriendAdapter.swapCursor(data);
        setFooterVisibility();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFriendAdapter.swapCursor(null);
    }

    @Override
    public void onPageLimitReached(int cursorPosition) {

    }
}
