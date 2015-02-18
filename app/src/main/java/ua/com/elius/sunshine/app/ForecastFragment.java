package ua.com.elius.sunshine.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forecast_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            String forecastLocation = "Sumy,ua";
            FetchWeatherTask weatherTask = new FetchWeatherTask();
            weatherTask.execute(forecastLocation,null,null);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayList<String> weekForecast;
        weekForecast = new ArrayList<>();
        weekForecast.add("Today-Sunny-88/63");
        weekForecast.add("Tomorrow-Foggy-70/46");
        weekForecast.add("Weds-Cloudy-72/63");
        weekForecast.add("Thurs-Rainy-64/51");
        weekForecast.add("Fri-Foggy-70/46");
        weekForecast.add("Sat-Sunny-76/68");

        ArrayAdapter<String> adapterForecast;
        adapterForecast = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                weekForecast);

        ListView listViewForecast = (ListView) rootView.findViewById(R.id.listview_forecast);
        listViewForecast.setAdapter(adapterForecast);

        return rootView;
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected Void doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr;

            try {
                Uri.Builder forecastURI = new Uri.Builder();
                forecastURI.scheme("http");
                forecastURI.authority("api.openweathermap.org");
                forecastURI.appendPath("data");
                forecastURI.appendPath("2.5");
                forecastURI.appendPath("forecast");
                forecastURI.appendPath("daily");
                forecastURI.appendQueryParameter("q", params[0]);
                forecastURI.appendQueryParameter("units", "metric");
                forecastURI.appendQueryParameter("cnt", "7");

                Log.v(LOG_TAG, "Builded URI: " + forecastURI.build());

                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                // new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=Sumy,ua&units=metric&cnt=7");
                URL url = new URL(forecastURI.build().toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast JSON string: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("LOG_TAG", "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }
}
