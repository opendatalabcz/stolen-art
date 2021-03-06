package com.michaljanecek.stolenartfinder.ui.search;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    private MutableLiveData<Bitmap> imageToSearch;

    public SearchViewModel() {
        imageToSearch = new MutableLiveData<>();
        imageToSearch.setValue(null);
    }

    public LiveData<Bitmap> getImageToSearch() {
        return imageToSearch;
    }

    public void setImageToSearch(Bitmap image){

        imageToSearch.postValue(image);

    }


}