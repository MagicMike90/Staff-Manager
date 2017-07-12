package com.sanitation.app.fragments.staffmanagement.staff;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sanitation.app.Constants;
import com.sanitation.app.R;

import org.json.JSONException;
import org.json.JSONObject;

public class StaffFilterFragment extends DialogFragment {
    private String filter_name = "";
    private String filter_department = Constants.DEPARTMENT[0];
    private String filter_online = Constants.ONLINE_STATUS[0];

    private OnCloseListener mListener;

    EditText StaffNameView;
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

        StaffListFragment frag = (StaffListFragment)getTargetFragment();
        onAttachToParentFragment(frag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_filter, container, false);

        StaffNameView = (EditText)view.findViewById(R.id.staff_name);

        //department selection
        MaterialSpinner department_spinner = (MaterialSpinner) view.findViewById(R.id.spinner_department);
        department_spinner.setItems(Constants.DEPARTMENT);
        department_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                filter_department = item;
//                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

        //user online status selection
        MaterialSpinner status_spinner = (MaterialSpinner) view.findViewById(R.id.spinner_online);
        status_spinner.setItems(Constants.ONLINE_STATUS);
        status_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
//                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                filter_online = item;
            }
        });

        Button staff_filter_confirm_button = (Button) view.findViewById(R.id.staff_filter_confirm_button);
        staff_filter_confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // user staff_name
                filter_name = StaffNameView.getText().toString();

                JSONObject result = new JSONObject();
                try {
                    result.put("filter_name",filter_name);
                    result.put("filter_department",filter_department);
                    result.put("filter_online",filter_online);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mListener.OnCloseListener(result);
                dismiss();
            }
        });

        return view;
    }

    public void onAttachToParentFragment(Fragment fragment) {
        try {
            mListener = (OnCloseListener) fragment;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnPlayerSelectionSetListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnCloseListener {
        // TODO: Update argument type and staff_name
        void OnCloseListener(JSONObject result);
    }
}
