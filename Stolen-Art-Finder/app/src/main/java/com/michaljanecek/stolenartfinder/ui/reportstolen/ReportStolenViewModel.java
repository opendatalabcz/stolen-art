package com.michaljanecek.stolenartfinder.ui.reportstolen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReportStolenViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ReportStolenViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}