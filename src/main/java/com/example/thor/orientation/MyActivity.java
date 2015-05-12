package com.example.thor.orientation;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.IOException;


public class MyActivity extends ActionBarActivity {
    private TextView coordinateValues, songName;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private SensorEventListener myEventListener;
    private float xValue, yValue, zValue;
    private MediaPlayer myMediaPlayer;
    private AssetFileDescriptor myFileDescriptor;
    int currentSong;
    int switching;
    private ImageView albumCover;
    private ProgressBar progress;

    ImageSwitcher imgSwitcher;

    private void updateUI()
    {
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                coordinateValues.setText("X: " + xValue + "    Y: " + yValue + "   Z: " + zValue);

                if(switching == 1)
                {
                    currentSong = (currentSong+1)%4;
                    switching = 0;
                }

                if(currentSong == 0) {
                    myFileDescriptor = getApplicationContext().getResources().openRawResourceFd(R.raw.amysayer_handsoftime);
                    songName.setText("Amy Sayer - Hands Of Time");
                    albumCover.setImageResource(R.drawable.amysayer_handsoftime);
                }
                else if(currentSong == 1){
                    myFileDescriptor = getApplicationContext().getResources().openRawResourceFd(R.raw.cranston_neverwantednewyork);
                    songName.setText("Cranston - Never Wanted New York");
                    albumCover.setImageResource(R.drawable.cranston_neverwantednewyork);
                }
                else if(currentSong == 2){
                    myFileDescriptor = getApplicationContext().getResources().openRawResourceFd(R.raw.hyqo_arrogalla);
                    songName.setText("Hyqo - Arrogalla");
                    albumCover.setImageResource(R.drawable.hyqo_arrogalla);
                }
                else if(currentSong == 3){
                    myFileDescriptor = getApplicationContext().getResources().openRawResourceFd(R.raw.sugarprince_nofunus);
                    songName.setText("Sugarprince - No Fun US");
                    albumCover.setImageResource(R.drawable.sugarprince_nofunus);
                }
            }
        });
    }


    synchronized void playAudio(AssetFileDescriptor currentSong)
    {
          myMediaPlayer.stop();
          myMediaPlayer.reset();

          try {
              myMediaPlayer.setDataSource(currentSong.getFileDescriptor(), currentSong.getStartOffset(), currentSong.getLength());
          } catch (IOException e) {
              e.printStackTrace();
          }
          try {
              myMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            myMediaPlayer.start();

    }

    public void selfDestruct(View view) {


        playAudio(myFileDescriptor);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        coordinateValues = (TextView) findViewById(R.id.editText);
        songName = (TextView) findViewById(R.id.editText4);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        myEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                float lastX = xValue;

                xValue =  event.values[0];
                yValue =  event.values[1];
                zValue =  event.values[2];

                if(xValue < 0 && lastX > 0) switching = 1;

                updateUI();

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        myMediaPlayer = new MediaPlayer();
        myFileDescriptor = getApplicationContext().getResources().openRawResourceFd(R.raw.amysayer_handsoftime);
        songName.setText("Amy Sayer - Hands Of Time");
        currentSong = 0;
        switching = 0;
        albumCover = (ImageView) findViewById(R.id.imageView);
        albumCover.setImageResource(R.drawable.amysayer_handsoftime);



    }

    public void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(myEventListener, mSensor, mSensorManager.SENSOR_DELAY_FASTEST);
    }

    public void onStop()
    {
        mSensorManager.unregisterListener(myEventListener, mSensor);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
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
