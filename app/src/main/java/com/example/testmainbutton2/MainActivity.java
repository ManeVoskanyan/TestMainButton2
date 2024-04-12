// MainActivity.java
package com.example.testmainbutton2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 2;

    Button send;
    private List<String> phoneNumbers = new ArrayList<>();
    private String currentLocation = "";
    private boolean locationReceived = false; // Флаг для отслеживания получения локации
    private LocationManager locationManager;
    private LocationListener locationListener;
    private MediaRecorder mediaRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button numbersButton = findViewById(R.id.button);
        numbersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNumbersActivity(v);
            }
        });

        send = findViewById(R.id.button_send);
        send.setEnabled(false);
        if (checkPermissions(Manifest.permission.SEND_SMS)) {
            send.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        loadNumbersFromSharedPreferences();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                currentLocation = "Latitude: " + latitude + ", Longitude: " + longitude;
                locationReceived = true; // Устанавливаем флаг в true при получении локации
            }
        };

        if (checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION)) {
            startLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void startLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to start location updates", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopLocationUpdates() {
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                // Разрешение не получено
            }
        }
    }

    public void onSend(View view) {
        startRecording();
        String messageWithLocation = "I need HELP!\n" +
                "Location: " + currentLocation + "\n" +
                "Click here to open in maps. " + currentLocationLink();

        if (phoneNumbers.isEmpty() || messageWithLocation.isEmpty()) {
            Toast.makeText(this, "Please enter phone numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkPermissions(Manifest.permission.SEND_SMS)) {
            SmsManager smsManager = SmsManager.getDefault();
            for (String phoneNumber : phoneNumbers) {
                smsManager.sendTextMessage(phoneNumber, null, messageWithLocation, null, null);
            }
            Toast.makeText(this, "Message Sent!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadNumbersFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String phoneNumber1 = sharedPreferences.getString("phoneNumber1", "");
        String phoneNumber2 = sharedPreferences.getString("phoneNumber2", "");
        String phoneNumber3 = sharedPreferences.getString("phoneNumber3", "");
        phoneNumbers.clear();
        if (!phoneNumber1.isEmpty()) phoneNumbers.add(phoneNumber1);
        if (!phoneNumber2.isEmpty()) phoneNumbers.add(phoneNumber2);
        if (!phoneNumber3.isEmpty()) phoneNumbers.add(phoneNumber3);
    }

    public void openNumbersActivity(View view) {
        Intent intent = new Intent(this, Numbers.class);
        startActivityForResult(intent, 1);
    }

    public boolean checkPermissions(String permissions) {
        int check = ContextCompat.checkSelfPermission(this, permissions);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        stopRecording();
    }

    private String currentLocationLink() {
        return "Location in maps: " + "http://maps.google.com" + currentLocation;
    }

    private void stopRecording() {
//        if (mediaRecorder != null) {
//            mediaRecorder.stop();
//            mediaRecorder.release();
//            mediaRecorder = null;
//            Toast.makeText(this, "Recording stopped!", Toast.LENGTH_SHORT).show();
  //      }
    }
    private void startRecording() {
//        // Создаем файл для записи аудио
//        String outputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/audio_record.3gp";
//        mediaRecorder = new MediaRecorder();
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        mediaRecorder.setOutputFile(outputFile);
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//
//        try {
//            mediaRecorder.prepare();
//            mediaRecorder.start();
//            Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Failed to start recording", Toast.LENGTH_SHORT).show();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Illegal state exception occurred", Toast.LENGTH_SHORT).show();
//        }
  }

}
