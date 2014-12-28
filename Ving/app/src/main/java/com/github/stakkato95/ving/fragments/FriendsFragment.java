package com.github.stakkato95.ving.fragments;

import android.app.Activity;
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
import com.github.stakkato95.ving.Api;
import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.bo.Friend;
import com.github.stakkato95.ving.manager.DataManager;
import com.github.stakkato95.ving.processing.FriendArrayProcessor;
import com.github.stakkato95.ving.source.VkDataSource;

import java.net.UnknownHostException;
import java.util.List;

public class FriendsFragment extends ListFragment implements DataManager.Callback<List<Friend>> {

    private static String REQUEST_CODE = "request_code";
    private ListView mListView;
    private ArrayAdapter<Friend> mArrayAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;
    private Button mRetryButton;
    private View mFooter;
    private boolean isPaginationEnabled = true;

    private static final int FRIENDS_GET_COUNT= 20;
    private static final int FRIENDS_GET_DEFAULT_OFFSET = 0;

    private ImageLoader mImageLoader;
    private static FriendArrayProcessor mFriendArrayProcessor;
    private static VkDataSource mVkDataSource;
    private List<Friend> mDataSet;


    //TODO for strip
    private boolean isDataLoaded = false;

    //TODO delete it
    private OnFragmentInteractionListener mListener;

    static {
        mVkDataSource = new VkDataSource();
        mFriendArrayProcessor = new FriendArrayProcessor();
    }


    public FriendsFragment() {
    }

    public static FriendsFragment newInstance(int request) {
        Bundle args = new Bundle();
        args.putInt(REQUEST_CODE, request);

        FriendsFragment fragment = new FriendsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //20 Mb DiskCache
        mImageLoader = new ImageLoader(getActivity(), 1024 * 1024 * 20, R.drawable.image_loading, R.drawable.image_loading_error);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && !isDataLoaded) {

            isDataLoaded = true;
        }
    }

    //TODO check & remove useless methods

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //TODO remove it
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
//            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
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
        if(data != null && data.size() == FRIENDS_GET_COUNT) {
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
        return Api.FRIENDS_GET  + "&count="+ count+"&offset="+offset;
    }



    //DataManager.Callbacks & methods

    public void loadData() {
        DataManager.loadData(FriendsFragment.this, getRequestUrl(FRIENDS_GET_COUNT, FRIENDS_GET_DEFAULT_OFFSET), mVkDataSource, mFriendArrayProcessor);
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

        if(mArrayAdapter == null) {
            mDataSet = data;
            mArrayAdapter = new ArrayAdapter<Friend>(getActivity(), R.layout.adapter_friend, android.R.id.text1, data) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = View.inflate(getActivity(), R.layout.adapter_friend, null);
                    }

                    Friend friend = getItem(position);
                    ((TextView) convertView.findViewById(android.R.id.text1)).setText(friend.getName());
                    ((TextView) convertView.findViewById(android.R.id.text2)).setText(friend.getNickname());
                    convertView.setTag(friend.getId());

                    final ImageView imageView = (ImageView) convertView.findViewById(android.R.id.icon);
                    final String photoUrl = friend.getPhoto();
                    imageView.setImageBitmap(null);
                    imageView.setTag(photoUrl);

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
                        }, getRequestUrl(FRIENDS_GET_COUNT, currentAdapterCount), mVkDataSource, mFriendArrayProcessor);
                    }
                }
            });

            //pagination logic
            if (data != null && data.size() == FRIENDS_GET_DEFAULT_OFFSET) {
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
