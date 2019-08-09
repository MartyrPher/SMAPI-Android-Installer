package com.MartyrPher.smapiandroidinstaller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int MY_PERMISSION_REQUEST_STORAGE = 2;

    private boolean hasPermissions = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

        final Button start_button = findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                if (hasPermissions)
                {
                    boolean[] foundGame;
                    ApkExtractor apkExtractor = new ApkExtractor(MainActivity.this);

                    foundGame = apkExtractor.checkForInstallOrUpgrade();

                    if((foundGame[0] || foundGame[1] || foundGame[2]))
                    {
                        BackgroundTask backgroundTask = new BackgroundTask(MainActivity.this, apkExtractor, foundGame[0]);
                        backgroundTask.execute();
                    }
                    else
                    {
                        DialogFrag.showDialog(MainActivity.this, R.string.cant_find, 1);
                        start_button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }

                }
                else
                {
                    requestPermissions();
                }
            }
        });
    }


    //Request permissions to be able to read/write external storage
    public void requestPermissions()
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST_STORAGE);
        }
        else
        {
            hasPermissions = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    hasPermissions = true;
                    Toast.makeText(this, R.string.permission, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}