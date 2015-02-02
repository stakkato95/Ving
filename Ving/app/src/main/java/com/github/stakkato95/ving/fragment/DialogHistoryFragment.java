package com.github.stakkato95.ving.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.activity.MainActivity;
import com.github.stakkato95.ving.adapter.DialogHistoryAdapter;
import com.github.stakkato95.ving.adapter.ZCursorAdapter;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.database.DialogHistoryTable;
import com.github.stakkato95.ving.processor.DatabaseProcessor;
import com.github.stakkato95.ving.processor.DialogHistoryProcessor;
import com.github.stakkato95.ving.provider.ZContentProvider;

/**
 * Created by Artyom on 02.02.2015.
 */
public class DialogHistoryFragment extends ZBaseListFragment {

    private String mRequestField;

    public static DialogHistoryFragment newInstance(String requestField) {
        DialogHistoryFragment fragment = new DialogHistoryFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_REQUEST_FIELD, requestField);
        fragment.setArguments(args);
        return fragment;
    }

    private void setHeaderVisibility() {
        if ((getRealAdapterCount(mZAdapter) % Api.GET_COUNT) == 0 && isNetworkAvailable()) {
            if (mListView.getHeaderViewsCount() == 0) {
                mListView.addHeaderView(mFooder);
            }
            if (mFooder.getVisibility() == View.GONE) {
                mFooder.setVisibility(View.VISIBLE);
                mListView.setFooterDividersEnabled(true);
            }
        } else {
            mListView.removeHeaderView(mFooder);
        }
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_dialog_history;
    }

    @Override
    public void whileOnCreateView(View view) {
        mFooder = View.inflate(getActivity(), R.layout.view_footer, null);
        mListView.addHeaderView(mFooder);
        mListView.setHeaderDividersEnabled(true);

        ImageView mSend = (ImageView) view.findViewById(R.id.dialog_history_send);
        mSend.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int maskedAction = event.getActionMasked();
                switch (maskedAction) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(getResources().getColor(R.color.button_material_light));
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(getResources().getColor(R.color.WindowBackground));
                        return true;
                    default:
                        return true;
                }
            }
        });

        mRequestField = getArguments().getString(MainActivity.KEY_REQUEST_FIELD);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView.removeHeaderView(mFooder);
    }

    @Override
    public ZCursorAdapter getAdapter() {
        return new DialogHistoryAdapter(getActivity(), null, 0);
    }

    @Override
    public DatabaseProcessor getProcessor() {
        return new DialogHistoryProcessor(getActivity());
    }

    @Override
    public String getRequestUrl() {
        return Api.getDialogHistory() + mRequestField;
    }

    @Override
    public Uri getUri() {
        return ZContentProvider.DIALOGS_HISTORY_CONTENT_URI;
    }

    //DataLoader
    @Override
    public void onLoadingStarted() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadingFinished() {
        super.onLoadingFinished();
        mProgressBar.setVisibility(View.GONE);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = " ASC";
        return new CursorLoader(getActivity(), mUri, DialogHistoryTable.PROJECTION, null, null, DialogHistoryTable._DATE + sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        super.onLoadFinished(loader, data);
        setHeaderVisibility();
        int itemIndex;
        if (mZAdapter.getCount() % Api.GET_COUNT == 0) {
            itemIndex = Api.GET_COUNT + mListView.getHeaderViewsCount();
        } else {
            itemIndex = mZAdapter.getCount() % Api.GET_COUNT;
        }

        //magic number that is obtained by trial and error
        int androidMagic = mFooder.getHeight() + 2;
        mListView.setSelectionFromTop(itemIndex, androidMagic);
    }
}
