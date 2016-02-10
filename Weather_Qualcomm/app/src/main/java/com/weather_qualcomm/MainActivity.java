package com.weather_qualcomm;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //region static variables
    private static String TAG = "MainActivity";
    private Random randomGenerator;
    //endregion

    //region activity lifecycle's
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate..");
        //Random object is created to generate random numbers for all the 5 days
        randomGenerator = new Random();

        Log.d(TAG, "Generating Random numbers");
        //Generate temparature in celcius as random numbers
        TextView monday = (TextView) findViewById(R.id.tmon);
        monday.setText(generateRandom());

        TextView tuesday = (TextView) findViewById(R.id.ttue);
        tuesday.setText(generateRandom());

        TextView wednesday = (TextView) findViewById(R.id.twed);
        wednesday.setText(generateRandom());

        TextView thursday = (TextView) findViewById(R.id.tthu);
        thursday.setText(generateRandom());

        TextView friday = (TextView) findViewById(R.id.tfri);
        friday.setText(generateRandom());

        Log.d(TAG, "Random numbers generated");
    }

    private String generateRandom() {
        // Random Generator generates the value from 0 to 36
        // The range of celcius values possible is from -6 to 30
        return String.valueOf(randomGenerator.nextInt(36) - 6);
    }
    //endregion

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
