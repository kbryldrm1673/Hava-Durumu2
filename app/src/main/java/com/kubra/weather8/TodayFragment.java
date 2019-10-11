package com.kubra.weather8;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.kubra.weather8.Common.Common;
import com.kubra.weather8.Model.WeatherResult;
import com.kubra.weather8.Retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import java.io.*;


public class TodayFragment extends Fragment {


    ImageView img_weather;
    TextView txt_city_name;
    TextView txt_humidity;
    TextView txt_sunrise;
    TextView txt_sunset;
    TextView txt_pressure;
    TextView txt_temperature;
    TextView txt_description;
    TextView txt_date_time;
    TextView txt_wind;
    TextView txt_geo_coord;
    LinearLayout weather_panel;
    ProgressBar loading;

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;






    static TodayFragment instance;

    public static TodayFragment getInstance() {
        if (instance == null)
            instance = new TodayFragment();
        return instance;
    }
    public TodayFragment() {

        compositeDisposable=new CompositeDisposable();
        Retrofit retrofit= RetrofitClient.getInstance();
        mService=retrofit.create(IOpenWeatherMap.class);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView= inflater.inflate(R.layout.fragment_today, container, false);

        img_weather=(ImageView) itemView.findViewById(R.id.img_weather);
        txt_city_name=(TextView) itemView.findViewById(R.id.txt_city_name);
        txt_humidity=(TextView) itemView.findViewById(R.id.txt_humidity);
        txt_sunrise=(TextView) itemView.findViewById(R.id.txt_sunrise);
        txt_sunset=(TextView) itemView.findViewById(R.id.txt_sunset);
        txt_pressure=(TextView) itemView.findViewById(R.id.txt_pressure);
        txt_temperature=(TextView) itemView.findViewById(R.id.txt_temprature);
        txt_description=(TextView) itemView.findViewById(R.id.txt_description);
        txt_date_time=(TextView) itemView.findViewById(R.id.txt_data_time);
        txt_wind=(TextView)itemView.findViewById(R.id.txt_wind);
        txt_geo_coord=(TextView) itemView.findViewById(R.id.txt_geo_cood);

        weather_panel=(LinearLayout) itemView.findViewById(R.id.weather_panel);
        loading=(ProgressBar) itemView.findViewById(R.id.loading);


        getWeatherInformation();

        return  itemView;
    }

    private void getWeatherInformation() {

        //Log.d("todayFragment " ,Common.current_location.getLatitude());
        compositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                               @Override
                               public void accept(WeatherResult weatherResult) throws Exception {

                                   Picasso.get().load(new StringBuilder("https://openweathermap.org/img/wn/")
                                           .append(weatherResult.getWeather().get(0).getIcon())
                                           .append(".png").toString()).into(img_weather);

                                   txt_city_name.setText(weatherResult.getName());
                                   txt_description.setText(new StringBuilder(" ")//Değiştirdim
                                           .append(weatherResult.getName()).toString());


                                   txt_temperature.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp())).append("°C").toString());

                                   txt_date_time.setText(Common.convertUnixToDate(weatherResult.getDt()));

                                   txt_pressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append("hpa").toString());
                                   txt_humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append("%").toString());
                                   txt_sunrise.setText(Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
                                   txt_sunset.setText(Common.convertUnixToHour(weatherResult.getSys().getSunset()));
                                   txt_geo_coord.setText(new StringBuilder("[").append(weatherResult.getCoord().toString()).append("]").toString());


                                   weather_panel.setVisibility(View.VISIBLE);
                                   loading.setVisibility(View.GONE);

                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(getActivity(),""+throwable.getMessage(),Toast.LENGTH_SHORT).show();
                               }
                           }

                ));
    }


}