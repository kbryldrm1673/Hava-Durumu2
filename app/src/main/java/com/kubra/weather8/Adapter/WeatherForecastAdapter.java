package com.kubra.weather8.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import com.kubra.weather8.Common.Common;
import com.kubra.weather8.Model.WeatherForecastResult;
import com.kubra.weather8.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.sql.CommonDataSource;


public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.MyViewHolder>{

    Context context;
    WeatherForecastResult weatherForecastResult;

    NumberFormat formatter = new DecimalFormat("#0.00");


    public WeatherForecastAdapter(Context context, WeatherForecastResult weatherForecastResult) {
        this.context = context;
        this.weatherForecastResult = weatherForecastResult;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.weather_forecast,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
               .append(weatherForecastResult.list.get(position).weather.get(0).getIcon())
                .append(".png").toString()).into(holder.img_weather);


        holder.txt_description.setText(new StringBuilder(weatherForecastResult.list.get(position).weather
                .get(0).getDescription()));



         String a=((String.valueOf(weatherForecastResult.list.get(position)
               .main.getTemp())));

         float b=Float.parseFloat(a);
         float c= (float) (b*(0.1));
         formatter.format(c);

         String d=String.valueOf(c);

         DecimalFormat df = new DecimalFormat("#.##");
         String dx = df.format(c);

         String e= "  °C ";
         holder.txt_temperature.setText(dx + e);


        holder.txt_data_time.setText(new StringBuilder(Common.convertUnixToDate(weatherForecastResult.list.get(position).dt))); //ekledim



    }


    @Override
    public int getItemCount() {

        return weatherForecastResult.list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_data_time, txt_description,txt_temperature;
        ImageView img_weather;
        public MyViewHolder(View itemView) {
            super(itemView);

            img_weather=(ImageView)itemView.findViewById(R.id.img_weather);
            txt_data_time=(TextView)itemView.findViewById(R.id.txt_date_time);
            txt_description=(TextView)itemView.findViewById(R.id.txt_description);
            txt_temperature=(TextView)itemView.findViewById(R.id.txt_temprature);

        }
    }
}