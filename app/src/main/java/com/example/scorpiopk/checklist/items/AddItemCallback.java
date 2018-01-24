package com.example.scorpiopk.checklist.items;

/**
 * Created by ScorpioPK on 1/7/2018.
 */

public interface AddItemCallback
{
    void AddItem(Item item);
    void UpdateItem(Item item);
    void ShowNewItemScreen();
    void ShowNewItemScreen(Item item);
    void HideNewItemScreen();
}
