package com.weather_qualcomm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {
    //region static/final variables
    private static String TAG = "MainActivity";

    static {
        System.loadLibrary("myCelciusToFarenheit");
    }

    final String DEGREE = "\u00b0";
    final String DEGREE_CELCIUS = " ".concat(DEGREE).concat("C");
    //endregion
    final String DEGREE_FAHRENHEIT = " ".concat(DEGREE).concat("F");
    //region Variables
    private Random randomGenerator;
    private SensorManager mSensorManager;
    private Sensor mAmbientTemperatureSensor;
    private boolean sensorExists;
    private TextView temperature_air;
    private String mCelciusStr;
    private String mFahrenheit;
    private Button convertButton;
    private Boolean mViewCelcius;
    private TextView monday;
    private TextView tuesday;
    private TextView wednesday;
    private TextView thursday;
    private TextView friday;
    //endregion

    //Native function to be called to convert the temperature from celcius to fahrenheit
    public native String convertTemperature(String celciusList);

    //region activity lifecycle's functions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate..");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Code to work with sensors
        sensorExists = true;
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        temperature_air = (TextView) findViewById(R.id.temperature);
        // Sensors introduced on Ice Cream Sandwitch level i.e., API 14 but our minimum SDK support is API 21
        if (Build.VERSION.SDK_INT >= 21) {
            mAmbientTemperatureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }
        if (mAmbientTemperatureSensor == null) {
            sensorExists = false;
            Log.d(TAG, "Sensor doesn't exist in this phone");

            temperature_air.setText("Sensor doesn't exist");
            // getColor(int id ) method is deprecated in API 23 but our app needs to support API 21 as well
            temperature_air.setTextColor(getResources().getColor(R.color.red));

        }

        //Random object is created to generate random numbers for all the 5 days
        randomGenerator = new Random();
        Log.d(TAG, "Generating Random numbers");
        //Generate temparature in celcius as random numbers
        monday = (TextView) findViewById(R.id.tmon);
        monday.setText(generateRandom().concat(DEGREE_CELCIUS));
        tuesday = (TextView) findViewById(R.id.ttue);
        tuesday.setText(generateRandom().concat(DEGREE_CELCIUS));
        wednesday = (TextView) findViewById(R.id.twed);
        wednesday.setText(generateRandom().concat(DEGREE_CELCIUS));
        thursday = (TextView) findViewById(R.id.tthu);
        thursday.setText(generateRandom().concat(DEGREE_CELCIUS));
        friday = (TextView) findViewById(R.id.tfri);
        friday.setText(generateRandom().concat(DEGREE_CELCIUS));
        Log.d(TAG, "Random numbers generated");

//        Using native C++ code to convert all the celsius values to Fahrenheit
        mFahrenheit = convertTemperature(mCelciusStr.trim());
        mFahrenheit = mFahrenheit.substring(0, mFahrenheit.lastIndexOf(",")); // Just to cut off the last delimitter
        Log.d(TAG, "Converted to Fahrenheit: " + mFahrenheit);
        mViewCelcius = true;

        //Button to show the temperature in either celcius or fahrenheit on UI
        convertButton = (Button) findViewById(R.id.convert);
        convertButton.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (mViewCelcius) {
            updateUIwithValues(mFahrenheit, DEGREE_FAHRENHEIT);
            convertButton.setText("To Celcius");
            mViewCelcius = false;
        } else {
            updateUIwithValues(mCelciusStr, DEGREE_CELCIUS);
            convertButton.setText("To Fahrenheit");
            mViewCelcius = true;
        }
    }

    //endregion

    //region user defined functions
    public String generateRandom() {
        // Random Generator generates the value from 0 to 36
        // The range of celcius values possible is from -6 to 30
        String generatedNumber = String.valueOf(randomGenerator.nextInt(36) - 6);
        // Each random celcius is stored in String for later use
        if (mCelciusStr == null)
            mCelciusStr = generatedNumber;
        else
            mCelciusStr = mCelciusStr.concat(",").concat(generatedNumber);
        Log.d(TAG, "generatedNumber:  " + mCelciusStr);
        return generatedNumber;
    }

    private void updateTemperatureUI(float temperature) {
        temperature_air.setText(String.valueOf(temperature_air));
        Log.d(TAG, "Ambient Temperature updated on UI");
    }


    private void updateUIwithValues(String mValues, String degree) {
        String[] values = mValues.split(",");
        monday.setText(values[0].concat(degree));
        tuesday.setText(values[1].concat(degree));
        wednesday.setText(values[2].concat(degree));
        thursday.setText(values[3].concat(degree));
        friday.setText(values[4].concat(degree));
    }
    //endregion

}
