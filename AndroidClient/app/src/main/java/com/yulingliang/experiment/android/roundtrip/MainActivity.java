package com.yulingliang.experiment.android.roundtrip;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// This is the generated main activity class.
public class MainActivity extends ActionBarActivity {
    final String TAG = "RoundTrip.MainActivity";
    public static final String EXTRA_MESSAGE = "message";

    private static final String FRAGMENT_TAG = "MAIN_FRAGMENT";

    AtomicInteger msgId = new AtomicInteger();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment(), FRAGMENT_TAG)
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void appendLog(String log) {
        PlaceholderFragment fragment =
                (PlaceholderFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        fragment.appendLog(log);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements GcmUtils.GcmRegistrationCallback  {
        private static final String TAG = "RoundTrip.PlaceholderFragment";
        /**
         * Substitute you own sender ID here. This is the project number you got
         * from the API Console, as described in "Getting Started."
         */
        private static final String SENDER_ID = "503276688513";

        private List<String> mLogMessages = new ArrayList<String>();
        private ArrayAdapter<String> mAdapter;
        private Time mTime = new Time(Time.getCurrentTimezone());
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            Log.d(TAG, "onCreateView");
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mLogMessages);
            ListView logList = (ListView) rootView.findViewById(R.id.log_messages);
            logList.setAdapter(mAdapter);


            GcmUtils.checkAndMaybeRegisterGcm(getActivity(), getActivity(), SENDER_ID, this);

            return rootView;
        }

        public void appendLog(String logMessage) {
            mTime.setToNow();
            mLogMessages.add(0, mTime.format("%k:%M:%S") + ": " + logMessage);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onGcmRegistrationFinished(String msg) {
            appendLog(msg);
        }
    }

}
