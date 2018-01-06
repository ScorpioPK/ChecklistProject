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

public class MainActivity extends Activity
{
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button mEditTextButton = null;
    private Button mPlusButton = null;
    LinearLayout mAddNewItemContainer = null;
    private EditText mEditText = null;
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

        mEditText = (EditText) mAddNewItemContainer.findViewById(R.id.edittext_view);
        mEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                String text = mEditText.getText().toString();
                if (text.length() >=3)
                {
                    //Add suggestions box.
                    // Change main layout from Linear Layout to Relative Layout.
                    // Add suggestion box under EditText, but don't link it in any way to the
                    // RecyclerView. Make sure suggestions box is on top of RecyclerView
                    // Items in suggestion box should be clickable.
                    // When clicked, the text should be inserted in the EditText
                    // It will NOT be inserted in the list directly(only after button is pressed.
                    // When item is sent to the EditText, hide Suggestions box. Only show it again
                    // after the text in EditText is changed.
                }
            }
        });
        mEditTextButton = (Button) mAddNewItemContainer.findViewById(R.id.edittext_button);
        mEditTextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mAdapter.AddNewItem(mEditText.getText().toString());
                mEditText.setText("");
                HideNewItemScreen();
            }
        });

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

        InputMethodManager inputMethodManager =  (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(mEditText.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        mEditText.requestFocus();
    }

    private void HideNewItemScreen()
    {
        mAddNewItemContainer.setVisibility(View.GONE);
        //hide keyboard
        View view = this.getCurrentFocus();
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
