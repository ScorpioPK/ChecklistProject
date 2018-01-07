package com.example.scorpiopk.checklist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.scorpiopk.checklist.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements AddItemCallback
{
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button mPlusButton = null;
    LinearLayout mAddNewItemContainer = null;
    AddNewItemView mAddNewItemView = null;
    List<String> mDataset = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataset = new ArrayList<>();
        mDataset.add("First");
        mDataset.add("Second");
        mDataset.add("Third");

        mAddNewItemContainer = (LinearLayout)findViewById(R.id.add_new_item_container);
        mAddNewItemContainer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HideNewItemScreen();
            }
        });
        mAddNewItemView = (AddNewItemView)findViewById(R.id.add_item_layout);
        mAddNewItemView.SetAddItemCallback(this);

        mPlusButton = (Button) findViewById(R.id.plus_button);
        mPlusButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Open new window on top to add new item in it.
                ShowNewItemScreen();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        mAdapter = new MyAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void ShowNewItemScreen()
    {
        mAddNewItemContainer.setVisibility(View.VISIBLE);

        mAddNewItemView.ShowScreen();
    }

    private void HideNewItemScreen()
    {
        mAddNewItemContainer.setVisibility(View.GONE);
        mAddNewItemView.HideScreen();
    }

    @Override
    public void AddItem(String itemName)
    {
        mAdapter.AddNewItem(itemName);
    }

    @Override
    public void HideScreen()
    {
        HideNewItemScreen();
    }
}
