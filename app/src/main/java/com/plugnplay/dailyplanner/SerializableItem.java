package com.plugnplay.dailyplanner;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andread on 10.06.2016.
 */
public class SerializableItem implements Serializable{
    private TaskItemModel mItemModel;
    private List<TaskItemModel> selectedItems;

    public TaskItemModel getItemModel() {
        return mItemModel;
    }

    public void setItemModel(TaskItemModel _itemModel) {
        mItemModel = _itemModel;
    }

    public void setSelectedItems(List<TaskItemModel> _selectedItems) {
        selectedItems = _selectedItems;
    }

    public List<TaskItemModel> getSelectedItems() {
        return selectedItems;
    }
}
