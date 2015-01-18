package com.github.stakkato95.ving.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.stakkato95.ving.CoreApplication;
import com.github.stakkato95.ving.adapter.FriendsAdapter;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.R;
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

    private FriendProcessor mFriendProcessor;
    private VkDataSource mVkDataSource;

    public FriendsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mFriendProcessor = new FriendProcessor(mContext);
        mVkDataSource = VkDataSource.get(mContext);
        mLoaderManager = getActivity().getSupportLoaderManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        mFooter = View.inflate(mContext, R.layout.view_footer, null);
        mFriendAdapter = new FriendsAdapter(mContext,null,0 , this);
        mListView = (ListView)view.findViewById(android.R.id.list);
        mListView.setAdapter(mFriendAdapter);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int mPreviousTotalItemCount = 0;
            private static final int VISIBLE_THRESHOLD = 5;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                ListAdapter adapter = view.getAdapter();
//                int currentAdapterCount = getRealAdapterCount(adapter);

//                if (currentAdapterCount == 0) {
//                    return;
//                }
                if (mPreviousTotalItemCount!= totalItemCount && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
                    mPreviousTotalItemCount = totalItemCount;
//                    Dataloader.loadData(new DataManager.Callback<List<Friend>>() {
//                        @Override
//                        public void onDataLoadStart() {
//
//                        }
//
//                        @Override
//                        public void onDone(List<Friend> data) {
//                            reloadData(data);
//                            resetFooter();
//                        }
//
//                        @Override
//                        public void onError(Exception e) {
//                            resetFooter();
//                        }
//                    }, getRequestUrl(Api.FRIENDS_GET_COUNT, currentAdapterCount), mVkDataSource, mFriendProcessor);
                }
            }
        });

        mProgressBar = (ProgressBar)view.findViewById(android.R.id.progress);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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



    private String getRequestUrl() {
        return Api.getFriends();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)CoreApplication.get(mContext, mContext.CONNECTIVITY_SERVICE);
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
        if(mSwipeRefreshLayout.isRefreshing()) {
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
            Toast.makeText(mContext,"Проверьте подключение и\n" + "      повторите попытку", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadData() {
        DataLoader loader = new DataLoader(mContext);
        loader.getDataToDatabase(this, getRequestUrl(), mVkDataSource, mFriendProcessor);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection;
        if(isNetworkAvailable()) {
            projection = FriendsTable.PROJECTION;
        } else {
            projection = FriendsTable.PROJECTION_OFFLINE;
        }
        return new CursorLoader(mContext, VingContentProvider.FRIENDS_CONTENT_URI, projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFriendAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFriendAdapter.swapCursor(null);
    }

    @Override
    public void onPageLimitReached(int cursorPosition) {

    }
}
