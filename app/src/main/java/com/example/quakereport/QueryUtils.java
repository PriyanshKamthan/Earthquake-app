package com.example.quakereport;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    public static final String LOG_TAG = MainActivity.class.getName();

    public static List<Earthquake> extractFeaturesFromJson(String earthquakeJson) {
        if (TextUtils.isEmpty(earthquakeJson)) return null;
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(earthquakeJson);
            JSONArray earthquakeArray = root.getJSONArray("features");
            for (int i = 0; i <= earthquakeArray.length() - 1; i++) {
                JSONObject firstData = earthquakeArray.getJSONObject(i);
                JSONObject properties = firstData.getJSONObject("properties");
                double mag = properties.getDouble("mag");
                String place = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");
                earthquakes.add(new Earthquake(mag, place, time, url));
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return earthquakes;
    }

    private static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the url", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) return jsonResponse;

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else Log.e(LOG_TAG, "Error response code" + httpURLConnection.getResponseCode());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results", e);
        } finally {
            if (httpURLConnection != null) httpURLConnection.disconnect();
            if (inputStream != null) inputStream.close();
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream!=null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line!=null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<Earthquake> fetchEarthquakeData(String requesUrl) {
        URL url = createURL(requesUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }
        catch (IOException e) {
            Log.e(LOG_TAG,"Problem making http request");
        }
        List<Earthquake> earthquakes = extractFeaturesFromJson(jsonResponse);
        return earthquakes;
    }
}

