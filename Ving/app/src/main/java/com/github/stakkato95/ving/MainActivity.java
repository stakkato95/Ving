package com.github.stakkato95.ving;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.stakkato95.ving.bo.Friend;
import com.github.stakkato95.ving.bo.NoteGsonModel;
import com.github.stakkato95.ving.manager.DataManager;
import com.github.stakkato95.ving.processing.BitmapProcessor;
import com.github.stakkato95.ving.processing.FriendArrayProcessor;
import com.github.stakkato95.ving.processing.Processor;
import com.github.stakkato95.ving.source.DataSource;
import com.github.stakkato95.ving.source.HttpDataSource;
import com.github.stakkato95.ving.source.VkDataSource;

import org.w3c.dom.Text;

import java.util.List;


public class MainActivity extends ActionBarActivity implements DataManager.Callback<List<Friend>> {

    private FriendArrayProcessor mFriendArrayProcessor;
    private VkDataSource mVkDataSource;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;

    private AdapterView mAdapterView;
    private ArrayAdapter mArrayAdapter;
    private List<Friend> mRefreshData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFriendArrayProcessor = new FriendArrayProcessor();
        mVkDataSource = new VkDataSource();

        mProgressBar = (ProgressBar)findViewById(android.R.id.progress);
        mAdapterView = (AbsListView)findViewById(android.R.id.list);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshListView(mVkDataSource, mFriendArrayProcessor);
            }
        });
        refreshListView(mVkDataSource, mFriendArrayProcessor);
    }

    private void refreshListView(HttpDataSource dataSource, FriendArrayProcessor processor) {
        DataManager.loadData(MainActivity.this, getRequestUrl(), dataSource, processor);
    }

    private String getRequestUrl() {
        return Api.FRIENDS_GET;
    }


    @Override
    public void onDataLoadStart() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mProgressBar.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDone(List<Friend> data) {
        if(mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mProgressBar.setVisibility(View.GONE);

        if(mArrayAdapter == null) {
            mRefreshData = data;
            mArrayAdapter = new ArrayAdapter<Friend>(this, R.layout.adapter_friend, android.R.id.text1, data) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = View.inflate(MainActivity.this, R.layout.adapter_friend, null);
                    }
                    Friend friend = getItem(position);
                    ((TextView)convertView.findViewById(android.R.id.text1)).setText(friend.getName());
                    ((TextView)convertView.findViewById(android.R.id.text2)).setText(friend.getNickname());
                    convertView.setTag(friend.getId());
                    final ImageView imageView = (ImageView)convertView.findViewById(android.R.id.icon);
                    final String photoUrl = friend.getPhoto();
                    imageView.setImageBitmap(null);
                    imageView.setTag(photoUrl);

                    if (!TextUtils.isEmpty(photoUrl)) {
                        DataManager.loadData(new DataManager.Callback<Bitmap>() {
                            @Override
                            public void onDataLoadStart() {

                            }

                            @Override
                            public void onDone(Bitmap data) {
                                if (photoUrl.equals(imageView.getTag())) {
                                    imageView.setImageBitmap(data);
                                }
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        },
                                photoUrl, HttpDataSource.get(MainActivity.this), new BitmapProcessor());
                    }

                    return convertView;
                }
            };
            mAdapterView.setAdapter(mArrayAdapter);

            mAdapterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, FriendDetailsActivity.class);
                    Friend friend = (Friend)mArrayAdapter.getItem(position);
                    NoteGsonModel note = new NoteGsonModel(friend.getId(), friend.getFirstName(), friend.getLastName());
                    intent.putExtra("item", note);
                    startActivity(intent);
                }
            });
        } else {
            mRefreshData.clear();
            mRefreshData.addAll(data);
            mArrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(Exception e) {
        mProgressBar.setVisibility(View.GONE);
        TextView errorView = (TextView)findViewById(R.id.error_text);
        errorView.setText("ERROR\n" + e.getLocalizedMessage());
    }
}
