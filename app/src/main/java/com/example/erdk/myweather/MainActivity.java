package com.example.erdk.myweather;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {


    EditText cityField;
    TextView resultWeather;
    String cityToFind;
    final String apiKey = "&APPID=3190e3dbbf55937e45d2bf778e016ee4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        cityField = (EditText) findViewById(R.id.editText);
        resultWeather = (TextView) findViewById(R.id.textView2);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Loading Weather", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                findWeather(view);

            }
        });
    }



    // Fetch data from Openweather API and parse for app.


    public void findWeather(View v) {
        cityToFind = cityField.getText().toString();

        try {
            //Make the Asynctask get data in background
            ExecuteTask task1 = new ExecuteTask();
                task1.execute("http://api.openweathermap.org/data/2.5/weather?q=" + cityToFind + apiKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // This task gets data from API in background
    public class ExecuteTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream is = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);

                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute (String s) {
            super.onPostExecute(s);

            try {
                String message = "";
                JSONObject jsonObject = new JSONObject(s);
                String infoWeatherToday = jsonObject.getString("weather");
                JSONArray array = new JSONArray(infoWeatherToday);

                // Convert JSON to text
                for (int i =0; i <array.length(); i ++) {
                    JSONObject jsonNext = array.getJSONObject(i);
                    String weatherDescription = "";

                    weatherDescription = jsonNext.getString("description");

                    if (weatherDescription != "") {
                        message += weatherDescription + "\r\n";
                    }
                }

                if (message != "") {
                    resultWeather.setText(message.toUpperCase());
                } else {
                    Toast.makeText(MainActivity.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }











    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
