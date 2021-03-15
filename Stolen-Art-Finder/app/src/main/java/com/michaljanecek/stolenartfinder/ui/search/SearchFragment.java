package com.michaljanecek.stolenartfinder.ui.search;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.Observable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.michaljanecek.stolenartfinder.R;
import com.michaljanecek.stolenartfinder.helpers.ImageUtils;
import com.michaljanecek.stolenartfinder.models.FoundPaintingModel;
import com.michaljanecek.stolenartfinder.models.Painting;
import com.michaljanecek.stolenartfinder.networking.APIClient;
import com.michaljanecek.stolenartfinder.networking.SearchDBService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static android.app.Activity.RESULT_OK;

public class SearchFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_PICK = 2;
    private static final int PICK_FROM_GALLERY_PERMISSION = 3;


    private SearchViewModel searchViewModel;

    private FloatingActionButton takePicButton;
    private FloatingActionButton uploadPicButton;
    private Button uploadToServerButton;
    private ImageView imageToSearch;
    private ProgressBar progressBar;
    private TextView progressBarLabel;
    private TextView instructionsView;

    String currentPhotoPath;
    List<FoundPaintingModel> foundPaintings;

    enum UploadPhase {
        PREUPLOAD,
        UPLOADING,
        DOWNLOADING_RESULT,
        POSTUPLOAD,
        FAILED,
        FRESH
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        takePicButton = root.findViewById(R.id.button_take_picture);
        uploadPicButton = root.findViewById(R.id.button_upload_picture);
        uploadToServerButton = root.findViewById(R.id.button_post_upload);
        imageToSearch = root.findViewById(R.id.image_to_search);
        progressBar = root.findViewById(R.id.progress_bar);
        progressBarLabel = root.findViewById(R.id.progress_bar_label);
        instructionsView = root.findViewById(R.id.instructions_view);

        setOnClickListeners();


        searchViewModel.getImageToSearch().observe(getViewLifecycleOwner(), image -> imageToSearch.setImageBitmap(image));

        searchViewModel.getFoundImages().observe(getViewLifecycleOwner(), this::newImageDownloaded);

        setUploadPhase(UploadPhase.FRESH);

        return root;
    }

    private void setOnClickListeners() {

        takePicButton.setOnClickListener(v -> dispatchTakePictureIntent());

        uploadPicButton.setOnClickListener(v -> {

            checkStoragePermissions();
            dispatchPickPictureIntent();

        });


        uploadToServerButton.setOnClickListener(v -> uploadToServer());

    }

    private void newImageDownloaded(List<Pair<Integer, Bitmap>> images) {


        if (images == null || images.isEmpty())
            return;

        setUploadPhase(UploadPhase.POSTUPLOAD);


        int foundPaintingId = images.get(0).first;
        Bitmap foundPainting = images.get(0).second;

        setUploadPhase(UploadPhase.FRESH);
        launchFoundPaintingDetailFragment(foundPainting, foundPaintingId);

    }

    private void downloadImage(String fromUrl, int id) {


        HandlerThread ht = new HandlerThread("MyHandlerThread");
        ht.start();
        Handler asyncHandler = new Handler(ht.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bitmap downloadedImage = (Bitmap) msg.obj;

                // Do things on UI thread HERE

                searchViewModel.updateFoundImages(downloadedImage, id);

            }
        };

        Runnable runnable = () -> {

            InputStream in = null;

            try {
                Log.i("URL", fromUrl);
                URL url = new URL(fromUrl);
                URLConnection urlConn = url.openConnection();
                HttpURLConnection httpConn = (HttpURLConnection) urlConn;
                httpConn.connect();

                in = httpConn.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap downloadedImage = BitmapFactory.decodeStream(in);

            Message message = new Message();
            message.obj = downloadedImage;
            asyncHandler.sendMessage(message);


        };
        asyncHandler.post(runnable);
    }

    private void paintingsFound() {

        if (foundPaintings == null || foundPaintings.isEmpty()) {

            //TODO tell the user that no matches were found, better
            Toast.makeText(getContext(), "Sorry, we do not recognize this painting.", Toast.LENGTH_SHORT).show();
            setUploadPhase(UploadPhase.POSTUPLOAD);
            return;
        }

        setUploadPhase(UploadPhase.DOWNLOADING_RESULT);

        for (FoundPaintingModel found : foundPaintings) {

            String imageUrl = found.getImageUrl();

            downloadImage(imageUrl, found.getId());

        }

    }


    private void uploadToServer() {

        setUploadPhase(UploadPhase.UPLOADING);


        File file = new File(currentPhotoPath);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        // add another part within the multipart request
        RequestBody fullName =
                RequestBody.create(MediaType.parse("multipart/form-data"), "Your Name");


        RequestBody k = RequestBody.create(MultipartBody.FORM, "1");

        SearchDBService service = APIClient.getRetrofitInstance().create(SearchDBService.class);


        Call<List<FoundPaintingModel>> call = service.searchByPainting(body, k);

        call.enqueue(new Callback<List<FoundPaintingModel>>() {
            @Override
            public void onResponse(Call<List<FoundPaintingModel>> call, Response<List<FoundPaintingModel>> response) {
                //  progressDoalog.dismiss();


                foundPaintings = response.body();
                paintingsFound();
            }

            @Override
            public void onFailure(Call<List<FoundPaintingModel>> call, Throwable t) {

                setUploadPhase(UploadPhase.FAILED);
                Log.e("Ex", "Exception: " + Log.getStackTraceString(t));
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void launchFoundPaintingDetailFragment(Bitmap foundPainting, int id){

        FoundPaintingDetailFragment nextFrag = new FoundPaintingDetailFragment();

        // the original instance returned from server (including all info - name, id, imageurl... but not the Bitmap)
        FoundPaintingModel fpm = null;

        for (FoundPaintingModel f: foundPaintings){
            if (f.getId()==id){
                fpm = f;
                break;
            }
        }

        Painting foundPaintingWithInfo = new Painting(foundPainting, fpm);

        Bundle b = new Bundle();
        b.putParcelable(FoundPaintingDetailFragment.foundPaintingParamKey, foundPaintingWithInfo);

        nextFrag.setArguments(b);


        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchPickPictureIntent() {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.michaljanecek.stolenartfinder.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }



    private void setUploadPhase(UploadPhase phase){

        switch (phase){
            case FRESH:
                imageToSearch.setVisibility(View.GONE);
                uploadToServerButton.setVisibility(View.INVISIBLE);
                instructionsView.setVisibility(View.VISIBLE);
                instructionsView.bringToFront();
                progressBar.setVisibility(View.INVISIBLE);
                progressBarLabel.setVisibility(View.INVISIBLE);
                break;

            case PREUPLOAD:
                instructionsView.setVisibility(View.INVISIBLE);
                imageToSearch.setVisibility(View.VISIBLE);
                uploadToServerButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                progressBarLabel.setVisibility(View.INVISIBLE);
                break;

            case UPLOADING:
                imageToSearch.setImageBitmap(null);
                progressBar.setVisibility(View.VISIBLE);
                progressBarLabel.setVisibility(View.VISIBLE);
                progressBarLabel.setText(getString(R.string.uploading_progress_label));
                break;

            case DOWNLOADING_RESULT:
                progressBar.setVisibility(View.VISIBLE);
                progressBarLabel.setVisibility(View.VISIBLE);
                progressBarLabel.setText(getString(R.string.downloading_progress_label));
                break;

            case POSTUPLOAD:
                progressBar.setVisibility(View.INVISIBLE);
                progressBarLabel.setVisibility(View.INVISIBLE);
                break;

            case FAILED:
                instructionsView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                progressBarLabel.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {

                    galleryAddPic();

                    updateImage();

                }
                break;

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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_FROM_GALLERY_PERMISSION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.v("Success", "Permissions granted");

                } else {
                    // TODO permission not granted, inform user about it
                }
                break;
        }
    }


    public void updateImage() {

        Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
        imageBitmap = ImageUtils.imageOrientationFixer(imageBitmap, currentPhotoPath);

        searchViewModel.setImageToSearch(imageBitmap);
        setUploadPhase(UploadPhase.PREUPLOAD);

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
    }

    public void checkStoragePermissions() {

        try {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY_PERMISSION);
            } else {
                // permissions are already granted
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}