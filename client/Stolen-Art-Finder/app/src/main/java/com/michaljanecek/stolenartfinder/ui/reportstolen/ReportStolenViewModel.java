package com.michaljanecek.stolenartfinder.ui.reportstolen;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.michaljanecek.stolenartfinder.R;

public class ReportStolenViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Bitmap> paintingToUpload;

    public ReportStolenViewModel(Application application) {
        super(application);

        Bitmap placeholder = BitmapFactory.decodeResource(application.getResources(),
                R.drawable.placeholder);
        paintingToUpload = new MutableLiveData<>();
        paintingToUpload.setValue(placeholder);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Bitmap> getPaintingToUpload() {
        return paintingToUpload;
    }

    public void setPaintingToUpload(Bitmap image){

        paintingToUpload.postValue(image);

    }

}