package com.example.recipebook_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class EditRecordActivity extends AppCompatActivity {

    ImageView pImageView;
    EditText pTitle;
    EditText pIngredients;
    EditText pSteps;
    MaterialButton pSaveInfo;
    ProgressBar progressRound;
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int STORAGE_REQUEST_CODE = 101;

    public static final int IMAGE_PICK_CAMERA_CODE = 102;
    public static final int IMAGE_PICK_GALLERY_CODE = 103;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri imageUri;

    ActionBar actionBar;

    private String id, title, ingredients, steps, addTimeStamp, updateTimeStamp;
    private boolean editMode = false;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);


        pSaveInfo = findViewById(R.id.buttonSave);
        pImageView = findViewById(R.id.imageViewAdd);
        pTitle = findViewById(R.id.title);
        pIngredients = findViewById(R.id.ingredients);
        pSteps = findViewById(R.id.steps);
        progressRound = findViewById(R.id.progressRound);
        progressRound.setVisibility(View.GONE);

        actionBar = getSupportActionBar();

        Intent intent = getIntent();
        editMode = intent.getBooleanExtra("editMode", editMode);
        id = intent.getStringExtra("ID");
        title = intent.getStringExtra("TITLE");
        ingredients = intent.getStringExtra("INGREDIENTS");
        steps = intent.getStringExtra("STEPS");
        imageUri = Uri.parse(intent.getStringExtra("IMAGE"));
        addTimeStamp = intent.getStringExtra("ADD_TIMESTAMP");
        updateTimeStamp = intent.getStringExtra("UPDATE_TIMESTAMP");

        if(editMode){
            actionBar.setTitle("Update Recipe");
            editMode = intent.getBooleanExtra("editMode", editMode);
            id = intent.getStringExtra("ID");
            title = intent.getStringExtra("TITLE");
            ingredients = intent.getStringExtra("INGREDIENTS");
            steps = intent.getStringExtra("STEPS");
            imageUri = Uri.parse(intent.getStringExtra("IMAGE"));
            addTimeStamp = intent.getStringExtra("ADD_TIMESTAMP");
            updateTimeStamp = intent.getStringExtra("UPDATE_TIMESTAMP");

            pTitle.setText(title);
            pIngredients.setText(ingredients);
            pSteps.setText(steps);

            if(imageUri.toString().equals("null")){
                pImageView.setImageResource(R.drawable.icon3);
            }
            else{
                pImageView.setImageURI(imageUri);
            }
        }
        else{
            actionBar.setTitle("Add Recipe");
        }

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        dbHelper = new DatabaseHelper(this);


        pImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog();
            }
        });

        pSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressRound.setVisibility(View.VISIBLE);
                getData();
                startActivity(new Intent(EditRecordActivity.this, MainActivity.class));
                progressRound.setVisibility(View.GONE);
                Toast.makeText(EditRecordActivity.this, "Updated Successfully", Toast.LENGTH_LONG).show();
            }
        });

        pIngredients.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int after) {
                if(after > before){
                    if(text.toString().length()==1){
                        text = "▪ "+ text;
                        pIngredients.setText(text);
                        pIngredients.setSelection(pIngredients.getText().length());
                    }
                    if(text.toString().endsWith("\n")){
                        text=text.toString().replace("\n","\n▪ ");
                        text=text.toString().replace("▪ ▪","▪");
                        pIngredients.setText(text);
                        pIngredients.setSelection(pIngredients.getText().length());
                    }
                }

            }
        });

        pSteps.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int after) {
                if(after > before){
                    if(text.toString().length()==1){
                        text = "▪ "+ text;
                        pSteps.setText(text);
                        pSteps.setSelection(pSteps.getText().length());
                    }
                    if(text.toString().endsWith("\n")){
                        text=text.toString().replace("\n","\n▪ ");
                        text=text.toString().replace("▪ ▪","▪");
                        pSteps.setText(text);
                        pSteps.setSelection(pSteps.getText().length());
                    }
                }
            }
        });

    }

    private void getData() {
        String title = ""+pTitle.getText().toString().trim();
        String ingredients = ""+pIngredients.getText().toString().trim();
        String steps = ""+pSteps.getText().toString().trim();

        if(imageUri.toString().equals("")){
            Toast.makeText(EditRecordActivity.this, "Add Image", Toast.LENGTH_LONG).show();
        }
        else if(title.equals("")){
            Toast.makeText(EditRecordActivity.this, "Add Title", Toast.LENGTH_LONG).show();
        }
        else if(ingredients.equals("")){
            Toast.makeText(EditRecordActivity.this, "Add Ingredients", Toast.LENGTH_LONG).show();
        }
        else if(steps.equals("")){
            Toast.makeText(EditRecordActivity.this, "Add Instructions", Toast.LENGTH_LONG).show();
        }
        else if(!title.equals("") && !ingredients.equals("") && !steps.equals("") && !imageUri.toString().equals("")) {

            if (editMode) {
                String newUpdateTime = "" + System.currentTimeMillis();

                dbHelper.updateInfo(
                        "" + id, "" + title, "" + imageUri,
                        "" + ingredients, "" + steps,
                        "" + addTimeStamp, "" + newUpdateTime
                );
            } else {
                String timeStamp = "" + System.currentTimeMillis();

                dbHelper.insertInfo(
                        "" + title,
                        "" + imageUri,
                        "" + ingredients,
                        "" + steps,
                        "" + timeStamp,
                        "" + timeStamp
                );
            }
        }
    }

    private void imagePickDialog(){

        String[] options = {"Camera", "Gallery"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select for image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    if(!checkCameraPermissionGranted()){
                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }
                }
                else if (which==1){
                    if(!checkStoragePermissionGranted()){
                        requestStoragePermission();
                    }
                    else {
                        pickFromStorage();
                    }
                }

            }
        });
        builder.create().show();

    }

    private void pickFromStorage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);


    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image Description");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }

    private boolean checkStoragePermissionGranted(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);


    }

    private boolean checkCameraPermissionGranted(){
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);

        return result1 && result2;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);


    }

    public void doToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_REQUEST_CODE: {
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted){
                        pickFromCamera();
                    }

                    else {
                        doToast("Camera permission is required");
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if(grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickFromStorage();
                    }
                    else {
                        doToast("Toast Permission required");
                    }
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }

            else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK ){
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    Picasso.get().load(resultUri).into(pImageView);
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    Exception error = result.getError();
                    doToast(""+ error);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}