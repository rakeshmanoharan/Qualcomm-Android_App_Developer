package com.weather_qualcomm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    //region static variables
    private static String TAG = "MainActivity";
    //endregion

    static {
        System.loadLibrary("myCelciusToFarenheit");
    }

    //region Variables
    private Random randomGenerator;
    private SensorManager mSensorManager;
    private Sensor mAmbientTemperatureSensor;
    private boolean sensorExists = true;
    //private String celciusList="";
    //endregion
    private TextView temperature_air;
    private String celciusList = "10,-2,0,28,87";

    public native String convertTemperature(String celciusList);

    //region activity lifecycle's
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate..");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Code to work with sensors
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        temperature_air = (TextView) findViewById(R.id.temperature);
        // Sensors introduced on Ice Cream Sandwitch level but our minimum SDK support is API 21
        if (Build.VERSION.SDK_INT >= 21) {
            mAmbientTemperatureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }
        if (mAmbientTemperatureSensor == null) {
            sensorExists = false;
            Log.d(TAG, "Sensor doesn't exist in this phone");
            temperature_air.setText("Sensor doesn't exist");
        }

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
//        String celciusListToConvert=celciusList.substring(0,celciusList.lastIndexOf(",")-1);
        //  Log.d(TAG,celciusListToConvert);
        Log.d(TAG, convertTemperature(celciusList));
    }

    // Sensor should register on create and also on resume of the activity
    @Override
    protected void onResume() {
        super.onResume();
        if (sensorExists) {
            mSensorManager.registerListener(this, mAmbientTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "Register Ambient Temperature Sensor");
        }
    }

    // Sensors should be unregistered when going on pause or stop
    @Override
    protected void onPause() {
        super.onPause();
        if (sensorExists) {
            mSensorManager.unregisterListener(this);
            Log.d(TAG, "Unregister Ambient Temperature Sensor");
        }
    }
    //endregion

    //region override methods
    @Override
    public void onSensorChanged(SensorEvent event) {
        float temperature_air = event.values[0];
        updateTemperatureUI(temperature_air);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    //endregion

    //region user defined functions
    private String generateRandom() {
        // Random Generator generates the value from 0 to 36
        // The range of celcius values possible is from -6 to 30
        String generatedNumber = String.valueOf(randomGenerator.nextInt(36) - 6);
        // Each random celcius is stored in String for later use
        //celciusList.concat(generatedNumber).concat(",");
        return generatedNumber;
    }

    private void updateTemperatureUI(float temperature) {
        temperature_air.setText(String.valueOf(temperature_air));
        Log.d(TAG, "Ambient Temperature updated on UI");
    }
    //endregion

}
