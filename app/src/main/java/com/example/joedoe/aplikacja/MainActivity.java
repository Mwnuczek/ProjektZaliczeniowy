package com.example.joedoe.aplikacja;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor swiatlo;
    private Sensor magnetometr;
    private Sensor akcelerometr;

    private float[] wartSwiatla;
    private float[] wartMagnetometr;
    private float[] wartAkcelometr;

    TextView textAkcelometr;
    TextView textMagnetyczne;
    TextView textOswietlenie;
    TextView textOdczyt;

    Button button;
    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textAkcelometr = (TextView) findViewById(R.id.textAkcelometr);
        textMagnetyczne = (TextView) findViewById(R.id.textMagnetyczne);
        textOswietlenie = (TextView) findViewById(R.id.textOswietlenie);
        textOdczyt = (TextView) findViewById(R.id.textOdczyt);

        button = (Button) findViewById(R.id.buttonZapis);
        button2 = (Button) findViewById(R.id.buttonOdczyt);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        magnetometr = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        swiatlo = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        akcelerometr = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String FILENAME ="plik.txt";
                String pomiary;

                String xAkcelometr;
                String yAkcelometr;
                String zAkcelometr;

                xAkcelometr = String.format("%.0f", wartAkcelometr[0]);
                yAkcelometr = String.format("%.0f", wartAkcelometr[1]);
                zAkcelometr = String.format("%.0f", wartAkcelometr[2]);

                String xMagnetyczne;
                String yMagnetyczne;
                String zMagnetyczne;

                xMagnetyczne = String.format("%.0f", wartMagnetometr[0]);
                yMagnetyczne = String.format("%.0f", wartMagnetometr[1]);
                zMagnetyczne = String.format("%.0f", wartMagnetometr[2]);

                pomiary = "Akcelometr: X: "+xAkcelometr+" Y: "+yAkcelometr+" Z: "+zAkcelometr +"\nMagnetometr: X:"+xMagnetyczne+" Y: "+yMagnetyczne+" Z: "+zMagnetyczne
                        + "\nSwiatlo: " + wartSwiatla[0];

                FileOutputStream fos;
                try {
                    fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                    fos.write(pomiary.getBytes());
                    fos.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Zapisano", Toast.LENGTH_LONG).show();

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                StringBuilder text = new StringBuilder();
                try {
                    InputStream instream = openFileInput("plik.txt");
                    if (instream != null) {
                        InputStreamReader inputreader = new InputStreamReader(instream);
                        BufferedReader buffreader = new BufferedReader(inputreader);
                        String line=null;
                        while (( line = buffreader.readLine()) != null) {
                            text.append(line);
                            text.append('\n');
                        }}}catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Wczytano", Toast.LENGTH_LONG).show();

                TextView tv = (TextView)findViewById(R.id.textOdczyt);
                textOdczyt.setText(text);

            }
        });

    }

    @Override
    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);

    }

    @Override
    protected void onResume(){
        super.onResume();

        mSensorManager.registerListener(this, akcelerometr, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, magnetometr, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, swiatlo, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event){

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            wartAkcelometr = new float[3];
            System.arraycopy(event.values, 0, wartAkcelometr, 0, 3);
            String xAkcelometr;
            String yAkcelometr;
            String zAkcelometr;

            xAkcelometr = String.format("%.0f", wartAkcelometr[0]);
            yAkcelometr = String.format("%.0f", wartAkcelometr[1]);
            zAkcelometr = String.format("%.0f", wartAkcelometr[2]);
            textAkcelometr.setText("Akcelometr: X:"+xAkcelometr+" Y: "+yAkcelometr+" Z: "+zAkcelometr);

        }

        else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            wartMagnetometr = new float[3];
            System.arraycopy(event.values, 0, wartMagnetometr, 0, 3);
            String xMagnetyczne;
            String yMagnetyczne;
            String zMagnetyczne;

            xMagnetyczne = String.format("%.0f", wartMagnetometr[0]);
            yMagnetyczne = String.format("%.0f", wartMagnetometr[1]);
            zMagnetyczne = String.format("%.0f", wartMagnetometr[2]);

            textMagnetyczne.setText("Magnetometr: X:"+xMagnetyczne+" Y: "+yMagnetyczne+" Z: "+zMagnetyczne );

        }
        else if(event.sensor.getType() == Sensor.TYPE_LIGHT){
            wartSwiatla =new float[1];
            System.arraycopy(event.values, 0, wartSwiatla, 0, 1);
            textOswietlenie.setText("Swiatlo: " + wartSwiatla[0]);


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}