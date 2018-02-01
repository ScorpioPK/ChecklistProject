package com.example.scorpiopk.checklist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.scorpiopk.checklist.lists.ListsPage;
import com.example.scorpiopk.checklist.utils.FileHelper;
import com.example.scorpiopk.checklist.utils.sheets.SheetsInitializer;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends Activity implements EasyPermissions.PermissionCallbacks
{
    private static MainActivity sInstance = null;
    private SheetsInitializer mSheetsInitializer = null;

    public static MainActivity GetCurrentActivity() {
        return MainActivity.sInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sInstance = this;
        //ShowMainScreen();

        mSheetsInitializer = new SheetsInitializer(this);
        mSheetsInitializer.getResultsFromApi();
    }

    public void DeleteList(String listName) {
        ListsPage listsPage = new ListsPage(this);
        setContentView(listsPage);
        listsPage.DeleteList(listName);
    }

    public void ShowMainScreen() {
        ListsPage listsPage = new ListsPage(this);
        setContentView(listsPage);
    }

    @Override
    public void onBackPressed() {
        ShowMainScreen();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        FileHelper.PermissionResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    public void HideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @AfterPermissionGranted(SheetsInitializer.REQUEST_PERMISSION_GET_ACCOUNTS)
    public void chooseAccount() {
        mSheetsInitializer.chooseAccount();
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSheetsInitializer.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        mSheetsInitializer.onPermissionsGranted(requestCode, perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        mSheetsInitializer.onPermissionsDenied(requestCode, perms);
    }

}
