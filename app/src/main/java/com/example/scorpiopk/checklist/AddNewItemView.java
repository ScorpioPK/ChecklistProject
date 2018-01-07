package com.example.scorpiopk.checklist;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by ScorpioPK on 1/7/2018.
 */

public class AddNewItemView extends LinearLayout
{
    private Context mContext;
    private AddItemCallback mAddItemCallback = null;
    private EditText mItemNameEditText= null;
    private EditText mQuantityEditText= null;
    private Spinner mPackageSpinner = null;
    private EditText mPackSizeEditText= null;
    private Spinner mMeasurementUnitSpinner = null;
    private EditText mDetailsEditText = null;
    private Button mAddItemButton = null;

    public AddNewItemView(Context context)
    {
        super(context);
        init(context, null);
    }

    public AddNewItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        mContext = context;
        inflate(context, R.layout.edittext_view, this);

        // name EditText
        mItemNameEditText = (EditText) findViewById(R.id.name_edittext);
        mItemNameEditText.addTextChangedListener(new TextWatcher()
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
                String text = mItemNameEditText.getText().toString();
                if (text.length() >=3)
                {
                    // Add suggestions box.
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

        // Quantity EditText
        mQuantityEditText = (EditText)findViewById(R.id.quantity_edittext);

        // Package spinner
        mPackageSpinner = (Spinner) findViewById(R.id.package_spinner);
        List<String> packageList = new ArrayList<String>();
        packageList.add("Pcs.");
        packageList.add("Boxes");
        packageList.add("Bags");
        ArrayAdapter<String> packageDataAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, packageList);
        packageDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPackageSpinner.setAdapter(packageDataAdapter);

        // Pack Size EditText
        mPackSizeEditText = (EditText)findViewById(R.id.pack_size_edittext);

        // Measurement unit spinner
        mMeasurementUnitSpinner = (Spinner) findViewById(R.id.measurement_unit_spinner);
        List<String> measurementUnitList = new ArrayList<String>();
        measurementUnitList.add("g");
        measurementUnitList.add("kg");
        measurementUnitList.add("mm");
        measurementUnitList.add("m");
        measurementUnitList.add("pcs.");
        ArrayAdapter<String> measurementUnitDataAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, measurementUnitList);
        measurementUnitDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMeasurementUnitSpinner.setAdapter(measurementUnitDataAdapter);

        // Details EditText
        mDetailsEditText = (EditText)findViewById(R.id.details_edittext);

        //add item button
        mAddItemButton = (Button) findViewById(R.id.add_item_button);
        mAddItemButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mAddItemCallback != null)
                {
                    mAddItemCallback.AddItem(mItemNameEditText.getText().toString());
                }
                mItemNameEditText.setText("");
                if (mAddItemCallback != null)
                {
                    mAddItemCallback.HideScreen();
                }
            }
        });
    }

    public void ShowScreen()
    {
        InputMethodManager inputMethodManager = (InputMethodManager)mContext.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(mItemNameEditText.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        mItemNameEditText.requestFocus();
    }

    public void HideScreen()
    {
        //hide keyboard
        View view = ((Activity)mContext).getCurrentFocus();
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void SetAddItemCallback(AddItemCallback callback)
    {
        mAddItemCallback = callback;
    }
}