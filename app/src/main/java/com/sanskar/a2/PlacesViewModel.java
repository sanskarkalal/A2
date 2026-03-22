package com.sanskar.a2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlacesViewModel extends ViewModel {

    private final MutableLiveData<Integer> selectedIndex = new MutableLiveData<>(-1);
    private final MutableLiveData<String> selectedUrl = new MutableLiveData<>("");

    public void selectPlace(int index, String url) {
        selectedIndex.setValue(index);
        selectedUrl.setValue(url);
    }

    public LiveData<Integer> getSelectedIndex() {
        return selectedIndex;
    }

    public LiveData<String> getSelectedUrl() {
        return selectedUrl;
    }
}