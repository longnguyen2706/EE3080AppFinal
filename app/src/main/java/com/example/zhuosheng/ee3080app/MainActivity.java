package com.example.zhuosheng.ee3080app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.rmtheis.yandtran.translate.Translate;
import com.rmtheis.yandtran.language.Language;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import static com.rmtheis.yandtran.ApiKeys.YANDEX_API_KEY;

public class MainActivity extends AppCompatActivity {

    Button btnSendPicture,btnCapture, btnReadResult, btnTranslate, btnReadTranslate;
    EditText IPAddress;
    TextView ShowResult;
    Spinner LangSpinner;
    int TAKE_PHOTO_CODE = 0;
    int CAMERA_ACTIVITY_CODE = 100;
    TextToSpeech t1;
    String TranslatedText = "";
    Language DestLanguage = Language.ENGLISH;
    Locale DestLocale = Locale.UK;
    String ChosenLanguage = "English";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 0;


        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        btnSendPicture = (Button) findViewById(R.id.btnSendPicture);
        btnCapture = (Button) findViewById(R.id.btnCapturePicture);
        btnReadResult = (Button) findViewById(R.id.btnReadResult);
        btnTranslate =(Button) findViewById(R.id.btnTranslate);
        btnReadTranslate =(Button) findViewById(R.id.btnReadTranslate);
        IPAddress = (EditText) findViewById(R.id.IPaddress);
        ShowResult = (TextView) findViewById(R.id.ResultText);
        LangSpinner = (Spinner) findViewById(R.id.LangSpinner);


        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    addItemsOnLangSpinner();
                }else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });



        btnSendPicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*String ip = IPAddress.getText().toString();
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.dog);
                Client myClient = new Client(ip, 5000, ShowResult, bm, 10000);
                myClient.execute();*/
                Intent cameraIntent = new Intent(MainActivity.this, Camera.class);
                cameraIntent.putExtra("IPAddress", IPAddress.getText().toString());
                startActivityForResult(cameraIntent, CAMERA_ACTIVITY_CODE);
            }
        });

        btnCapture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

            }
        });

        btnReadResult.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String toSpeak = ShowResult.getText().toString();
                t1.setLanguage(Locale.UK);
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);

            }
        });

        btnTranslate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String toTranslate = ShowResult.getText().toString();
                ChosenLanguage = LangSpinner.getSelectedItem().toString();
                DestLanguage = Language.valueOf(ChosenLanguage.toUpperCase());
                TranslatedText = TranslateText(toTranslate, DestLanguage);
                Toast.makeText(MainActivity.this,TranslatedText, Toast.LENGTH_SHORT).show();
            }
        });

        btnReadTranslate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DestLocale = new Locale(DestLanguage.toString());
                t1.setLanguage(DestLocale);
                t1.speak(TranslatedText, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Uri u = data.getData();
            CropImage.activity(u).setGuidelines(CropImageView.Guidelines.ON).start(this);

        }

       if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    String ip = IPAddress.getText().toString();
                    PostTaskListener <String> postTaskListener = new PostTaskListener<String>() {
                        @Override
                        public void onPostTask(String result) {
                            ShowResult.setText(result);
                        }
                    };
                    Client myClient = new Client(postTaskListener, ip, 5000, photo, 5000);
                    myClient.execute();
                }catch(IOException e){
                    Log.e("ImageUploader", "Error cropping image", e);
                    Toast.makeText(this,"Error retrieving cropped image",Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this,"Error cropping image",Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == CAMERA_ACTIVITY_CODE && resultCode == RESULT_OK){
            String ip = IPAddress.getText().toString();
            Uri imageUri = data.getParcelableExtra("imageUri");
            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                PostTaskListener <String> postTaskListener = new PostTaskListener<String>() {
                    @Override
                    public void onPostTask(String result) {
                        ShowResult.setText(result);
                    }
                };
                Client myClient = new Client(postTaskListener, ip, 5000, photo, 5000);
                myClient.execute();
            }catch(IOException e){
                Log.e("ImageUploader", "Error sending image", e);
            }
        }


    }

    String TranslateText(String toTranslate, Language destLanguage){
        try {
            Translate.setKey(YANDEX_API_KEY);
            return Translate.execute(toTranslate, Language.ENGLISH, destLanguage);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void addItemsOnLangSpinner() {

        LangSpinner = (Spinner) findViewById(R.id.LangSpinner);

        Set<Locale> locales = t1.getAvailableLanguages();
        ArrayList<String> languages = new ArrayList<String>();
        for (Locale locale : locales) {
            String language = locale.getDisplayLanguage();
                if (language.trim().length() > 0 && !languages.contains(language) && Language.contains(language))
                    if(!language.equalsIgnoreCase("English"))
                        languages.add(language);
        }
        Collections.sort(languages);
        for (String language : languages) {
            System.out.println(language);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        LangSpinner.setAdapter(dataAdapter);
    }
}

