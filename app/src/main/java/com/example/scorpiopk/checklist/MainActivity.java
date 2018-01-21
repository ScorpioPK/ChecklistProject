package com.example.scorpiopk.checklist;

import android.app.Activity;
import android.os.Bundle;

import com.example.scorpiopk.checklist.items.ItemsPage;
import com.example.scorpiopk.checklist.lists.ListsPage;
import com.example.scorpiopk.checklist.utils.FileHelper;

public class MainActivity extends Activity
{
    private static Activity sInstance = null;

    public static Activity GetCurrentActivity() {
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
}
