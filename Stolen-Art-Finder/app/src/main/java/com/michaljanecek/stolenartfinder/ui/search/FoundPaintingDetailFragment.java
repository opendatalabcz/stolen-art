package com.michaljanecek.stolenartfinder.ui.search;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.michaljanecek.stolenartfinder.R;
import com.michaljanecek.stolenartfinder.models.Painting;



public class FoundPaintingDetailFragment extends Fragment {


    public static final String foundPaintingParamKey = "foundPainting";
    private Painting painting;
    private ImageView foundPaintingView;
    private TextView foundPaintingNameView;

    public FoundPaintingDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            painting = getArguments().getParcelable(foundPaintingParamKey);

        }

        showBackButton();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found_painting_detail, container, false);

        foundPaintingView = view.findViewById(R.id.found_painting_view);
        foundPaintingNameView = view.findViewById(R.id.painting_name_view);

        setPaintingInfo();

        return view;
    }

    private void setPaintingInfo(){

        // the painting (Bitmap)
        foundPaintingView.setImageBitmap(painting.getPainting());

        // the name of the painting

        foundPaintingNameView.setText(painting.getName());

    }

    private void showBackButton(){

        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if(ab != null){
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }



}