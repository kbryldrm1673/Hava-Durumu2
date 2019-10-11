package com.kubra.weather8;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import com.kubra.weather8.Adapter.WeatherForecastAdapter;
import com.kubra.weather8.Common.Common;
import com.kubra.weather8.Model.Main;
import com.kubra.weather8.Model.MyList;
import com.kubra.weather8.Model.Weather;
import com.kubra.weather8.Model.WeatherForecastResult;
import com.kubra.weather8.Model.WeatherResult;
import com.kubra.weather8.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;



public class ForeCastFragment extends Fragment {

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;

    TextView txt_city_name;
    ImageView img_weather; //ekle
    TextView txt_temprature; //ekle
    TextView txt_description; //ekle
    TextView txt_data_time;

    RecyclerView recycler_forecast;

    WeatherForecastAdapter adapter;
    WeatherForecastResult weatherForecastResult;


    static ForeCastFragment instance;

    public static ForeCastFragment getInstance() {
        if (instance == null)
            instance = new ForeCastFragment();
        return instance;
    }


    public ForeCastFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View itemView = inflater.inflate(R.layout.fragment_fore_cast, container, false);
        txt_city_name = (TextView) itemView.findViewById(R.id.txt_city_name);
        img_weather=(ImageView) itemView.findViewById(R.id.img_weather); //ekledim
        txt_temprature=(TextView) itemView.findViewById(R.id.txt_temprature);  //ekledim
        txt_description=(TextView) itemView.findViewById(R.id.txt_description); //ekledim
        txt_data_time=(TextView) itemView.findViewById(R.id.txt_data_time);


        getForecastWeatherInformation();


        return itemView;
    }


    private void getForecastWeatherInformation() {

        compositeDisposable.add(mService.getForecastWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID,
                "units")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResult>() {
                    @Override
                    public void accept(WeatherForecastResult weatherForecastResult) throws Exception {
                        displayForecastWeather(weatherForecastResult);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("Error", "" + throwable.getMessage());
                    }
                })
        );
    }


    private void displayForecastWeather(WeatherForecastResult weatherForecastResult) {
        this.weatherForecastResult = weatherForecastResult;
        txt_city_name.setText(new StringBuilder(weatherForecastResult.city.name));


        //txt_description.setText(new StringBuilder());


        recycler_forecast = (RecyclerView) getView().findViewById(R.id.recycler_forecast);
        recycler_forecast.setHasFixedSize(true);
        recycler_forecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new WeatherForecastAdapter(getContext(), weatherForecastResult);
        recycler_forecast.setAdapter(adapter);



    }

}

