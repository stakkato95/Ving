package com.github.stakkato95.ving.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.github.stakkato95.imageloader.ImageLoader;

/**
 * Created by Artyom on 13.02.2015.
 */
public abstract class ZRecyclerCursorAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>
        implements View.OnClickListener {

    private class RecyclerCursorObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            isDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            isDataValid = false;
            notifyDataSetChanged();
        }
    }

    public interface OnRecyclerClickListener {
        void onRecyclerItemClicked(View view);
    }

    public abstract class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }

    public abstract class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View view) {
            super(view);
        }

    }

    private Cursor mCursor;
    private RecyclerCursorObserver mCursorObserver;
    private boolean isDataValid;
    private int mRowIDColumn;
    private int mPreviousItemCount = 0;
    private int mStandardDose;

    protected View mFooter;
    protected View mHeader;

    private static final int HEADER_POSITION = 0;
    private int FOOTER_POSITION;
    protected static final int ITEM_REGULAR = 0;
    protected static final int ITEM_FOOTER = 1;
    protected static final int ITEM_HEADER = 2;

    private OnRecyclerClickListener mOnRecyclerClickListener;
    private ImageLoader mImageLoader;

    public ZRecyclerCursorAdapter(Context context, Cursor cursor) {
        mCursor = cursor;
        isDataValid = mCursor != null;
        mRowIDColumn = isDataValid ? mCursor.getColumnIndex(BaseColumns._ID) : -1;
        if (isDataValid) {
            mCursorObserver = new RecyclerCursorObserver();
            mCursor.registerDataSetObserver(mCursorObserver);
        }
        mImageLoader = ImageLoader.get(context);
    }

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        if (!isDataValid) {
            throw new IllegalStateException("data is invalid");
        } else {
            if (position != HEADER_POSITION || position != FOOTER_POSITION) {
                if (isHeaderUsed()) {
                    mCursor.moveToPosition(position - 1);
                } else {
                    mCursor.moveToPosition(position);
                }
                onBindViewHolder(viewHolder, mCursor, position);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        if (isDataValid && mCursor != null && mCursor.moveToPosition(position)) {
            return mCursor.getLong(mRowIDColumn);
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return getRealItemCount() + getFooterViewsCount() + getHeaderViewsCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && isFooterUsed()) {
            return ITEM_FOOTER;
        } else if (position == 0 && isHeaderUsed()) {
            return ITEM_HEADER;
        } else {
            return ITEM_REGULAR;
        }
    }

    public int getRealItemCount() {
        if (isDataValid && mCursor != null) {
            if (isFooterUsed()) {
                return FOOTER_POSITION = mCursor.getCount();
            }
            return mCursor.getCount();
        }
        return 0;
    }

    public Cursor swapCursor(Cursor newCursor) {
        int itemCount = 0;
        int positionStart = 0;

        if (newCursor == mCursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        if (oldCursor != null) {
            if (!isHeaderUsed()) {
                positionStart = oldCursor.getCount();
            }
            if (mCursorObserver != null) {
                oldCursor.unregisterDataSetObserver(mCursorObserver);
            }
        }
        mCursor = newCursor;
        if (newCursor != null) {
            mRowIDColumn = newCursor.getColumnIndexOrThrow(BaseColumns._ID);
            isDataValid = true;
            if (mCursor.getCount() == 0) {
                notifyItemRangeChanged(itemCount, mStandardDose);
                mPreviousItemCount = 0;
            } else {
                if (mPreviousItemCount != 0) {
                    itemCount = mCursor.getCount() - mPreviousItemCount;
                    mPreviousItemCount += itemCount;
                } else {
                    itemCount = mStandardDose = mPreviousItemCount = mCursor.getCount();
                }

                if (mCursorObserver != null) {
                    newCursor.registerDataSetObserver(mCursorObserver);
                }
                if (mHeader != null) {
                    notifyItemRangeInserted(positionStart, itemCount);
                } else {
                    notifyItemRangeInserted(positionStart, itemCount);
                }
            }
            //CardView
        } else {
            mRowIDColumn = -1;
            isDataValid = false;
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    public abstract void onBindViewHolder(VH viewHolder, Cursor cursor, int position);


    public void addFooterView(View view) {
        mFooter = view;
    }

    public void addHeaderView(View view) {
        mHeader = view;
    }

    public void removeFooterView(View view) {
        if (mFooter == view) {
            mFooter = null;
        }
    }

    public void removeHeaderView(View view) {
        if (mHeader == view) {
            mHeader = null;
        }
    }

    public View getFooter() {
        return mFooter;
    }

    public View getHeader() {
        return mHeader;
    }

    public int getFooterViewsCount() {
        return mFooter == null ? 0 : 1;
    }

    public int getHeaderViewsCount() {
        return mHeader == null ? 0 : 1;
    }

    private boolean isFooterUsed() {
        return mFooter != null;
    }

    private boolean isHeaderUsed() {
        return mHeader != null;
    }

    public int getStandardDose() {
        return mStandardDose;
    }


    protected RecyclerView.ViewHolder getFooterViewHolder(ViewGroup viewGroup) {
        return null;
    }

    protected RecyclerView.ViewHolder getHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }


    //Callback
    public void setOnClickListener(OnRecyclerClickListener onClickListener) {
        mOnRecyclerClickListener = onClickListener;
    }

    @Override
    public void onClick(View v) {
        mOnRecyclerClickListener.onRecyclerItemClicked(v);
    }

    //Internal service methods
    protected ImageLoader getImageLoader() {
        return mImageLoader;
    }

}
