package com.github.stakkato95.ving.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.adapter.DividerDecoration;
import com.github.stakkato95.ving.api.Api;

/**
 * Created by Artyom on 18.01.2015.
 */
public abstract class ZQueueFragment extends ZListFragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private String[] mProjection;
    private String[] mProjectionOffline;

    public ZQueueFragment() {
    }

    @Override
    public void whileOnCreateView(View view, Bundle savedInstanceState) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFooder.setVisibility(View.GONE);
                loadData();
            }
        });

        mProjection = getProjection();
        mProjectionOffline = getProjectionOffline();
    }

    private void setFooterVisibility() {
        if ((getRecyclerAdapter().getRealItemCount() % Api.GET_COUNT) == 0) {
            if (mRequestOffset == Api.GET_COUNT) {
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    if (getRecyclerAdapter().getFooterViewsCount() == 0) {
                        getRecyclerAdapter().addFooterView(mFooder);
                    }
                    if (getRecyclerAdapter().getFooter().getVisibility() == View.GONE) {
                        getRecyclerAdapter().getFooter().setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            getRecyclerAdapter().removeFooterView(mFooder);
        }
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
        //todo something with constantly invoked code
        getRecyclerView().addItemDecoration(new DividerDecoration(getActivity()));
        if (data.getCount() == getRecyclerAdapter().getStandardDose()) {
            getRecyclerView().scrollToPosition(0);
        }
    }


    //ZListFragment overriding
    @Override
    public int getLayout() {
        return R.layout.fragment_zlist;
    }

    public abstract String[] getProjection();

    public abstract String[] getProjectionOffline();

}