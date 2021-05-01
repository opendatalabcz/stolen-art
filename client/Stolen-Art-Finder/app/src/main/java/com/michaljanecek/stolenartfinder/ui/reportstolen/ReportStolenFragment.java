package com.michaljanecek.stolenartfinder.ui.reportstolen;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.michaljanecek.stolenartfinder.R;
import com.michaljanecek.stolenartfinder.helpers.ImageUtils;
import com.michaljanecek.stolenartfinder.models.FoundPaintingModel;
import com.michaljanecek.stolenartfinder.networking.APIClient;
import com.michaljanecek.stolenartfinder.networking.ReportStolenService;
import com.michaljanecek.stolenartfinder.networking.SearchDBService;
import com.michaljanecek.stolenartfinder.ui.search.SearchFragment;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class ReportStolenFragment extends Fragment {

    static final int REQUEST_IMAGE_PICK = 2;

    // path to currently selected photo
    String currentPhotoPath;

    private ReportStolenViewModel reportStolenViewModel;

    private EditText paintingNameEditText;
    private Button reportButton;
    private ImageView reportedPainting;

    boolean picked = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportStolenViewModel =
                new ViewModelProvider(this).get(ReportStolenViewModel.class);
        View root = inflater.inflate(R.layout.fragment_report_stolen, container, false);

        paintingNameEditText = root.findViewById(R.id.painting_name_edit_text);
        reportButton = root.findViewById(R.id.report_button);
        reportedPainting = root.findViewById(R.id.reported_painting);

        reportStolenViewModel.getPaintingToUpload().observe(getViewLifecycleOwner(), image -> reportedPainting.setImageBitmap(image));

        setOnClickListeners();

        return root;
    }

    private void setOnClickListeners() {

        reportedPainting.setOnClickListener(v -> {

            if (picked) {
                // if an image is already picked, two clicks are required
                picked = false;
                return;
            }

            dispatchPickPictureIntent();

        });

        reportButton.setOnClickListener(v -> {

            uploadToServer();

        });

    }

    /**
     * Dispatches an Intent to pick a picture to upload.
     */
    private void dispatchPickPictureIntent() {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);

    }

    /**
     * Handles the result of activities started by dispatched intents.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case REQUEST_IMAGE_PICK:
                if (resultCode == RESULT_OK) {

                    Uri selectedImage = data.getData();

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    if (selectedImage != null) {
                        Cursor cursor = getContext().getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        if (cursor != null) {

                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            currentPhotoPath = cursor.getString(columnIndex);
                            cursor.close();
                            updateImage();

                        }
                    }
                }
                break;
        }
    }

    /**
     * Sets the imageView to the selected painting.
     */
    public void updateImage() {

        Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
        imageBitmap = ImageUtils.imageOrientationFixer(imageBitmap, currentPhotoPath);

        reportStolenViewModel.setPaintingToUpload(imageBitmap);

        picked = true;

    }

    /**
     * Creates the request body, sends it to the server and handles response.
     */
    private void uploadToServer() {

        if (currentPhotoPath == null) {
            Toast.makeText(getContext(), "Please select an image to upload.", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(currentPhotoPath);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        // add another part within the multipart request
        RequestBody paintingName =
                RequestBody.create(MediaType.parse("multipart/form-data"), paintingNameEditText.getText().toString());


        ReportStolenService service = APIClient.getRetrofitInstance().create(ReportStolenService.class);


        Call<ResponseBody> call = service.searchByPainting(body, paintingName);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                ResponseBody res = response.body();
                Toast.makeText(getContext(), "Server responded.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.e("Ex", "Exception: " + Log.getStackTraceString(t));
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });


    }


}