package com.example.admin.myapplication;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    public  Button btnStart, btnStop, btnPlay, btnStopPlayStart;
    public MediaRecorder mediaRecorder;
    public String AudioSavePathInDevice=null;
    public Random random;
    public String RandomAudioFileName="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public MediaPlayer mediaPlayer;

    public static final int RequestPermissionCode=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart=(Button)findViewById(R.id.btnRecord);
        btnStop=(Button)findViewById(R.id.btnStop);
        btnPlay=(Button)findViewById(R.id.btnPlay);
        btnStopPlayStart=(Button)findViewById(R.id.btnStopPlayRecord);

        btnStop.setEnabled(false);
        btnPlay.setEnabled(false);
        btnStopPlayStart.setEnabled(false);

        random=new Random();
        btnStart.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                if(checkPermission()){
                    AudioSavePathInDevice=Environment.getExternalStorageDirectory().
                            getAbsolutePath()+"/"+CreateRandomAudioFileName(5)+"AudioRecording.3gp";
                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    btnStart.setEnabled(false);
                    btnStop.setEnabled(true);

                    Toast.makeText(MainActivity.this, "Record start", Toast.LENGTH_LONG).show();

                }
                else{
                    requestPermission();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                mediaRecorder.stop();
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                btnPlay.setEnabled(true);
                btnStopPlayStart.setEnabled(false);
                Toast.makeText(MainActivity.this, "recording stop", Toast.LENGTH_LONG).show();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)throws  IllegalArgumentException,
            SecurityException, IllegalStateException{
                       btnStop.setEnabled(false);
                       btnStart.setEnabled(false);
                       btnStopPlayStart.setEnabled(true);
                       mediaPlayer=new MediaPlayer();
                       try{
                           mediaPlayer.setDataSource(AudioSavePathInDevice);
                           mediaPlayer.prepare();
                       }catch (IOException e){
                           e.printStackTrace();
                       }
                       mediaPlayer.start();
                       Toast.makeText(MainActivity.this, "Recording Playing",
                               Toast.LENGTH_LONG).show();

            }
        });

        btnStopPlayStart.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                btnStop.setEnabled(false);
                btnStart.setEnabled(true);
                btnStopPlayStart.setEnabled(false);
                btnPlay.setEnabled(true);

                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
            }
        });



    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder=new StringBuilder(string);
        int i=0;
        while(i<string){
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));
            i++;
        }
        return stringBuilder.toString();
    }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }
    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(MainActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
}
