package com.orangex.amazingfellow.features.homepage.recent;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orangex.amazingfellow.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecentFragment extends Fragment {

    public RecentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
