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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.stakkato95.ving.CoreApplication;
import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.activity.MainActivity;
import com.github.stakkato95.ving.adapter.DialogHistoryAdapter;
import com.github.stakkato95.ving.adapter.ZCursorAdapter;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.database.DialogHistoryTable;
import com.github.stakkato95.ving.loader.DataLoader;
import com.github.stakkato95.ving.processor.DatabaseProcessor;
import com.github.stakkato95.ving.processor.DialogHistoryProcessor;
import com.github.stakkato95.ving.provider.ZContentProvider;
import com.github.stakkato95.ving.source.VkDataSource;

import java.net.UnknownHostException;

/**
 * Created by Artyom on 25.01.2015.
 */
public class DialogHistoryFragment extends ListFragment implements DataLoader.DatabaseCallback, LoaderManager.LoaderCallbacks<Cursor> {

    private Context mContext;
    private ListView mListView;
    private ZCursorAdapter mZAdapter;
    private ProgressBar mProgressBar;
    private TextView mErrorText;
    private View mHeader;
    private boolean isPaginationEnabled = true;

    private static final int CURSOR_LOADER = 0;
    private LoaderManager mLoaderManager;
    private ContentResolver mContentResolver;
    private Uri mContentType;
    private String[] mProjection;

    private DataLoader mDataLoader;
    private DatabaseProcessor mProcessor;
    private VkDataSource mVkDataSource;
    private int REQUEST_OFFSET;
    private String mRequestField;

    public DialogHistoryFragment() { }

    public static DialogHistoryFragment newInstance(String requestField) {
        DialogHistoryFragment fragment = new DialogHistoryFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_REQUEST_FIELD, requestField);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mContentResolver = mContext.getContentResolver();
        mLoaderManager = getActivity().getSupportLoaderManager();
        mDataLoader = new DataLoader();
        mVkDataSource = VkDataSource.get(mContext);
        mProcessor = new DialogHistoryProcessor(mContext);

        mRequestField = getArguments().getString(MainActivity.KEY_REQUEST_FIELD);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_history, container, false);

        mZAdapter = new DialogHistoryAdapter(mContext,null,0);
        mHeader = View.inflate(mContext, R.layout.view_footer, null);
        mListView = (ListView)view.findViewById(android.R.id.list);
        mListView.setAdapter(mZAdapter);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            private static final int VISIBLE_THRESHOLD = 5;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                ListAdapter adapter = view.getAdapter();
                int currentAdapterCount = getRealAdapterCount(adapter);

                if (currentAdapterCount == (firstVisibleItem + visibleItemCount)) {
                    return;
                }
                if (firstVisibleItem <= VISIBLE_THRESHOLD) {
                    mDataLoader.getDataToDatabase(new DataLoader.DatabaseCallback() {

                        @Override
                        public void onLoadingFinished() {
                            if (mLoaderManager.getLoader(CURSOR_LOADER) == null) {
                                mLoaderManager.initLoader(CURSOR_LOADER, null, DialogHistoryFragment.this);
                            } else {
                                mLoaderManager.destroyLoader(CURSOR_LOADER);
                                mLoaderManager.initLoader(CURSOR_LOADER, null, DialogHistoryFragment.this);
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


        mProgressBar = (ProgressBar)view.findViewById(android.R.id.progress);
        mErrorText  = (TextView)view.findViewById(R.id.loading_error_text_view);;
        ImageView mSend = (ImageView)view.findViewById(R.id.dialog_history_send);
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

        mProjection = DialogHistoryTable.PROJECTION;
        mContentType = ZContentProvider.DIALOGS_HISTORY_CONTENT_URI;

        loadData();
        return view;
    }


    @Override
    public void onLoadingStarted() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadingFinished() {
        mProgressBar.setVisibility(View.GONE);

        if (mLoaderManager.getLoader(CURSOR_LOADER) == null) {
            mLoaderManager.initLoader(CURSOR_LOADER, null, DialogHistoryFragment.this);
        } else {
            mLoaderManager.destroyLoader(CURSOR_LOADER);
            mLoaderManager.initLoader(CURSOR_LOADER, null, DialogHistoryFragment.this);
        }
    }

    @Override
    public void onLoadingError(Exception e) {
        mProgressBar.setVisibility(View.GONE);

        if (UnknownHostException.class.isInstance(e)) {
            Toast.makeText(mContext, "Проверьте подключение и\n" + "      повторите попытку", Toast.LENGTH_SHORT).show();
        } else {
            mErrorText.setVisibility(View.VISIBLE);
            mErrorText.setText(e.getMessage());
        }
    }

    private void loadData() {
        cleanDatabaseOut();
        REQUEST_OFFSET = 0;
        mDataLoader.getDataToDatabase(this, getRequestUrl(REQUEST_OFFSET), mVkDataSource, mProcessor);
        REQUEST_OFFSET += Api.GET_COUNT;
    }

    private String getRequestUrl(int offset) {
        return Api.getDialogHistory() + mRequestField + Api.FIELD_COUNT + Api.GET_COUNT + Api.FIELD_OFFSET + offset;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = CoreApplication.get(mContext, mContext.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
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

    private void setHeaderVisibility() {
        if (isPaginationEnabled) {
            if (REQUEST_OFFSET == Api.GET_COUNT) {
                mListView.addHeaderView(mHeader, null, false);
                mListView.setFooterDividersEnabled(true);
            }
        } else {
            mListView.removeHeaderView(mHeader);
        }
        isPaginationEnabled = (getRealAdapterCount(mZAdapter) % Api.GET_COUNT) == 0;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //if (isNetworkAvailable()) {
            return new CursorLoader(mContext, mContentType, mProjection, null, null, null);
        //}
        //return new CursorLoader(mContext, mContentType, mProjectionOffline, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mZAdapter.swapCursor(data);
        setHeaderVisibility();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}