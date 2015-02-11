package com.github.stakkato95.ving.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.activity.MainActivity;
import com.github.stakkato95.ving.adapter.DialogHistoryAdapter;
import com.github.stakkato95.ving.adapter.ZCursorAdapter;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.api.Shipper;
import com.github.stakkato95.ving.api.ShipperBuilder;
import com.github.stakkato95.ving.database.DialogHistoryTable;
import com.github.stakkato95.ving.processor.DBProcessor;
import com.github.stakkato95.ving.processor.DialogHistoryDBProcessor;
import com.github.stakkato95.ving.processor.MessageSendProcessor;
import com.github.stakkato95.ving.provider.ZContentProvider;
import com.github.stakkato95.ving.utils.ProcessingUtils;

import java.util.Date;

/**
 * Created by Artyom on 02.02.2015.
 */
public class DialogHistoryFragment extends ZListFragment implements Shipper.Callback<Integer> {

    private String mRequestField;
    private EditText mEditText;

    public static DialogHistoryFragment newInstance(String requestField) {
        DialogHistoryFragment fragment = new DialogHistoryFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_REQUEST_FIELD, requestField);
        fragment.setArguments(args);
        return fragment;
    }

    private void setHeaderVisibility() {
        if ((getRealAdapterCount(getListAdapter()) % Api.GET_COUNT) == 0 && isNetworkAvailable()) {
            if (getListView().getHeaderViewsCount() == 0) {
                getListView().addHeaderView(mFooder);
            }
            if (mFooder.getVisibility() == View.GONE) {
                mFooder.setVisibility(View.VISIBLE);
                getListView().setFooterDividersEnabled(true);
            }
        } else {
            getListView().removeHeaderView(mFooder);
        }
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_dialog_history;
    }

    @Override
    public void whileOnCreateView(View view, Bundle savedInstanceState) {
        mFooder = View.inflate(getActivity(), R.layout.view_footer, null);
        mListView = (ListView)view.findViewById(android.R.id.list);
        getListView().setDividerHeight(0);
        getListView().addHeaderView(mFooder);
        getListView().setHeaderDividersEnabled(true);

        mEditText = (EditText) view.findViewById(R.id.dialog_history_message);
        ImageView mSend = (ImageView) view.findViewById(R.id.dialog_history_send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = mEditText.getText().toString();
                mEditText.setText(Api.EMPTY_STRING);

                Shipper shipper = new ShipperBuilder()
                        .setRequestField(mRequestField)
                        .setMessageText(messageText)
                        .setProcessor(new MessageSendProcessor())
                        .setCallback(DialogHistoryFragment.this)
                        .setContext(getActivity())
                        .build();
                shipper.send();

                long date = new Date().getTime();
                String stringDate = ProcessingUtils.getDate(date);
                ContentValues contentValues = new ContentValues();
                contentValues.put(DialogHistoryTable._BODY, messageText);
                contentValues.put(DialogHistoryTable._DATE, date);
                contentValues.put(DialogHistoryTable._ROUTE, Api.MESSAGE_ROUTE_OUT);
                contentValues.put(DialogHistoryTable._DATE_TEXT, stringDate);
                mContentResolver.bulkInsert(mUri, new ContentValues[]{contentValues});
            }
        });

        mRequestField = getArguments().getString(MainActivity.KEY_REQUEST_FIELD);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().removeHeaderView(mFooder);
    }

    @Override
    public ZCursorAdapter getAdapter() {
        return new DialogHistoryAdapter(getActivity(), null, 0);
    }

    @Override
    public DBProcessor getProcessor() {
        return new DialogHistoryDBProcessor(getActivity());
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

    //LoaderManager
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = " ASC";
        return new CursorLoader(getActivity(), mUri, DialogHistoryTable.PROJECTION, null, null, DialogHistoryTable._DATE + sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        super.onLoadFinished(loader, data);
        if (!isRotated) {
            setHeaderVisibility();
            int itemIndex;
            if (getListAdapter().getCount() % Api.GET_COUNT == 0) {
                itemIndex = Api.GET_COUNT + getListView().getHeaderViewsCount();
            } else {
                itemIndex = getListAdapter().getCount() % Api.GET_COUNT;
            }

            getListView().setSelectionFromTop(itemIndex, mFooder.getHeight());
        }
        isRotated = false;
    }

    @Override
    public void onShippingPerformed(Integer integer) {
        setSelection(getListView().getCount());
    }
}
