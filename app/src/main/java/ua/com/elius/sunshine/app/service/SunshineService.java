package ua.com.elius.sunshine.app.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ua.com.elius.sunshine.app.ForecastFragment;

public class SunshineService extends IntentService{

    private final String LOG_TAG = SunshineService.class.getSimpleName();

    private boolean DEBUG = true;

    public SunshineService() {
        super(SunshineService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {


    }

    public static class AlarmReceiver extends BroadcastReceiver {
        private final String LOG_TAG = AlarmReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "onReceive");
            String location = intent.getStringExtra(ForecastFragment.LOCATION_KEY);
            Intent serviceIntent = new Intent(context, SunshineService.class);
            intent.putExtra(ForecastFragment.LOCATION_KEY, location);
            context.startService(serviceIntent);
        }
    }
}
