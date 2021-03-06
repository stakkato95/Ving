package com.github.stakkato95.ving.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.stakkato95.ving.CoreApplication;
import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.adapter.DividerDecoration;
import com.github.stakkato95.ving.adapter.ZRecyclerCursorAdapter;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.fragment.assist.FragmentId;
import com.github.stakkato95.ving.loader.DataLoader;
import com.github.stakkato95.ving.processor.DBProcessor;
import com.github.stakkato95.ving.source.VkDataSource;
import com.github.stakkato95.ving.utils.FragmentUtils;

import java.net.UnknownHostException;

/**
 * Created by Artyom on 02.02.2015.
 */
public abstract class ZListFragment extends Fragment
        implements DataLoader.DatabaseCallback, LoaderManager.LoaderCallbacks<Cursor> {

    public static interface ClickCallback {
        void showDetails(FragmentId fragmentId, String requestField);
    }

    protected RecyclerView mRecyclerView;
    protected ZRecyclerCursorAdapter mZRecyclerAdapter;
    protected ZRecyclerCursorAdapter.OnRecyclerClickListener mOnClickListener;

    protected int mFirstVisiblePosition;
    protected int mPositionVisibleHeight;
    protected boolean isRotated = false;

    private static final String FIRST_VISIBLE_POSITION = "first_visible_position";
    private static final String POSITION_VISIBLE_HEIGHT = "position_visible_height";

    protected TextView mErrorText;
    protected ProgressBar mProgressBar;
    protected View mFooder;

    private static final int CURSOR_LOADER = 0;
    private LoaderManager mLoaderManager;
    protected ContentResolver mContentResolver;
    protected Uri mUri;

    protected DataLoader mDataLoader;
    protected DBProcessor mProcessor;
    protected VkDataSource mVkDataSource;
    protected String mRequestUrl;
    protected int mRequestOffset;

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
        View view = inflater.inflate(getLayout(), container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);

        mProgressBar = (ProgressBar) view.findViewById(android.R.id.progress);
        mErrorText = (TextView) view.findViewById(R.id.loading_error_text_view);

        mFooder = View.inflate(getActivity(), R.layout.view_fooder, null);
        mFooder.setOnClickListener(new View.OnClickListener() {
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
                        mFooder.findViewById(android.R.id.progress).setVisibility(View.GONE);
                        mFooder.findViewById(android.R.id.text1).setVisibility(View.VISIBLE);
                        onError(e);
                    }
                }, getRequestUrl(mRequestOffset), mVkDataSource, mProcessor);
                mRequestOffset += Api.GET_COUNT;
            }
        });

        whileOnCreateView(view, savedInstanceState);

        mUri = getUri();
        mRequestUrl = getRequestUrl();
        mProcessor = getProcessor();

        loadData();
        if (savedInstanceState != null) {
            isRotated = true;
            mFirstVisiblePosition = savedInstanceState.getInt(FIRST_VISIBLE_POSITION, 0);
            mPositionVisibleHeight = savedInstanceState.getInt(POSITION_VISIBLE_HEIGHT, 0);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        int firstVisiblePosition = getRecyclerView().getFirstVisiblePosition();
//        View view = getRecyclerView().getChildAt(0);
//        int positionVisibleHeight = (view == null) ? 0 : view.getTop();
//        outState.putInt(FIRST_VISIBLE_POSITION, firstVisiblePosition);
//        outState.putInt(POSITION_VISIBLE_HEIGHT, positionVisibleHeight);
    }


    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    protected ZRecyclerCursorAdapter getRecyclerAdapter() {
        return mZRecyclerAdapter;
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
        return mRequestUrl + "&" + Api.FIELD_COUNT + Api.GET_COUNT + "&" + Api.FIELD_OFFSET + offset;
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

    protected ClickCallback getCallback() {
        return FragmentUtils.findFirstResponderFor(this, ClickCallback.class);
    }

    private void startAsynchLoad() {
        if (mLoaderManager.getLoader(CURSOR_LOADER) == null) {
            mLoaderManager.initLoader(CURSOR_LOADER, null, ZListFragment.this);
        } else {
            mLoaderManager.destroyLoader(CURSOR_LOADER);
            mLoaderManager.initLoader(CURSOR_LOADER, null, ZListFragment.this);
        }
    }

    //DataLoder
    @Override
    public void onLoadingError(Exception e) {
        onError(e);
        startAsynchLoad();
    }

    @Override
    public void onLoadingFinished() {
        startAsynchLoad();
    }

    //LoaderManager

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mZRecyclerAdapter == null) {
            mZRecyclerAdapter = getAdapter(getActivity(), null);
            getRecyclerAdapter().setOnClickListener(getOnClickListener());
            getRecyclerAdapter().setHasStableIds(true);
            getRecyclerView().setAdapter(getRecyclerAdapter());
        }
        getRecyclerAdapter().swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    public abstract int getLayout();

    public abstract void whileOnCreateView(View view, Bundle savedInstanceState);

    public abstract DBProcessor getProcessor();

    public abstract String getRequestUrl();

    public abstract Uri getUri();

    public abstract ZRecyclerCursorAdapter getAdapter(Context context, Cursor cursor);

    public ZRecyclerCursorAdapter.OnRecyclerClickListener getOnClickListener() { return null; }

    public FragmentId getFragmentId() {
        return null;
    }

}
