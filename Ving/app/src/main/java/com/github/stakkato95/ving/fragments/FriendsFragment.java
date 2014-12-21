package com.github.stakkato95.ving.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.stakkato95.loader.ImageLoader;
import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.bo.Friend;
import com.github.stakkato95.ving.manager.DataManager;
import com.github.stakkato95.ving.processing.BitmapProcessor;
import com.github.stakkato95.ving.source.HttpDataSource;

import java.util.ArrayList;

public class FriendsFragment extends ListFragment {

    private static final String DATA_PARAM = "data_param";
    private ImageLoader mImageLoader;

    //TODO delete it
    private OnFragmentInteractionListener mListener;

    public static FriendsFragment newInstance(ArrayList<Friend> data) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(DATA_PARAM, data);
        fragment.setArguments(args);
        return fragment;
    }

    public FriendsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //20 Mb DiskCache
        mImageLoader = new ImageLoader(getActivity(), 1024 * 1024 * 20, R.drawable.image_loading, R.drawable.image_loading_error);

        ArrayList<Friend> friendArrayList = getArguments().getParcelableArrayList(DATA_PARAM);

        setListAdapter(new ArrayAdapter<Friend>(getActivity(), R.layout.adapter_friend, android.R.id.text1, friendArrayList) {
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
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
