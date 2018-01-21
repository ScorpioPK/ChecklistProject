package com.example.scorpiopk.checklist.lists;

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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.scorpiopk.checklist.R;
import com.example.scorpiopk.checklist.items.AddNewItemView;
import com.example.scorpiopk.checklist.items.ItemsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ScorpioPK on 1/21/2018.
 */

public class ListsPage extends FrameLayout implements View.OnClickListener {
    Context mContext = null;
    private RecyclerView mRecyclerView;
    private ListsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button mPlusButton = null;
    LinearLayout mAddNewListContainer = null;
    List<ListItem> mDataset = null;
    TextView mTitleView = null;
    EditText mNewListEditText = null;
    Button mAddButton = null;

    public ListsPage(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public ListsPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ListsPage(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void init(Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        mContext = context;

        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.lists_page, this);

        mTitleView = (TextView)findViewById(R.id.title_textview);
        mTitleView.requestFocus();

        mNewListEditText = (EditText) findViewById(R.id.new_list_edittext);
        mAddButton = (Button) findViewById(R.id.add_list_button);
        mAddButton.setOnClickListener(this);

        mDataset = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.items_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ListsAdapter(mContext, mRecyclerView, mDataset);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.ReadDataFromFile();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_list_button:
                String newListName = mNewListEditText.getText().toString();
                if (newListName.length() > 0) {
                    mAdapter.AddNewList(new ListItem(newListName));
                }
                break;
        }
    }
}
