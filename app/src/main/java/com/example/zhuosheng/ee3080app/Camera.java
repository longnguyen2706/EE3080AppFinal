package com.example.zhuosheng.ee3080app;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.flurgle.camerakit.CameraKit;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Camera extends AppCompatActivity {

    CameraView camera;
    RadioGroup captureModeRadioGroup;
    RadioGroup flashModeRadioGroup;
    ImageButton btnCapture, btnLive, btnToggleCamera;
    TextView PredictedText;
    FrameLayout Flashlay;
    int LIVE_MODE = 0, CAMERA_MODE = 0;
    int CAMERA_RESULT_ACTIVITY = 10;
    int TAKE_PHOTO_CODE = 1;
    String ip;
    Client myClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 0;


        if (ContextCompat.checkSelfPermission(Camera.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(Camera.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(Camera.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            }
        }

        camera = (CameraView) findViewById(R.id.camera);
        captureModeRadioGroup = (RadioGroup) findViewById(R.id.captureModeRadioGroup);
        flashModeRadioGroup = (RadioGroup) findViewById(R.id.flashModeRadioGroup);
        btnToggleCamera = (ImageButton) findViewById(R.id.toggleCamera);
        btnLive = (ImageButton) findViewById(R.id.livemode);
        btnCapture = (ImageButton) findViewById(R.id.capturePhoto);
        PredictedText = (TextView) findViewById(R.id.PredictionText);
        //Bundle extras = getIntent().getExtras();
        ip = "192.168.1.1";//extras.getString("IPAddress");
        Flashlay = (FrameLayout) findViewById(R.id.Framelay);

        captureModeRadioGroup.setOnCheckedChangeListener(captureModeChangedListener);
        flashModeRadioGroup.setOnCheckedChangeListener(flashModeChangedListener);

        btnCapture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (LIVE_MODE == 0) {
                    Toast.makeText(Camera.this, "Processing Image, Please Wait...", Toast.LENGTH_SHORT).show();
                    btnCapture.setEnabled(false);
                    CAMERA_MODE = 1;
                    camera.captureImage();

                } else {
                    Toast.makeText(Camera.this, "Live Mode is still on!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnLive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (CAMERA_MODE == 0) {
                    switch (LIVE_MODE) {
                        case 0:
                            LIVE_MODE = 1;
                            Toast.makeText(Camera.this, "Live Mode is initialised!", Toast.LENGTH_SHORT).show();
                            btnLive.setBackgroundColor(Color.RED);
                            camera.captureImage();
                            break;
                        case 1:
                            LIVE_MODE = 0;
                            Toast.makeText(Camera.this, "Live Mode is stopped!", Toast.LENGTH_SHORT).show();
                            btnLive.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                            break;
                    }
                } else {
                    Toast.makeText(Camera.this, "Camera mode is active!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnToggleCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (camera.toggleFacing()) {
                    case CameraKit.Constants.FACING_BACK:
                        Toast.makeText(Camera.this, "Switched to back camera!", Toast.LENGTH_SHORT).show();
                        break;

                    case CameraKit.Constants.FACING_FRONT:
                        Toast.makeText(Camera.this, "Switched to front camera!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        final PostTaskListener<String> postTaskListener = new PostTaskListener<String>() {
            @Override
            public void onPostTask(String result) {
                //Toast.makeText(Camera.this, result, Toast.LENGTH_SHORT).show();
                PredictedText.setText(result);
                camera.captureImage();
            }
        };


        camera.setCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] picture) {
                super.onPictureTaken(picture);
                // Create a bitmap
                Bitmap result = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                if (LIVE_MODE == 1 && CAMERA_MODE == 0) {
                    myClient = new Client(postTaskListener, ip, 5000, result, 5000);
                    myClient.execute();
                }
                if (CAMERA_MODE == 1 && LIVE_MODE == 0) {
                    String title = "tst";

                    File tempDir = Environment.getExternalStorageDirectory();
                    tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
                    tempDir.mkdir();
                    //Intent cameraIntent = new Intent(Camera.this, CameraResult.class);
                    //startActivityForResult(cameraIntent, CAMERA_RESULT_ACTIVITY);
                    try {

                        File tempFile = File.createTempFile(title, ".jpg", tempDir);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        result.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        byte[] bitmapData = bytes.toByteArray();

                        //write the bytes in file
                        FileOutputStream fos = new FileOutputStream(tempFile);
                        fos.write(bitmapData);
                        fos.flush();
                        fos.close();

                        CropImage.activity(Uri.fromFile(tempFile)).setGuidelines(CropImageView.Guidelines.ON).start(Camera.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        camera.start();
        btnCapture.setEnabled(true);
    }

    @Override
    protected void onPause() {
        camera.stop();
        LIVE_MODE = 0;
        CAMERA_MODE = 0;
        if(myClient != null) {
            if (myClient.getStatus() == Client.Status.RUNNING) {
                myClient.cancel(true);
            }
        }
        super.onPause();
    }

    RadioGroup.OnCheckedChangeListener captureModeChangedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            camera.setMethod(
                    checkedId == R.id.modeCaptureStandard ?
                            CameraKit.Constants.METHOD_STANDARD :
                            CameraKit.Constants.METHOD_STILL
            );

            if(checkedId == R.id.modeCaptureStandard){
                Flashlay.setVisibility(View.VISIBLE);
            }else{
                Flashlay.setVisibility(View.GONE);
                camera.setFlash(CameraKit.Constants.FLASH_OFF);
            }

            Toast.makeText(Camera.this, "Picture capture set to" + (checkedId == R.id.modeCaptureStandard ? " quality!" : " speed!"), Toast.LENGTH_SHORT).show();
        }
    };

    RadioGroup.OnCheckedChangeListener flashModeChangedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
                String FlashType = "";
                switch(checkedId) {
                    case R.id.modeFlashOn:
                        camera.setFlash(CameraKit.Constants.FLASH_ON);
                        FlashType = "Turned on";
                        break;
                    case R.id.modeFlashOff:
                        camera.setFlash(CameraKit.Constants.FLASH_OFF);
                        FlashType = "Turned off";
                        break;
                    case R.id.modeFlashAuto:
                        camera.setFlash(CameraKit.Constants.FLASH_AUTO);
                        FlashType = "set to Auto";
                        break;
                }

            Toast.makeText(Camera.this, "Flash " + FlashType, Toast.LENGTH_SHORT).show();
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE){
            Uri u = data.getData();
            CropImage.activity(u).setGuidelines(CropImageView.Guidelines.ON).start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();
                Intent intent = new Intent(Camera.this, CameraResult.class);
                intent.putExtra("imageUri", imageUri.toString());
                startActivity(intent);
                //setResult(RESULT_OK,intent);
                //finish();
                }

             else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error cropping image", Toast.LENGTH_LONG).show();
            }
        }


        }
    }
