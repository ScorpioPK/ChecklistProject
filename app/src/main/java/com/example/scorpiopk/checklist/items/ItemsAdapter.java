package com.example.scorpiopk.checklist.items;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.scorpiopk.checklist.R;
import com.example.scorpiopk.checklist.utils.Defines;
import com.example.scorpiopk.checklist.utils.FileHelper;
import com.example.scorpiopk.checklist.utils.JSONWrapper;
import com.example.scorpiopk.checklist.utils.ReadFileCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> implements ReadFileCallback, View.OnClickListener, View.OnLongClickListener
{
    private List<Item> mDataset = null;
    private String mListName = null;
    Context mContext = null;
    RecyclerView mRecyclerView = null;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public TextView mNameTextview;
        public TextView mDetailsTextview;
        public LinearLayout mItemLayout;
        public CardView mItemCardview;
        public TextView mItemBackgroundLeft;
        public TextView mItemBackgroundRight;
        public ViewHolder(FrameLayout v)
        {
            super(v);
            mItemLayout = (LinearLayout)v.findViewById(R.id.item_layout);
            mItemCardview = (CardView)v.findViewById(R.id.item_cardview);
            mItemBackgroundLeft = (TextView)v.findViewById(R.id.item_background_left);
            mItemBackgroundRight = (TextView)v.findViewById(R.id.item_background_right);
            mNameTextview = (TextView)v.findViewById(R.id.item_name_textview);
            mDetailsTextview = (TextView)v.findViewById(R.id.item_details_textview);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ItemsAdapter(Context context, RecyclerView recyclerView, List<Item> myDataset, String listName)
    {
        mContext = context;
        mRecyclerView = recyclerView;
        mDataset = new ArrayList<>();
        mDataset.addAll(myDataset);
        mListName = listName;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType)
    {
        // create a new view

        FrameLayout v = (FrameLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_file, parent, false);
        // set the view's size, margins, paddings and layout parameters
        v.setClickable(true);
        v.setOnClickListener(this);
        v.setOnLongClickListener(this);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Item item = mDataset.get(position);

        SetItemBackgroundForState(holder.mItemLayout, item.mState);

        holder.mNameTextview.setText(item.mName);
        String detailsText = new String();
        if (Defines.DEFAULT_VALUE != item.mQuantity) {
            detailsText += item.mQuantity + " " + item.mPackType;
        }
        if (Defines.DEFAULT_VALUE != item.mPackSize) {
            if (detailsText.length() > 0) {
                detailsText += " of ";
            }
            detailsText += item.mPackSize + " " + item.mMeasurementUnit;
        }
        if (!Defines.DEFAULT_STRING.equals(item.mDetails)) {
            if (detailsText.length() > 0) {
                detailsText += ". ";
            }
            detailsText += item.mDetails;
        }
        if (detailsText.length() == 0) {
            holder.mDetailsTextview.setVisibility(View.GONE);
        }
        else {
            holder.mDetailsTextview.setVisibility(View.VISIBLE);
            holder.mDetailsTextview.setText(detailsText);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }

    public void AddNewItem(Item item)
    {
        // Need to check if an item with same name already exists. We show a pop up and go back to edit
        // Maybe ask to override?
        mDataset.add(item);
        notifyItemInserted(mDataset.size() - 1);
        SaveToFile();
    }

    public void SaveToFile() {
        JSONArray jsonArray = new JSONArray();
        for (Item item : mDataset) {
            jsonArray.put(item.ToJson());
        }
        FileHelper.SaveToFile(jsonArray.toString(), mListName);
    }

    public void ReadDataFromFile() {
        FileHelper.ReadFile(mContext, mListName, this);
    }

    @Override
    public void ReadData(String data) {
        if (data == null) {
            return;
        }
        try {
            mDataset.clear();
            JSONArray jsonArray = new JSONArray(data);
            for (int i=0; i < jsonArray.length(); i++) {
                JSONWrapper jsonObject = new JSONWrapper(jsonArray.getJSONObject(i));
                mDataset.add(new Item(jsonObject));
            }
            notifyDataSetChanged();
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onLongClick(View v)
    {
        int itemPosition = mRecyclerView.getChildLayoutPosition(v);
        Item item = mDataset.get(itemPosition);
        LinearLayout itemLayout = (LinearLayout)v.findViewById(R.id.item_layout);
        switch (item.mState) {
            case Item.TO_BUY:
            case Item.BOUGHT:
                item.mState = Item.ASSIGNED;
                break;
            case Item.ASSIGNED:
                item.mState = Item.TO_BUY;
                break;
        }
        SetItemBackgroundForState(itemLayout, item.mState);
        SaveToFile();
        return true;
    }

    @Override
    public void onClick(View v) {
        int itemPosition = mRecyclerView.getChildLayoutPosition(v);
        Item item = mDataset.get(itemPosition);
        LinearLayout itemLayout = (LinearLayout)v.findViewById(R.id.item_layout);
        switch (item.mState) {
            case Item.TO_BUY:
            case Item.ASSIGNED:
                item.mState = Item.BOUGHT;
                break;
            case Item.BOUGHT:
                item.mState = Item.TO_BUY;
                break;
        }
        SetItemBackgroundForState(itemLayout, item.mState);
        SaveToFile();
    }

    public void RemoveItem(int itemPosition) {
        mDataset.remove(itemPosition);
        notifyItemRemoved(itemPosition);
        SaveToFile();
    }

    public void SetItemBackgroundForState(LinearLayout itemLayout, int state) {
        switch (state) {
            case Item.TO_BUY:
                itemLayout.setBackgroundResource(R.color.itemToBuy);
                break;
            case Item.BOUGHT:
                itemLayout.setBackgroundResource(R.color.itemBought);
                break;
            case Item.ASSIGNED:
                itemLayout.setBackgroundResource(R.color.itemAssigned);
                break;
        }
    }

    public void CleanItems() {
        for (Item tempItem : mDataset) {
            tempItem.mState = Item.TO_BUY;
        }
        notifyDataSetChanged();
        SaveToFile();
    }

    public void SortList() {
        Collections.sort(mDataset, new ItemsComparator());
        notifyDataSetChanged();
        SaveToFile();
    }

    public void RemoveBoughtItems() {
        int index = 0;
        while (index < mDataset.size()) {
            Item item = mDataset.get(index);
            if (item.mState == Item.BOUGHT) {
                mDataset.remove(index);
            }
            else {
                index++;
            }
        }
        notifyDataSetChanged();
        SaveToFile();
    }

    public void UpdateItem(Item item) {
        for (int index = 0; index < mDataset.size(); index++) {
            Item listItem = mDataset.get(index);
            if (listItem.mTag == item.mTag) {
                mDataset.remove(index);
                mDataset.add(index, item);
                notifyItemChanged(index);
                break;
            }
        }
    }

    public Item GetItemAtPosition(int position) {
        return mDataset.get(position);
    }

    public class ItemsComparator implements Comparator<Item> {
        @Override
        public int compare(Item item1, Item item2) {
            if (item1.mState == Item.TO_BUY ||
                    (item1.mState == Item.ASSIGNED && item2.mState != Item.TO_BUY) ||
                    (item1.mState == Item.BOUGHT && item2.mState == Item.BOUGHT)) {
                return -1;
            }
            return 1;
        }
    }
}