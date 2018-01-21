package com.example.scorpiopk.checklist.items;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.scorpiopk.checklist.R;
import com.example.scorpiopk.checklist.utils.ResizeAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ScorpioPK on 1/21/2018.
 */

public class ItemsPage extends FrameLayout implements AddItemCallback {
    Context mContext = null;
    String mListName = null;
    private RecyclerView mRecyclerView;
    private ItemsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button mPlusButton = null;
    LinearLayout mAddNewItemContainer = null;
    AddNewItemView mAddNewItemView = null;
    List<Item> mDataset = null;
    TextView mTitleView = null;

    public ItemsPage(@NonNull Context context, String listName) {
        super(context);
        init(context, listName);
    }

    public void init(Context context,String listName) {
        mContext = context;
        mListName = listName;
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.items_page, this);

        mTitleView = (TextView)findViewById(R.id.title_textview);
        mTitleView.setText(mListName);
        mTitleView.requestFocus();

        mAddNewItemContainer = (LinearLayout)findViewById(R.id.add_new_item_container);
        mAddNewItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideNewItemScreen();
            }
        });
        mAddNewItemView = (AddNewItemView)findViewById(R.id.add_item_layout);
        mAddNewItemView.SetAddItemCallback(this);

        mPlusButton = (Button) findViewById(R.id.plus_button);
        mPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowNewItemScreen();
            }
        });

        mDataset = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.items_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ItemsAdapter(mContext, mRecyclerView, mDataset, mListName);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.ReadDataFromFile();
    }

    @Override
    public void ShowNewItemScreen() {
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
    public void HideNewItemScreen() {
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
    public void AddItem(Item item) {
        mAdapter.AddNewItem(item);
    }

}
