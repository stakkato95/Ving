package com.github.stakkato95.ving.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.stakkato95.loader.ImageLoader;
import com.github.stakkato95.ving.CoreApplication;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.bo.Friend;
import com.github.stakkato95.ving.database.DbInsertingThread;
import com.github.stakkato95.ving.database.VkDataBaseOpenHelper;
import com.github.stakkato95.ving.manager.DataManager;
import com.github.stakkato95.ving.processing.FriendArrayProcessor;
import com.github.stakkato95.ving.source.VkDataSource;

import java.net.UnknownHostException;
import java.util.List;

public class FriendsFragment extends ListFragment implements DataManager.Callback<List<Friend>> {

    private ListView mListView;
    private ArrayAdapter<Friend> mArrayAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;
    private Button mRetryButton;
    private View mFooter;
    private boolean isPaginationEnabled = true;

    private ImageLoader mImageLoader;
    private static FriendArrayProcessor mFriendArrayProcessor;
    private static VkDataSource mVkDataSource;
    private List<Friend> mDataSet;

    private int DEFAULT_IMAGE_RESOURCE = -1;

    static {
        mFriendArrayProcessor = new FriendArrayProcessor();
    }

    public FriendsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //20 Mb DiskCache
        mImageLoader = ImageLoader.get(getActivity());
        mVkDataSource = VkDataSource.get(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        mListView = (ListView)view.findViewById(android.R.id.list);
        mFooter = View.inflate(getActivity(), R.layout.view_footer, null);
        mListView.addFooterView(mFooter);


        mProgressBar = (ProgressBar)view.findViewById(android.R.id.progress);
        mErrorTextView = (TextView)view.findViewById(R.id.loading_error_text_view);
        mRetryButton = (Button)view.findViewById(R.id.retry_loading_button);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

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



    private void resetFooter() {
        if(mFooter != null) {
            if (isPaginationEnabled) {
                mFooter.setVisibility(View.VISIBLE);
            } else {
                mFooter.setVisibility(View.GONE);
            }
        }
    }

    private void reloadData(List<Friend> data) {
        if(data != null && data.size() == Api.FRIENDS_GET_COUNT) {
            isPaginationEnabled = true;
            mListView.addFooterView(mFooter);
        } else {
            isPaginationEnabled = false;
            mListView.removeFooterView(mFooter);
        }

        if (data != null) {
            mDataSet.addAll(data);
        }
        mArrayAdapter.notifyDataSetChanged();
    }

    private static int getRealAdapterCount(ListAdapter adapter) {
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

    private String getRequestUrl(int count, int offset) {
        return Api.FRIENDS_GET + "&count="+ count+"&offset="+offset;
    }



    //DataManager.Callbacks & methods

    public void loadData() {
        DataManager.loadData(FriendsFragment.this, getRequestUrl(Api.FRIENDS_GET_COUNT, Api.FRIENDS_GET_DEFAULT_OFFSET), mVkDataSource, mFriendArrayProcessor);
    }

    @Override
    public void onDataLoadStart() {
        if (mSwipeRefreshLayout != null && !mSwipeRefreshLayout.isRefreshing()) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDone(List<Friend> data) {
        if(mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        mProgressBar.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.GONE);
        mRetryButton.setVisibility(View.GONE);

        if(data == null || data.isEmpty()) {
            mErrorTextView.setVisibility(View.VISIBLE);
            mRetryButton.setVisibility(View.VISIBLE);
        }

        DbInsertingThread insertingThread = new DbInsertingThread(getActivity(), data);
        insertingThread.start();

        if(mArrayAdapter == null) {
            mDataSet = data;
            mArrayAdapter = new ArrayAdapter<Friend>(getActivity(), R.layout.adapter_friend, android.R.id.text1, data) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = View.inflate(getActivity(), R.layout.adapter_friend, null);
                    }

                    Friend friend = getItem(position);
                    ((TextView) convertView.findViewById(android.R.id.text1)).setText(friend.getFullName());
                    convertView.setTag(friend.getId());

                    //shows is user online
                    ImageView onlineImageView = (ImageView)convertView.findViewById(android.R.id.icon1);
                    onlineImageView.setImageResource(DEFAULT_IMAGE_RESOURCE);
                    if(friend.isOnlineMobile()) {
                        onlineImageView.setImageResource(R.drawable.ic_friend_online_mobile);
                    } else if (friend.isOnline()){
                        onlineImageView.setImageResource(R.drawable.ic_friend_online_computer);
                    }

                    final ImageView imageView = (ImageView) convertView.findViewById(android.R.id.icon);
                    final String photoUrl = friend.getPhoto();

                    if (!TextUtils.isEmpty(photoUrl)) {
                        mImageLoader.obtainImage(imageView, photoUrl);
                    }

                    return convertView;
                }
            };
            mListView.setAdapter(mArrayAdapter);
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                private int previousTotalItemCount = 0;
                private int visibleThreshold = 5;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    ListAdapter adapter = view.getAdapter();
                    int currentAdapterCount = getRealAdapterCount(adapter);

                    if(currentAdapterCount == 0) {
                        return;
                    }
                    if (previousTotalItemCount != totalItemCount && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        previousTotalItemCount = totalItemCount;
                        DataManager.loadData(new DataManager.Callback<List<Friend>>() {
                            @Override
                            public void onDataLoadStart() {

                            }

                            @Override
                            public void onDone(List<Friend> data) {
                                reloadData(data);
                                resetFooter();
                            }

                            @Override
                            public void onError(Exception e) {
                                resetFooter();
                            }
                        }, getRequestUrl(Api.FRIENDS_GET_COUNT, currentAdapterCount), mVkDataSource, mFriendArrayProcessor);
                    }
                }
            });

            //pagination logic
            if (data != null && data.size() == Api.FRIENDS_GET_DEFAULT_OFFSET) {
                isPaginationEnabled = true;
            } else {
                isPaginationEnabled = false;
            }
        } else {
            mDataSet.clear();
            reloadData(data);
        }
        resetFooter();
    }

    @Override
    public void onError(Exception e) {
        mSwipeRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);

        if (UnknownHostException.class.isInstance(e) && mDataSet == null) {
            mErrorTextView.setVisibility(View.VISIBLE);
            mRetryButton.setVisibility(View.VISIBLE);
        } else {
            mErrorTextView.setText("ERROR\n" + e.getLocalizedMessage());
        }
    }

}
