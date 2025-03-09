package com.example.weatherj;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;


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

            }
        });

    }
}