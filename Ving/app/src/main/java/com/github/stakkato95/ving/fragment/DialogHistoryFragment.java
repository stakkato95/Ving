package com.github.stakkato95.ving.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.activity.MainActivity;
import com.github.stakkato95.ving.adapter.DialogHistoryAdapter;
import com.github.stakkato95.ving.adapter.DialogHistoryRecyclerAdapter;
import com.github.stakkato95.ving.adapter.ZCursorAdapter;
import com.github.stakkato95.ving.adapter.ZRecyclerCursorAdapter;
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
        if ((getRecyclerAdapter().getRealItemCount() % Api.GET_COUNT) == 0 && isNetworkAvailable()) {
            if (getRecyclerAdapter().getHeaderViewsCount() == 0) {
                getRecyclerAdapter().addHeaderView(mFooder);
            }
            if (getRecyclerAdapter().getHeader().getVisibility() == View.GONE) {
                getRecyclerAdapter().getHeader().setVisibility(View.VISIBLE);
            }
        } else {
            getRecyclerAdapter().removeHeaderView(mFooder);
        }
    }

    @Override
    public ZRecyclerCursorAdapter getAdapter(Context context, Cursor cursor) {
        return new DialogHistoryRecyclerAdapter(context, cursor);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_dialog_history;
    }

    @Override
    public void whileOnCreateView(View view, Bundle savedInstanceState) {
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
        //getRecyclerAdapter().removeHeaderView(mFooder);
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
        super.onLoadFinished(loader,data);
        setHeaderVisibility();
        if (data.getCount() == Api.GET_COUNT && isNetworkAvailable()) {
            getRecyclerView().scrollToPosition(getRecyclerAdapter().getRealItemCount());
        } else if (!isNetworkAvailable()) {
            getRecyclerView().scrollToPosition(getRecyclerAdapter().getRealItemCount() - 1);
        }
    }

    @Override
    public void onShippingPerformed(Integer integer) {
        //getRecyclerView().setSelection(getRecyclerView().getCount());
    }
}
