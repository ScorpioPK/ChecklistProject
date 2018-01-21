package com.example.scorpiopk.checklist.lists;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.scorpiopk.checklist.MainActivity;
import com.example.scorpiopk.checklist.R;
import com.example.scorpiopk.checklist.items.Item;
import com.example.scorpiopk.checklist.items.ItemsAdapter;
import com.example.scorpiopk.checklist.items.ItemsPage;
import com.example.scorpiopk.checklist.utils.Defines;
import com.example.scorpiopk.checklist.utils.FileHelper;
import com.example.scorpiopk.checklist.utils.JSONWrapper;
import com.example.scorpiopk.checklist.utils.ReadFileCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ScorpioPK on 1/21/2018.
 */

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ViewHolder> implements ReadFileCallback, View.OnClickListener {
    private List<ListItem> mDataset = null;
    Context mContext = null;
    RecyclerView mRecyclerView = null;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mNameTextview;
        public Button mListButton;
        public LinearLayout mCellLayout;
        public LinearLayout mListLayout;
        public ViewHolder(LinearLayout v) {
            super(v);
            mCellLayout = v;
            mListLayout = (LinearLayout)mCellLayout.findViewById(R.id.list_layout);
            mNameTextview = (TextView)mCellLayout.findViewById(R.id.list_name_textview);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListsAdapter(Context context, RecyclerView recyclerView, List<ListItem> myDataset) {
        mContext = context;
        mRecyclerView = recyclerView;
        mDataset = new ArrayList<>();
        mDataset.addAll(myDataset);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view

        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_layout_file, parent, false);
        // set the view's size, margins, paddings and layout parameters
        v.setClickable(true);
        v.setOnClickListener(this);
        ListsAdapter.ViewHolder vh = new ListsAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ListsAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ListItem item = mDataset.get(position);

        holder.mNameTextview.setText(item.mName);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void AddNewList(ListItem item) {
        // Add List to the list of lists and open the list screen.
        // What to do if list already exists? Maybe we don't add it
        // and we open it's screen.
        // However, there should be a message "List already exists"
        for (ListItem listItem : mDataset) {
            if (listItem.equals(item)) {
                OpenListPage(listItem.mName);
                return;
            }
        }
        mDataset.add(item);
        notifyDataSetChanged();
        SaveToFile();
        OpenListPage(item.mName);
    }

    public void SaveToFile() {
        JSONArray jsonArray = new JSONArray();
        for (ListItem item : mDataset) {
            jsonArray.put(item.ToJson());
        }
        FileHelper.SaveToFile(jsonArray.toString(), Defines.ALL_LISTS_NAME);
    }

    public void ReadDataFromFile() {
        FileHelper.ReadFile(mContext, Defines.ALL_LISTS_NAME, this);
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
                mDataset.add(new ListItem(jsonObject));
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int itemPosition = mRecyclerView.getChildLayoutPosition(v);
        ListItem item = mDataset.get(itemPosition);
        OpenListPage(item.mName);
    }

    public void OpenListPage(String listName) {
        // Open list page
        ItemsPage itemsPage = new ItemsPage(mContext, listName);
        MainActivity.GetCurrentActivity().HideKeyboard();
        MainActivity.GetCurrentActivity().setContentView(itemsPage);
    }
}
