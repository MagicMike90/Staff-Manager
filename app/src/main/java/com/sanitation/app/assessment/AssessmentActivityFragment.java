package com.sanitation.app.assessment;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;

import com.hrules.charter.CharterLine;
import com.hrules.charter.CharterXLabels;
import com.sanitation.app.R;

import java.util.Random;


/**
 * A placeholder fragment containing a simple view.
 */
public class AssessmentActivityFragment extends Fragment {
    private CharterLine charterLine;
    private CharterXLabels charterBarLabelX;
    private float[] values;
    private static final int DEFAULT_ITEMS_COUNT = 15;
    private static final int DEFAULT_RANDOM_VALUE_MIN = 10;
    private static final int DEFAULT_RANDOM_VALUE_MAX = 100;

    public AssessmentActivityFragment() {
    }
    private float[] fillRandomValues(int length, int max, int min) {
        Random random = new Random();

        float[] newRandomValues = new float[length];
        for (int i = 0; i < newRandomValues.length; i++) {
            newRandomValues[i] = random.nextInt(max - min + 1) - min;
        }
        return newRandomValues;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assessment, container, false);

        values =
                fillRandomValues(DEFAULT_ITEMS_COUNT, DEFAULT_RANDOM_VALUE_MAX, DEFAULT_RANDOM_VALUE_MIN);
        Resources res = getResources();
        int[] barColors = new int[]{
                res.getColor(R.color.colorAccent), res.getColor(R.color.colorPrimary),
                res.getColor(R.color.colorPrimaryDark)
        };

        charterBarLabelX = (CharterXLabels) view.findViewById(R.id.charter_line_XLabel);
        charterBarLabelX.setStickyEdges(false);
        charterBarLabelX.setVisibilityPattern(new boolean[] { true, false });
        charterBarLabelX.setValues(values);


        charterLine = (CharterLine) view.findViewById(R.id.charter_line);
        charterLine.setValues(values);
        charterLine.setAnimInterpolator(new BounceInterpolator());
        charterLine.setShowGridLines(true);
        charterLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values = fillRandomValues(DEFAULT_ITEMS_COUNT, DEFAULT_RANDOM_VALUE_MAX,
                        DEFAULT_RANDOM_VALUE_MIN);
                charterLine.setValues(values);
                charterLine.show();
            }
        });
        return view;
    }
}
