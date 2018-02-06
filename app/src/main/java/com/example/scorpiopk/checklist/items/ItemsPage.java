package com.example.scorpiopk.checklist.items;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.scorpiopk.checklist.MainActivity;
import com.example.scorpiopk.checklist.R;
import com.example.scorpiopk.checklist.utils.ResizeAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ScorpioPK on 1/21/2018.
 */

public class ItemsPage extends FrameLayout implements AddItemCallback, View.OnClickListener, PopupMenu.OnMenuItemClickListener, ItemsTouchHelper.RecyclerItemTouchHelperListener {
    Context mContext = null;
    String mListName = null;
    private RecyclerView mRecyclerView;
    private ItemsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button mPlusButton = null;
    LinearLayout mAddNewItemContainer = null;
    AddNewItemView mAddNewItemView = null;
    List<Item> mDataset = null;
    private LinearLayout mBackButton = null;
    TextView mTitleView = null;
    private LinearLayout mOptionsButton = null;
    PopupMenu mOptionsMenu = null;
    View mListCover = null;

    public ItemsPage(Context context, String listName) {
        super(context);
        init(context, listName);
    }

    public void init(Context context,String listName) {
        mContext = context;
        mListName = listName;
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.items_page, this);

        mBackButton = (LinearLayout) findViewById(R.id.back_button);
        mBackButton.setOnClickListener(this);

        mTitleView = (TextView)findViewById(R.id.title_textview);
        mTitleView.setText(mListName);
        mTitleView.requestFocus();

        mOptionsButton = (LinearLayout) findViewById(R.id.options_button);
        mOptionsButton.setOnClickListener(this);
        mOptionsMenu = new PopupMenu(MainActivity.GetCurrentActivity(), mOptionsButton);
        //Inflating the Popup using xml file
        mOptionsMenu.getMenuInflater().inflate(R.menu.list_options, mOptionsMenu.getMenu());

        //registering popup with OnMenuItemClickListener
        mOptionsMenu.setOnMenuItemClickListener(this);

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
                if (mAddNewItemView.IsOpen()) {
                    HideNewItemScreen();
                }
                else {
                    ShowNewItemScreen();
                }
            }
        });

        mDataset = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.items_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ItemsAdapter(mContext, mRecyclerView, mDataset, mListName);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.ReadDataFromFile();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemsTouchHelper(0,
                                                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        mListCover = findViewById(R.id.list_cover);
        mListCover.setOnClickListener(this);

    }

    @Override
    public void ShowNewItemScreen() {
        ShowNewItemScreen(null);
    }

    @Override
    public void ShowNewItemScreen(Item item) {
        final int addItemHeightOpen = (int)getResources().getDimension(R.dimen.addItemHeightOpen);
        int addItemHeightClosed = (int)getResources().getDimension(R.dimen.addItemHeightClosed);
        ResizeAnimation resizeAnimation = new ResizeAnimation(
                mAddNewItemContainer,
                addItemHeightOpen,
                addItemHeightClosed);
        resizeAnimation.setDuration(500);
        resizeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mListCover.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mAddNewItemContainer.startAnimation(resizeAnimation);

        mPlusButton.animate().translationY(addItemHeightOpen).setDuration(500).start();
        mPlusButton.animate().rotation(-225.0f).setDuration(500).start();

        mAddNewItemView.ShowScreen(item);
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

        mPlusButton.animate().translationY(0).setDuration(500).start();
        mPlusButton.animate().rotation(0).setDuration(500).start();

        mListCover.setVisibility(GONE);

        mAddNewItemView.HideScreen();
    }

    @Override
    public void AddItem(Item item) {
        mAdapter.AddNewItem(item);
    }

    @Override
    public void UpdateItem(Item item) {
        mAdapter.UpdateItem(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                if (mAddNewItemView.IsOpen()) {
                    HideNewItemScreen();
                }
                else {
                    MainActivity.GetCurrentActivity().onBackPressed();
                }
                break;
            case R.id.options_button:
                mOptionsMenu.show();//showing popup menu
                break;
            case R.id.list_cover:
                HideNewItemScreen();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clean_items:
                mAdapter.CleanItems();
                return true;
            case R.id.sort_list:
                mAdapter.SortList();
                return true;
            case R.id.remove_purchased:
                mAdapter.RemoveBoughtItems();
                return true;
            case R.id.delete_list:
                MainActivity.GetCurrentActivity().DeleteList(mTitleView.getText().toString());
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        int itemPosition = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT) {
            Item item = mAdapter.GetItemAtPosition(itemPosition);
            ShowNewItemScreen(item);
            mAdapter.notifyItemChanged(itemPosition);
        }
        else {
            mAdapter.RemoveItem(itemPosition);
        }
    }
}
