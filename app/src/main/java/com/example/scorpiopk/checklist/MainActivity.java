package com.example.scorpiopk.checklist;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements AddItemCallback
{
    private static Activity sInstance = null;

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button mPlusButton = null;
    LinearLayout mAddNewItemContainer = null;
    AddNewItemView mAddNewItemView = null;
    List<Item> mDataset = null;
    TextView mTitleView = null;

    public static Activity GetCurrentActivity() {
        return MainActivity.sInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitleView = (TextView)findViewById(R.id.title_textview);
        mTitleView.requestFocus();

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

        mDataset = new ArrayList<>();
        //mDataset.add(new Item("First", Defines.DEFAULT_VALUE, Defines.DEFAULT_STRING, 2, "kg", Defines.DEFAULT_STRING));
        //mDataset.add(new Item("Second", 5, "Pcs", Defines.DEFAULT_VALUE, Defines.DEFAULT_STRING, "From Lidl"));
        //mDataset.add(new Item("Third", 3, "Bags", 2, "Pcs", "From Auchan"));
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)


        mAdapter = new MyAdapter(this, mDataset);
        mRecyclerView.setAdapter(mAdapter);

        sInstance = this;

        mAdapter.ReadDataFromFile();
    }

    @Override
    public void ShowNewItemScreen()
    {
        //mAddNewItemContainer.setVisibility(View.VISIBLE);
        // Need to animate opening
        /*ScaleAnimation scale = new ScaleAnimation((float)1.0, (float)1.0, (float)1.0, (float)2.67);
        scale.setFillAfter(true);
        scale.setDuration(1000);
        mAddNewItemContainer.startAnimation(scale);*/
        //mAddNewItemContainer.animate().scaleY(2.67f)
        //        .setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(1000);

        ResizeAnimation resizeAnimation = new ResizeAnimation(
                mAddNewItemContainer,
                (int)getResources().getDimension(R.dimen.addItemHeightOpen),
                (int)getResources().getDimension(R.dimen.addItemHeightClosed)
        );
        resizeAnimation.setDuration(500);
        mAddNewItemContainer.startAnimation(resizeAnimation);

        mAddNewItemView.ShowScreen();
    }

    @Override
    public void HideNewItemScreen()
    {
        //mAddNewItemContainer.setVisibility(View.GONE);
        //need to animate closing
        //mAddNewItemContainer.animate().scaleY(2.67f)
        //        .setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(1000);
        ResizeAnimation resizeAnimation = new ResizeAnimation(
                mAddNewItemContainer,
                (int)getResources().getDimension(R.dimen.addItemHeightClosed),
                (int)getResources().getDimension(R.dimen.addItemHeightOpen)
        );
        resizeAnimation.setDuration(500);
        mAddNewItemContainer.startAnimation(resizeAnimation);
        mTitleView.requestFocus();
        mAddNewItemView.HideScreen();
    }

    @Override
    public void AddItem(Item item)
    {
        mAdapter.AddNewItem(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        FileHelper.PermissionResult(requestCode, permissions, grantResults);
    }
}
