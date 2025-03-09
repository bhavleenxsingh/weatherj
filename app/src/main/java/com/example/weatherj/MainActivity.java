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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {


    Button searchbtn;
    EditText searchbox;
    TextView result;
    String url;

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

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = searchbox.getText().toString();
                url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=d73050f0c136744314578782b3409ede";
                new Fetch().execute();
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
                 Toast.makeText(MainActivity.this, "Error! Please Try Again with Correct City!", Toast.LENGTH_SHORT).show();
                return "Error : " + e.getMessage();
                }
            }

            @Override
            public void onPostExecute(String results){
                try{
                    JSONObject json = new JSONObject(results);
                    JSONObject main  = json.getJSONObject("main");
                    double temp = main.getDouble("temp");
                    result.setText("Temperature is : " + temp + " C");
                }
                catch (Exception e){
                    result.setText("Error loading Weather");
                }
            }

        }

    }
