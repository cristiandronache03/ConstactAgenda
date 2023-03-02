package com.example.contactagenda;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddContact extends AppCompatActivity {

    private ImageView profiveIv;
    private EditText nameEt,phoneEt,emailEt,noteEt;

    private FloatingActionButton fab;

    String name, phone, email, note;

    ActionBar actionBar;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 200;
    private static final int IMAGE_FROM_GALLERY_CODE = 300;
    private static final int IMAGE_FROM_CAMERA_CODE = 400;

    private String[] cameraPermission;
    private String[] storagePermission;

    Uri imageUri;
    final int PIC_CROP = 2;
    final int CAMERA_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        cameraPermission = new String[]{android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        actionBar = getSupportActionBar();

        actionBar.setTitle("Add Contact");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        profiveIv = findViewById(R.id.profileIv);
        nameEt = findViewById(R.id.nameEt);
        phoneEt = findViewById(R.id.phoneEt);
        emailEt = findViewById(R.id.emailEt);
        noteEt = findViewById(R.id.noteEt);
        fab = findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        profiveIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });

    }

    private void showImagePickerDialog(){
        String options[] = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Choose an option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    if(!checkCameraPermission()) {
                        requestCameraPermission();
                    }else{
                        pickFromCamera();
                    }
                }else if(which == 1){
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        pickFromGallery();
                    }
                }

            }
        }).create().show();
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");

        startActivityForResult(galleryIntent,IMAGE_FROM_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"IMAGE_TITLE");
        values.put(MediaStore.Images.Media.DESCRIPTION,"IMAGE_DETAIL");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);

        startActivityForResult(cameraIntent, IMAGE_FROM_CAMERA_CODE);
    }

    private void saveData() {

        name = nameEt.getText().toString();
        phone = phoneEt.getText().toString();
        email = emailEt.getText().toString();
        note = noteEt.getText().toString();

        if(name.isEmpty() || phone.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please fill in the fields...",Toast.LENGTH_SHORT).show();
        }
        else{

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result & result1;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_PERMISSION_CODE);
    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_PERMISSION_CODE:
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted){
                        pickFromCamera();
                    }else{
                        Toast.makeText(getApplicationContext(),"Camera & Storage Permission neded..",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_PERMISSION_CODE:
                if(grantResults.length > 0){
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(storageAccepted){
                        pickFromGallery();
                    }else{
                        Toast.makeText(getApplicationContext(),"Storage Permission neded..",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_FROM_GALLERY_CODE){
                imageUri = data.getData();
                performCrop();
            }else if(requestCode == IMAGE_FROM_CAMERA_CODE){
                imageUri = data.getData();
                performCrop();


            }else if(requestCode == PIC_CROP) {
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                profiveIv.setImageBitmap(thePic);
            }
        }
    }

    private void performCrop() {
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(imageUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


}