package com.sanitation.app.staff;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sanitation.app.Constants;
import com.sanitation.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnCloseListener} interface
 * to handle interaction events.
 * Use the {@link StaffFilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaffFilterFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String filter_name;
    private String filter_department;
    private String filter_online;
    private OnCloseListener mListener;

    public StaffFilterFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static StaffFilterFragment newInstance() {
        StaffFilterFragment fragment = new StaffFilterFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_filter, container, false);
        // Inflate the layout for this fragment
        MaterialSpinner spinner = (MaterialSpinner) view.findViewById(R.id.spinner_department);
//        spinner.setDropdownHeight(500);
        spinner.setItems(Constants.DEPARTMENT);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

        MaterialSpinner status_spinner = (MaterialSpinner) view.findViewById(R.id.spinner_online);
        status_spinner.setItems(Constants.ONLINE_STATUS);

        Button staff_filter_confirm_button = (Button) view.findViewById(R.id.staff_filter_confirm_button);
        staff_filter_confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

        return view;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.OnCloseListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnCloseListener) {
//            mListener = (OnCloseListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnCloseListener");
//        }

//        try {
//            mListener = (OnCloseListener) getTargetFragment();
//        } catch (ClassCastException e) {
//            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
//        }

//        if (getTargetFragment() instanceof OnCloseListener) {
//            mListener = (OnCloseListener) getTargetFragment();
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnCloseListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCloseListener {
        // TODO: Update argument type and name
        void OnCloseListener(Uri uri);
    }
}
