package com.sanitation.app.staffsignin.step;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Michael on 4/17/17.
 */

public abstract class BaseFragment extends Fragment {
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        return view;
    }


    protected abstract int getLayoutResId();
}
