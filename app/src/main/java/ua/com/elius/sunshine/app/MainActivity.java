package ua.com.elius.sunshine.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
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

                String location;
                location = PreferenceManager
                        .getDefaultSharedPreferences(this)
                        .getString(getString(R.string.pref_location_key),
                                getString(R.string.pref_location_default));

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
