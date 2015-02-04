package com.github.stakkato95.ving.fragment;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.activity.MainActivity;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.loader.DataLoader;
import com.github.stakkato95.ving.processor.DBProcessor;
import com.github.stakkato95.ving.processor.UserDBProcessor;
import com.github.stakkato95.ving.source.VkDataSource;

/**
 * Created by Artyom on 04.02.2015.
 */
public class UserFragment extends Fragment {

    private static final int CURSOR_LOADER = 0;
    private LoaderManager mLoaderManager;
    protected ContentResolver mContentResolver;
    protected Uri mUri;

    protected DataLoader mDataLoader;
    protected DBProcessor mProcessor;
    protected VkDataSource mVkDataSource;
    protected String mRequestUrl;

    public static UserFragment newInstance(String userId) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_REQUEST_FIELD, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentResolver = getActivity().getContentResolver();
        mLoaderManager = getActivity().getSupportLoaderManager();
        mDataLoader = new DataLoader();
        mVkDataSource = VkDataSource.get(getActivity());
        mProcessor = new UserDBProcessor(getActivity());
        String userId = getArguments().getString(MainActivity.KEY_REQUEST_FIELD);
        //todo builder
        mRequestUrl = Api.getUser() + userId + Api.FIELD_USER_FIELDS + Api._BIRTHDAY + "," + Api._PHONE_MOBILE + "," + Api._PHONE + "," +
                Api._ONLINE + "," + Api._PHOTO_100 + "," + Api._SEX + "," + Api._SITE + "," + Api._SKYPE + "," + Api._STATUS + ",";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        return view;
    }
}