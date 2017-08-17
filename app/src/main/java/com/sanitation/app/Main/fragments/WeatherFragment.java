package com.sanitation.app.Main.fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;
import com.sanitation.app.Constants;
import com.sanitation.app.R;
import com.sanitation.app.StaffManagement.staffmanagement.sign.StaffSignInAndOutActivity;
import com.sanitation.app.StaffManagement.staffmanagement.sign.step.StepInfoStorage;
import com.sanitation.app.CustomComponent.widget.Fab;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment implements WeatherSearch.OnWeatherSearchListener {
    private static String TAG = "WeatherFragment";
    private TextView report_time;
    private TextView weather;
    private TextView Temperature;
    private TextView wind;
    private TextView humidity;
    private WeatherSearchQuery mquery;
    private WeatherSearch mweathersearch;
    private LocalWeatherLive weatherlive;
    private LocalWeatherForecast weatherforecast;
    private List<LocalDayWeatherForecast> forecastlist = null;
    private String cityname = "沈阳市";//天气搜索的城市，可以写名称或adcode；

    private MaterialSheetFab materialSheetFab;
    private int statusBarColor;
    private OnClickListener mOnClickListener;

    public WeatherFragment() {
        // Required empty public constructor
    }


    private void searchforcastsweather() {
        mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_FORECAST);//检索参数为城市和天气类型，实时天气为1、天气预报为2
        mweathersearch = new WeatherSearch(getContext());
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索
    }

    private void searchLiveWeather() {
        mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_LIVE);//检索参数为城市和天气类型，实时天气为1、天气预报为2
        mweathersearch = new WeatherSearch(getContext());
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索
    }


    private void setupFab(View view) {
        mOnClickListener = new OnClickListener();

        Fab fab = (Fab) view.findViewById(R.id.fab);
        View sheetView = view.findViewById(R.id.fab_sheet);
        View overlay = view.findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.background_card);
        int fabColor = getResources().getColor(R.color.theme_accent);

        // Create material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        // Set material sheet event listener
        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Save current status bar color
                statusBarColor = getStatusBarColor();
                // Set darker status bar color to match the dim overlay
                setStatusBarColor(getResources().getColor(R.color.theme_primary_dark2));
            }

            @Override
            public void onHideSheet() {
                // Restore status bar color
                setStatusBarColor(statusBarColor);
            }
        });

        // Set material sheet item click listeners
        view.findViewById(R.id.fab_sheet_item_type_1).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.fab_sheet_item_type_2).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.fab_sheet_item_type_3).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.fab_sheet_item_type_4).setOnClickListener(mOnClickListener);
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(color);
        }
    }

    private int getStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getActivity().getWindow().getStatusBarColor();
        }
        return 0;
    }

    private class OnClickListener implements View.OnClickListener {
        int type = R.string.title_activity_supervisor_check_in;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_sheet_item_type_1:
                    type = R.string.title_activity_supervisor_check_in;
                    StepInfoStorage.getInstance().staff_role = Constants.StaffRole.SUPERVISOR;
                    StepInfoStorage.getInstance().type = Constants.SignType.SIGN_IN;
                    break;
                case R.id.fab_sheet_item_type_2:
                    type = R.string.title_activity_supervisor_check_out;
                    StepInfoStorage.getInstance().type = Constants.SignType.SIGN_OUT;
                    StepInfoStorage.getInstance().staff_role = Constants.StaffRole.SUPERVISOR;
                    break;
                case R.id.fab_sheet_item_type_3:
                    type = R.string.title_activity_cleaner_check_in;
                    StepInfoStorage.getInstance().type = Constants.SignType.SIGN_IN;
                    StepInfoStorage.getInstance().staff_role = Constants.StaffRole.CLEANER;

                    break;
                case R.id.fab_sheet_item_type_4:
                    type = R.string.title_activity_cleaner_check_out;
                    StepInfoStorage.getInstance().type = Constants.SignType.SIGN_OUT;
                    StepInfoStorage.getInstance().staff_role = Constants.StaffRole.CLEANER;
                    break;
            }
            Log.d(TAG, "click");
            materialSheetFab.hideSheet();

            Intent intent = new Intent(WeatherFragment.this.getContext(), StaffSignInAndOutActivity.class);
            intent.putExtra(StaffSignInAndOutActivity.CHECK_IN_AN_OUT_TYPE, type);
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_weather, container, false);
        TextView city = (TextView) v.findViewById(R.id.city);
        city.setText(cityname);
        report_time = (TextView) v.findViewById(R.id.report_time);
        weather = (TextView) v.findViewById(R.id.weather);
        Temperature = (TextView) v.findViewById(R.id.temp);
        wind = (TextView) v.findViewById(R.id.wind);
        humidity = (TextView) v.findViewById(R.id.humidity);


        searchLiveWeather();

        setupFab(v);

        return v;
    }

    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                weatherlive = weatherLiveResult.getLiveResult();
                report_time.setText(weatherlive.getReportTime() + " 发布");
                weather.setText(weatherlive.getWeather());
                Temperature.setText(weatherlive.getTemperature() + "°");
                wind.setText(weatherlive.getWindDirection() + "风     " + weatherlive.getWindPower() + "级");
                humidity.setText("湿度     " + weatherlive.getHumidity() + "%");
            } else {
                Log.d("time", "no data");
            }
        } else {
            Log.d("rCode", rCode + "");
        }
    }

    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {

    }
}
