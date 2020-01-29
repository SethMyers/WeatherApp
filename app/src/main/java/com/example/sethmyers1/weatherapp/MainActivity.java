package com.example.sethmyers1.weatherapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Queue;

//Application by Seth Myers B00680160
public class MainActivity extends AppCompatActivity {
    private EditText search_field;
    private Button search_btn;
    private TextView cityname;
    private TextView temp;
    private TextView min;
    private TextView max;
    private TextView currWeather;
    private TextView currDescription;
    private TextView humidity;
    private TextView clouds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search_field = (EditText)findViewById(R.id.search_city);
        search_btn = (Button) findViewById(R.id.change_city);
        cityname = (TextView)findViewById(R.id.city_name);
        temp = (TextView)findViewById(R.id.temperature);
        min = (TextView)findViewById(R.id.minTemp);
        max = (TextView)findViewById(R.id.maxTemp);
        currWeather = (TextView)findViewById(R.id.currWeather);
        currDescription = (TextView)findViewById(R.id.currDescription);
        humidity = (TextView)findViewById(R.id.humidity);
        clouds = (TextView)findViewById(R.id.clouds);
        search_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String url = "http://api.openweathermap.org/data/2.5/weather?q="+search_field.getText()+"&APPID=0cbf929bb80a42fa1dfe9747f970a5e2";
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jo = new JSONObject(response);
                                    // - 273.15 is conversion form  kelvin to celsius
                                    temp.setText((String.format("%.2f", (jo.getJSONObject("main").getDouble("temp"))-273.15))+" ℃");
                                    cityname.setText(jo.getString("name") + ", " + jo.getJSONObject("sys").getString("country"));
                                    min.setText("Min. "+(String.format("%.2f", (jo.getJSONObject("main").getDouble("temp_min"))-273.15))+" ℃");
                                    max.setText("Max. "+(String.format("%.2f", (jo.getJSONObject("main").getDouble("temp_max"))-273.15))+" ℃");
                                    currWeather.setText(jo.getJSONArray("weather").getJSONObject(0).getString("main"));
                                    currDescription.setText(jo.getJSONArray("weather").getJSONObject(0).getString("description"));
                                    humidity.setText(jo.getJSONObject("main").getString("humidity")+"%\nHumidity");
                                    clouds.setText(jo.getJSONObject("clouds").getString("all")+"%\nClouds");
                                }
                                //print error message and set fields to null
                                catch(JSONException e){
                                    cityname.setText("One or more JSON fields could not be loaded, try again");
                                    temp.setText("");
                                    min.setText("");
                                    max.setText("");
                                    currWeather.setText("");
                                    currDescription.setText("");
                                    humidity.setText("");
                                    clouds.setText("");

                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    //error message for if api cannot find data for inputted city
                    public void onErrorResponse(VolleyError error) {
                        cityname.setText("REQUEST FAILED, INVALID CITY");
                        temp.setText("");
                        min.setText("");
                        max.setText("");
                        currWeather.setText("");
                        currDescription.setText("");
                        humidity.setText("");
                        clouds.setText("");
                    }
                });
                queue.add(stringRequest);
            }
        });
    }
}
