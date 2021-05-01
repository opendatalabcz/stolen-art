package com.michaljanecek.stolenartfinder.ui.search;

import android.graphics.Bitmap;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    private final MutableLiveData<Bitmap> imageToSearch;

    private final ArrayList<Pair<Integer, Bitmap> > foundImagesList = new ArrayList<>();
    private final MutableLiveData<List<Pair<Integer, Bitmap>>> foundImages;

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

    public LiveData<List<Pair<Integer, Bitmap>>> getFoundImages() {
        return foundImages;
    }

    public void updateFoundImages(Bitmap image, Integer id){

        Pair<Integer, Bitmap> downloadedImageWithId = new Pair<>(id, image);

        // remove this line if you want to have more images in the list
        foundImagesList.clear();

        // add the newly downloaded image to a classic list
        foundImagesList.add(downloadedImageWithId);

        // update the live data with the udpated list, no need to create new list
        foundImages.postValue(foundImagesList);

    }


}