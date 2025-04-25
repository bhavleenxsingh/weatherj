package com.example.weatherj;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {


    Button searchbtn;
    EditText searchbox;
    TextView result;
    String url;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchbtn = findViewById(R.id.searchbtn);
        searchbox = findViewById(R.id.searchbox);
        result = findViewById(R.id.result);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("The Weather");

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String city = searchbox.getText().toString();
                    url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=d73050f0c136744314578782b3409ede";
                    new Fetch().execute();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error loading city's weather. Please check spellings and try again!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

        class Fetch extends AsyncTask<Void, Void, String>{
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL urlin = new URL(url);
                    HttpsURLConnection conn = (HttpsURLConnection) urlin.openConnection();
                    conn.setRequestMethod("GET");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder resultw = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        resultw.append(line);
                    }
                    reader.close();
                    return resultw.toString();
                }
                catch (Exception e){
                return "Error : " + e.getMessage();
                }
            }

            @Override
            public void onPostExecute(String results){
                try{
                    JSONObject json = new JSONObject(results);
                    JSONObject main  = json.getJSONObject("main");
                    JSONObject cloud = json.getJSONObject("clouds");
                    JSONObject sys = json.getJSONObject("sys");

                    double temp = main.getDouble("temp");
                    int tempc = (int) (temp - 273.15);

                    double feel = main.getDouble("feels_like");
                    int feels = (int) (feel - 273.15);

                    int humid = main.getInt("humidity");

                    int cloudcount = cloud.getInt("all");

                    long sunrise = sys.getLong("sunrise");
                    String sunrisetime = unixtotime(sunrise);

                    long sunset = sys.getLong("sunset");
                    String sunsettime = unixtotime((sunset));



                    result.setText("Temperature : " + tempc + " \u00B0C \n" +
                            "Feels Like : " + feels + " \u00B0C \n" +
                            "Clouds : " + cloudcount + " %\n" +
                            "Humidity : " + humid + " %\n" +
                            "Sunrise : " + sunrisetime + ".\n" +
                            "Sunset : " + sunsettime + ".\n" +
                            "");
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "Error! Please Try Again. \n Use Correct spellings of the City!", Toast.LENGTH_SHORT).show();
                    result.setText("Error loading Weather");
                }
            }

        }


        public static String unixtotime(long unixseconds){

            Date date = new Date(unixseconds * 1000L);
            SimpleDateFormat dtfrmt = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return dtfrmt.format(date);
        }
    }