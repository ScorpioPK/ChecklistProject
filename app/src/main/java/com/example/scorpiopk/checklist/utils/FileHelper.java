package com.example.scorpiopk.checklist.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.scorpiopk.checklist.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ScorpioPK on 1/20/2018.
 */

public class FileHelper {
    public enum Operation {
        READ,
        WRITE,
        NONE
    };

    final static int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 5;
    final static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/checklist/" ;
    final static String EXTENSION = ".txt";
    final static String TAG = FileHelper.class.getName();

    public static String sFileName = null;
    public static String sData = null;
    public static ReadFileCallback sCallback = null;
    public static Operation sOperation = Operation.NONE;

    public static  void ReadFile(Context context, String fileName, ReadFileCallback callback) {
        if (AskForPermission(MainActivity.GetCurrentActivity())) {
            String line = null;

            try {
                FileInputStream fileInputStream = new FileInputStream(new File(path + fileName + EXTENSION));
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + System.getProperty("line.separator"));
                }
                fileInputStream.close();
                line = stringBuilder.toString();

                bufferedReader.close();
            } catch (FileNotFoundException ex) {
                Log.d(TAG, ex.getMessage());
            } catch (IOException ex) {
                Log.d(TAG, ex.getMessage());
            }
            callback.ReadData(line);
        } else {
            sFileName = fileName;
            sCallback = callback;
            sOperation = Operation.READ;
        }
    }

    public static void SaveToFile(String data, String fileName) {
        if (AskForPermission(MainActivity.GetCurrentActivity())) {
            try {
                new File(path).mkdir();
                File file = new File(path + fileName + EXTENSION);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                fileOutputStream.write((data + System.getProperty("line.separator")).getBytes());

            } catch (FileNotFoundException ex) {
                Log.d(TAG, ex.getMessage());
            } catch (IOException ex) {
                Log.d(TAG, ex.getMessage());
            }
        } else {
            sFileName = fileName;
            sData = data;
            sOperation = Operation.WRITE;
        }
    }

    public static boolean AskForPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            //if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,
            //        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            //} else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions((Activity)context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            return false;
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            //}
        }
        return true;
    }

    public static void PermissionResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    switch (sOperation) {
                        case READ:
                            ReadFile(MainActivity.GetCurrentActivity(), sFileName, sCallback);
                            sOperation = Operation.NONE;
                            sCallback = null;
                            sFileName = null;
                            break;
                        case WRITE:
                            SaveToFile( sData, sFileName);
                            sOperation = Operation.NONE;
                            sFileName = null;
                            sData = null;
                            break;
                    }
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}
