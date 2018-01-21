package com.example.scorpiopk.checklist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.scorpiopk.checklist.lists.ListsPage;
import com.example.scorpiopk.checklist.utils.FileHelper;

public class MainActivity extends Activity
{
    private static MainActivity sInstance = null;

    public static MainActivity GetCurrentActivity() {
        return MainActivity.sInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sInstance = this;
        ShowMainScreen();
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
    }

    public void HideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
