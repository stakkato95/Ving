package com.github.stakkato95.ving.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.database.DialogsTable;

/**
 * Created by Artyom on 20.01.2015.
 */
public class DialogsAdapter extends ZCursorAdapter {

    public DialogsAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.adapter_dialog, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView photoImage = (ImageView) view.findViewById(android.R.id.icon);
        TextView titleText = (TextView) view.findViewById(android.R.id.text1);

        String title = cursor.getString(cursor.getColumnIndex(DialogsTable._DIALOG_NAME));
        titleText.setText(title);

        String photoUrl = cursor.getString(cursor.getColumnIndex(DialogsTable._PHOTO_100));
        getImageLoader().obtainImage(photoImage, photoUrl);
    }

}