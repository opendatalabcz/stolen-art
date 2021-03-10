package com.michaljanecek.stolenartfinder.ui.search;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    private MutableLiveData<Bitmap> imageToSearch;

    private ArrayList<Bitmap> foundImagesList = new ArrayList<Bitmap>();
    private MutableLiveData<List<Bitmap>> foundImages;

    public SearchViewModel() {
        imageToSearch = new MutableLiveData<>();
        imageToSearch.setValue(null);

        foundImages = new MutableLiveData<>();
        foundImages.setValue(null);
    }

    public LiveData<Bitmap> getImageToSearch() {
        return imageToSearch;
    }

    public void setImageToSearch(Bitmap image){

        imageToSearch.postValue(image);

    }

    public LiveData<List<Bitmap>> getFoundImages() {
        return foundImages;
    }

    public void updateFoundImages(Bitmap image){

        // add the newly downloaded image to a classic list
        foundImagesList.add(image);

        // update the live data with the udpated list, no need to create new list
        foundImages.postValue(foundImagesList);

    }


}