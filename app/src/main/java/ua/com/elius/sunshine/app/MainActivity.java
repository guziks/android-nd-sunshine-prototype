package ua.com.elius.sunshine.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    String mLocation;
    boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocation = Utility.getPreferredLocation(this);

        setContentView(R.layout.activity_main);
        if (findViewById(R.id.weather_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        // The activity is about to become visible.
        Log.v("EventOrder", "onStart");
    }
    @Override
    protected void onResume() {
        super.onResume();

        String prefLocation = Utility.getPreferredLocation(this);
        if (!prefLocation.equals(mLocation)) {
            mLocation = prefLocation;
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
        }

        // The activity has become visible (it is now "resumed").
        Log.v("EventOrder", "onResume");
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
        Log.v("EventOrder", "onPause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
        Log.v("EventOrder", "onStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
        Log.v("EventOrder", "onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else {
            if (id == R.id.action_view_location) {

                String location = Utility.getPreferredLocation(this);

                Uri.Builder locationURI = new Uri.Builder();
                locationURI.scheme("geo");
                locationURI.appendPath("0,0");
                locationURI.appendQueryParameter("q", location);

                Intent viewLocationIntent;
                viewLocationIntent = new Intent();
                viewLocationIntent.setAction(Intent.ACTION_VIEW);
                viewLocationIntent.setData(locationURI.build());

                if (viewLocationIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(viewLocationIntent);
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
