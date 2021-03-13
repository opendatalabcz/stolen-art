package com.michaljanecek.stolenartfinder.ui.reportstolen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.michaljanecek.stolenartfinder.R;

public class ReportStolenFragment extends Fragment {

    private ReportStolenViewModel reportStolenViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportStolenViewModel =
                new ViewModelProvider(this).get(ReportStolenViewModel.class);
        View root = inflater.inflate(R.layout.fragment_report_stolen, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        reportStolenViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}