package com.example.scorpiopk.checklist;

import com.example.scorpiopk.checklist.JSONWrapper;

import org.json.JSONObject;

/**
 * Created by ScorpioPK on 1/11/2018.
 */

public class Item {
    public static final String JSON_NAME = "name";
    public static final String JSON_QUANTITY = "quantity";
    public static final String JSON_PACK_TYPE = "pack_type";
    public static final String JSON_PACK_SIZE = "pack_size";
    public static final String JSON_MEASUREMENT_UNIT = "unit";
    public static final String JSON_DETAILS = "details";

    public String mName;
    public int mQuantity = Defines.DEFAULT_VALUE;
    public String mPackType = Defines.DEFAULT_STRING;
    public int mPackSize = Defines.DEFAULT_VALUE;
    public String mMeasurementUnit = Defines.DEFAULT_STRING;
    public String mDetails = Defines.DEFAULT_STRING;

    public Item(String name, int quantity, String packType, int packSize, String measurementUnit, String details) {
        mName = name;
        mQuantity = quantity;
        mPackType = packType;
        mPackSize = packSize;
        mMeasurementUnit = measurementUnit;
        mDetails = details;
    }

    public Item(JSONWrapper jsonObject) {
        mName = jsonObject.GetStringFromJSON(JSON_NAME);
        mQuantity = jsonObject.GetIntFromJSON(JSON_QUANTITY);
        mPackType = jsonObject.GetStringFromJSON(JSON_PACK_TYPE);
        mPackSize = jsonObject.GetIntFromJSON(JSON_PACK_SIZE);
        mMeasurementUnit = jsonObject.GetStringFromJSON(JSON_MEASUREMENT_UNIT);
        mDetails = jsonObject.GetStringFromJSON(JSON_DETAILS);
    }

    public JSONObject ToJson() {
        JSONWrapper jsonObject = new JSONWrapper();
        jsonObject.PutStringInJSON(JSON_NAME, mName);
        if (mQuantity != Defines.DEFAULT_VALUE) {
            jsonObject.PutIntInJSON(JSON_QUANTITY, mQuantity);
            jsonObject.PutStringInJSON(JSON_PACK_TYPE, mPackType);
        }
        if (mPackSize != Defines.DEFAULT_VALUE) {
            jsonObject.PutIntInJSON(JSON_PACK_SIZE, mPackSize);
            jsonObject.PutStringInJSON(JSON_MEASUREMENT_UNIT, mMeasurementUnit);
        }
        jsonObject.PutStringInJSON(JSON_DETAILS, mDetails);

        return jsonObject.GetJSON();
    }
}
