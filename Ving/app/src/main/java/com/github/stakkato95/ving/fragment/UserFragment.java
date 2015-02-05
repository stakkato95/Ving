package com.github.stakkato95.ving.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.stakkato95.imageloader.ImageLoader;
import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.activity.MainActivity;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.bo.Photo;
import com.github.stakkato95.ving.bo.User;
import com.github.stakkato95.ving.loader.DataLoader;
import com.github.stakkato95.ving.processor.PhotosProcessor;
import com.github.stakkato95.ving.processor.Processor;
import com.github.stakkato95.ving.processor.UserProcessor;
import com.github.stakkato95.ving.source.VkDataSource;
import com.github.stakkato95.ving.utils.MetricsUtils;
import com.github.stakkato95.ving.utils.ProcessingUtils;

import java.util.Map;

/**
 * Created by Artyom on 04.02.2015.
 */
public class UserFragment extends Fragment implements DataLoader.Callback<User[]> {

    private ImageView mBackground;
    private ImageView mUserImage;
    private ImageView mOnlineImage;
    private TextView mUserName;
    private TextView mUserState;
    private TextView mUserStatus;

    private HorizontalScrollView mScrollContainer;
    private LinearLayout mCountersLinearContainer;
    private LinearLayout mPhotosLinearContainer;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private DataLoader mDataLoader;
    private Processor mProcessor;
    private VkDataSource mVkDataSource;
    private ImageLoader mImageLoader;
    private String mRequestUrl;
    private String mPhotosRequestUrl;

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
        mDataLoader = new DataLoader();
        mVkDataSource = VkDataSource.get(getActivity());
        mProcessor = new UserProcessor();
        mImageLoader = ImageLoader.get(getActivity());
        String userId = getArguments().getString(MainActivity.KEY_REQUEST_FIELD);
        //todo builder
        mRequestUrl = Api.getUser() + userId + "&" + Api.FIELD_USER_FIELDS + Api._BIRTHDAY + "," + Api._PHONE_MOBILE + "," + Api._PHONE + "," +
                Api._ONLINE + "," + Api._PHOTO_200 + "," + Api._PHOTO_MAX + "," + Api._SEX + "," + Api._SITE + "," + Api._SKYPE + "," +
                Api._STATUS + "," + Api._LAST_SEEN + "," + Api._COUNTERS;

        mPhotosRequestUrl = Api.getPhotos() + userId + "&" + Api.FIELD_ALBUM_ID + Api._ALBUM_PROFILE + "&" + Api.FIELD_PHOTOS_SORT_ORDER + Api.PHOTOS_SORT_ANTICHRONOLOGICAL + "&" + Api.FIELD_PHOTO_SIZES + Api.PHOTOS_SPECIAL_SIZES;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        mBackground = (ImageView) view.findViewById(R.id.background_image);
        mUserImage = (ImageView) view.findViewById(R.id.user_image);
        mOnlineImage = (ImageView) view.findViewById(R.id.online_image);

        mScrollContainer = (HorizontalScrollView) view.findViewById(R.id.counters_scroll_container);
        mCountersLinearContainer = (LinearLayout)view.findViewById(R.id.counters_linear_container);
        mPhotosLinearContainer = (LinearLayout)view.findViewById(R.id.photos_linear_container);

        mUserName = (TextView) view.findViewById(R.id.user_name);
        mUserState = (TextView) view.findViewById(R.id.user_state);
        mUserStatus = (TextView) view.findViewById(R.id.user_status);

        loadData();
        return view;
    }

    private void loadData() {
        mDataLoader.getDataAsynch(UserFragment.this, mRequestUrl, mVkDataSource, mProcessor);
        mDataLoader.getDataAsynch(new DataLoader.Callback<Photo[]>() {

            @Override
            public void onLoadingStarted() {

            }

            @Override
            public void onLoadingFinished(Photo[] photos) {
                setPhotos(photos);
            }

            @Override
            public void onLoadingError(Exception e) {

            }

        },mPhotosRequestUrl, mVkDataSource, new PhotosProcessor());
    }

    private void setOnlineMode(User user) {
        int onlineMode = user.getOnlineMode();
        if (onlineMode != Api.USER_OFFLINE) {
            mUserState.setText(getResources().getString(Api.USER_ONLINE_STRING));
            if (onlineMode == Api.USER_ONLINE_MOBILE) {
                mOnlineImage.setVisibility(View.VISIBLE);
                mOnlineImage.setImageBitmap(null);
                mOnlineImage.setImageResource(R.drawable.ic_friend_online_mobile);
            } else {
                mOnlineImage.setVisibility(View.VISIBLE);
                mOnlineImage.setImageBitmap(null);
                mOnlineImage.setImageResource(R.drawable.ic_friend_online_computer);
            }
        } else {
            String stringDate = ProcessingUtils.getDate(user.getLastSeen());
            String statePrefix = getResources().getString(Api.USER_STATE);
            if (user.getSex() == Api.USER_SEX_WOMAN) {
                statePrefix += "a";
            }
            mUserState.setText(statePrefix + " " + stringDate);
        }
    }

    private void setStatus(User user) {
        String status = user.getStatus();
        if (status.equals(Api.EMPTY_STRING)) {
            mUserStatus.setVisibility(View.GONE);
        } else {
            mUserStatus.setText(user.getStatus());
        }
    }

    private void setCounters(User user) {
        if (mUserStatus.getVisibility() == View.GONE) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mScrollContainer.getLayoutParams();
            params.removeRule(RelativeLayout.BELOW);
            params.addRule(RelativeLayout.BELOW, R.id.user_image);
            mScrollContainer.setLayoutParams(params);
        }

        LinearLayout.LayoutParams counterParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        counterParams.setMargins(16, 16, 16, 0);

        for (Map.Entry<Integer,String> pair : user.getCountersMap().entrySet()) {
            int sizeText = MetricsUtils.spToPx(12, getActivity());
            int sizeCount = MetricsUtils.spToPx(16, getActivity());
            String text = getResources().getString(pair.getKey()) + "\n";
            int border = text.length();
            text += pair.getValue();

            Spannable counter = new SpannableString(text);
            counter.setSpan(new StyleSpan(Typeface.BOLD), border, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            counter.setSpan(new AbsoluteSizeSpan(sizeText), 0, border, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            counter.setSpan(new AbsoluteSizeSpan(sizeCount), border, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            counter.setSpan(new AlignmentSpan() {
                @Override
                public Layout.Alignment getAlignment() {
                    return Layout.Alignment.ALIGN_CENTER;
                }
            }, 0, counter.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            TextView tv = new TextView(getActivity());
            tv.setLayoutParams(counterParams);
            tv.setText(counter);
            mCountersLinearContainer.addView(tv);
        }


    }

    private void setPhotos(Photo[] photos) {
        if (photos.length != 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(2, 0, 2, 0);

            for (Photo photo : photos) {
                ImageView imageView = new ImageView(getActivity());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(params);
                mImageLoader.toView(imageView).setCircled(false).byUrl(photo.getPhoto200());
                mPhotosLinearContainer.addView(imageView);
            }
        }
    }

    //DataLoader
    @Override
    public void onLoadingFinished(User[] users) {
        User user = users[0];

        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(user.getFullName());

        mImageLoader.toView(mUserImage).setCircled(true).byUrl(user.getPhoto200());
        mImageLoader.toView(mBackground).setCircled(false).byUrl(user.getPhotoMax());
        mUserName.setText(user.getFullName());






        setOnlineMode(user);
        setStatus(user);
        setCounters(user);
    }

    @Override
    public void onLoadingStarted() {

    }

    @Override
    public void onLoadingError(Exception e) {

    }
}