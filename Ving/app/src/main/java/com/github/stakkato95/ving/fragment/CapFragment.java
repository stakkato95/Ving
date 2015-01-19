package com.github.stakkato95.ving.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.stakkato95.ving.R;

public class CapFragment extends Fragment {

    private static final String TEXT_PARAM = "text_param";

    public static CapFragment newInstance(String text) {
        CapFragment fragment = new CapFragment();
        Bundle args = new Bundle();
        args.putString(TEXT_PARAM,text);
        fragment.setArguments(args);
        return fragment;
    }

    public CapFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cap, container, false);
        ((TextView)view.findViewById(android.R.id.text1)).setText(getArguments().getString(TEXT_PARAM));
        return view;
    }

}
