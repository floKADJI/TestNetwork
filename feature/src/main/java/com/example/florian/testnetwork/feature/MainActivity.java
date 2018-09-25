package com.example.florian.testnetwork.feature;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = "MainActivity";
    public static final String URL_link = "https://www.google.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        URL urlANDROID = createURL(URL_link);

        NetworkOperation task = new NetworkOperation();
        task.execute(urlANDROID);
    }

    private void updateUI (String result){
        TextView display = (TextView) findViewById(R.id.displayResult);
        display.setText(result);
    }

    private class NetworkOperation extends AsyncTask<URL, Void, String> {
        String response = null;

        @Override
        protected String doInBackground(URL... urls) {

            try {
                response = makeHttpRequest(urls[0]);
            }catch (IOException e){
                Log.e(LOG_TAG, "Error", e);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            updateUI(response);
        }
    }

    private static URL createURL(String stringURL){
        URL url = null;
        try {
            url = new URL(stringURL);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }

    private String makeHttpRequest(URL url) throws IOException {
        String responseConnection = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(1000 /* ms*/);
            urlConnection.setConnectTimeout(1500 /* ms*/);
            urlConnection.connect();
            if(urlConnection.getResponseCode() == 200){
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                responseConnection = "Successful";
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                responseConnection = "failed";
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the data", e);
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }
        return responseConnection;
    }
}
