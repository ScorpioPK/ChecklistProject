package com.example.scorpiopk.checklist.utils.sheets;

/**
 * Created by ScorpioPK on 2/1/2018.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.example.scorpiopk.checklist.MainActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.ValueRange;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An asynchronous task that handles the Google Sheets API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */
class SheetsCallWrapper extends AsyncTask<Void, Void, List<String>> {
    SheetsInitializer mSheetsInitializer = null;
    private com.google.api.services.sheets.v4.Sheets mService = null;
    private Exception mLastError = null;

    SheetsCallWrapper(GoogleAccountCredential credential, SheetsInitializer sheetsInitializer) {
        mSheetsInitializer = sheetsInitializer;
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Sheets API Android Quickstart")
                .build();
    }

    /**
     * Background task to call Google Sheets API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected List<String> doInBackground(Void... params) {
        try {
            return getDataFromApi();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    /**
     * Fetch a list of names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     * @return List of names and majors
     * @throws IOException
     */
    private List<String> getDataFromApi() throws IOException {
        String spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms";
        String range = "Class Data!A2:E";
        List<String> results = new ArrayList<String>();
        //Spreadsheet spreadsheet = new Spreadsheet();
        //spreadsheet.putAll();
        //this.mService.spreadsheets().create(spreadsheet).execute();
        ValueRange response = this.mService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values != null) {
            results.add("Name, Major");
            for (List row : values) {
                results.add(row.get(0) + ", " + row.get(4));
            }
        }
        return results;
    }

    @Override
    protected void onPreExecute() {
        // Show loading wheel
    }

    @Override
    protected void onPostExecute(List<String> output) {
        // Hide loading wheel and display data
        Log.d("SheetsCall", output.toString());
    }

    @Override
    protected void onCancelled() {
        // Hide loading wheel
        if (mLastError != null) {
            if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                mSheetsInitializer.showGooglePlayServicesAvailabilityErrorDialog(
                        ((GooglePlayServicesAvailabilityIOException) mLastError)
                                .getConnectionStatusCode());
            } else if (mLastError instanceof UserRecoverableAuthIOException) {
                MainActivity.GetCurrentActivity().startActivityForResult(
                        ((UserRecoverableAuthIOException) mLastError).getIntent(),
                        SheetsInitializer.REQUEST_AUTHORIZATION);
            } else {
                // Show error message
            }
        } else {
            //Show message: Request cancelled
        }
    }
}