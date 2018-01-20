package com.example.scorpiopk.checklist;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.scorpiopk.checklist.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements ReadFileCallback
{
    private List<Item> mDataset = null;
    Context mContext = null;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public TextView mNameTextview;
        public TextView mDetailsTextview;
        public Button mItemButton;
        public LinearLayout mCellLayout;
        public ViewHolder(LinearLayout v)
        {
            super(v);
            mCellLayout = v;
            mNameTextview = (TextView)mCellLayout.findViewById(R.id.item_name_textview);
            mDetailsTextview = (TextView)mCellLayout.findViewById(R.id.item_details_textview);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context, List<Item> myDataset)
    {
        mContext = context;
        mDataset = new ArrayList<>();
        mDataset.addAll(myDataset);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType)
    {
        // create a new view

        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_textview, parent, false);
        // set the view's size, margins, paddings and layout parameters

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
        holder.mDetailsTextview.setText(detailsText);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }

    public void AddNewItem(Item item)
    {
        mDataset.add(item);
        notifyDataSetChanged();
        SaveToFile();
    }

    public void SaveToFile() {
        JSONArray jsonArray = new JSONArray();
        for (Item item : mDataset) {
            jsonArray.put(item.ToJson());
        }
        FileHelper.SaveToFile(jsonArray.toString(), "testList.txt");
    }

    public void ReadDataFromFile() {
        FileHelper.ReadFile(mContext, "testList.txt", this);
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
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}