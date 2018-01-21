package com.example.scorpiopk.checklist.lists;

import com.example.scorpiopk.checklist.utils.Defines;
import com.example.scorpiopk.checklist.utils.JSONWrapper;

import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by ScorpioPK on 1/21/2018.
 */

public class ListItem {
    public static final String JSON_NAME = "name";

    public String mName;

    public ListItem(String name) {
        mName = name;
    }

    public ListItem(JSONWrapper jsonObject) {
        mName = jsonObject.GetStringFromJSON(JSON_NAME);
    }

    public JSONObject ToJson() {
        JSONWrapper jsonObject = new JSONWrapper();
        jsonObject.PutStringInJSON(JSON_NAME, mName);
        return jsonObject.GetJSON();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ListItem listItem = (ListItem) o;
        return (mName == listItem.mName) || (mName != null && mName.equals(listItem.mName));
    }

    @Override
    public int hashCode() {
        return Objects.hash(mName);
    }
}
