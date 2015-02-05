package com.github.stakkato95.ving.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.fragment.assist.FragmentId;
import com.github.stakkato95.ving.utils.FragmentUtils;

/**
 * Created by Artyom on 18.01.2015.
 */
public abstract class ZQueueFragment extends ZListFragment {

    public static interface ClickCallback {
        void showDetails(FragmentId fragmentId, String requestField);
    }

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private String[] mProjection;
    private String[] mProjectionOffline;

    public ZQueueFragment() {
    }

    @Override
    public void whileOnCreateView(View view) {
        mFooder = View.inflate(getActivity(), R.layout.view_footer, null);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setFooterDividersEnabled(false);
        listView.addFooterView(mFooder);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFooder.setVisibility(View.GONE);
                getListView().setFooterDividersEnabled(false);
                loadData();
            }
        });
        mProjection = getProjection();
        mProjectionOffline = getProjectionOffline();
    }

    private void setFooterVisibility() {
        if ((getRealAdapterCount(getListAdapter()) % Api.GET_COUNT) == 0) {
            if (mRequestOffset == Api.GET_COUNT) {
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    if (getListView().getFooterViewsCount() == 0) {
                        getListView().addFooterView(mFooder, null, false);
                    }
                    if (mFooder.getVisibility() == View.GONE) {
                        mFooder.setVisibility(View.VISIBLE);
                        getListView().setFooterDividersEnabled(true);
                    }
                }
            }
        } else {
            getListView().removeFooterView(mFooder);
        }
    }

    protected ClickCallback getCallback() {
        return FragmentUtils.findFirstResponderFor(this, ClickCallback.class);
    }

    //DataLoader
    @Override
    public void onLoadingStarted() {
        if (mSwipeRefreshLayout != null && !mSwipeRefreshLayout.isRefreshing()) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadingFinished() {
        super.onLoadingFinished();
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoadingError(Exception e) {
        super.onLoadingError(e);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    //LoaderManager
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (isNetworkAvailable()) {
            return new CursorLoader(getActivity(), mUri, mProjection, null, null, null);
        } else {
            return new CursorLoader(getActivity(), mUri, mProjectionOffline, null, null, null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        super.onLoadFinished(loader, data);
        setFooterVisibility();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    //ZListFragment overriding
    @Override
    public int getLayout() {
        return R.layout.fragment_zlist;
    }

    public abstract String[] getProjection();

    public abstract String[] getProjectionOffline();

    public abstract FragmentId getFragmentId();

}