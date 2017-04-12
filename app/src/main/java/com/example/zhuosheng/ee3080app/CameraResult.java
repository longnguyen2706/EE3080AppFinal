package com.example.zhuosheng.ee3080app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ImageView;
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

public class CameraResult extends AppCompatActivity {
    TextView main_prediction, second_prediction, third_prediction, fourth_prediction, fifth_prediction;
    ImageView takenImage;
    Spinner LangSpinner;
    Button btnTranslate;
    Bitmap photo;
    int TAKE_PHOTO_CODE = 0;
    int CAMERA_ACTIVITY_CODE = 100;
    String IPAddress;
    TextToSpeech t1;
    String TranslatedText = "";
    Language DestLanguage = Language.ENGLISH;
    Locale DestLocale = Locale.UK;
    String ChosenLanguage = "English";
    String result_en;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_result);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 0;

        if (ContextCompat.checkSelfPermission(CameraResult.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(CameraResult.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(CameraResult.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }
        }

            main_prediction = (TextView) findViewById(R.id.main_prediction);
            second_prediction = (TextView) findViewById(R.id.second_prediction);
            third_prediction = (TextView) findViewById(R.id.third_prediction);
            fourth_prediction = (TextView) findViewById(R.id.fourth_prediction);
            fifth_prediction = (TextView) findViewById(R.id.fifth_prediction);
            takenImage = (ImageView) findViewById(R.id.takenImage);
            btnTranslate =(Button) findViewById(R.id.btnTranslate);
            LangSpinner = (Spinner) findViewById(R.id.LangSpinner);

            t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        addItemsOnLangSpinner();
                    } else {
                        Log.e("TTS", "Initialization failed");
                    }
                }
            });

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String imageUriString = extras.getString("imageUri");
                Uri imageUri = Uri.parse(imageUriString);
                try {
                    photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    takenImage.setImageBitmap(photo);
                } catch (IOException e) {
                    Log.e("ImageBitmap", "Cannot save image as bitmap", e);
                }

                String ip = IPAddress;
                PostTaskListener<String> postTaskListener = new PostTaskListener<String>() {
                    @Override
                    public void onPostTask(String result) {
                        if(result.startsWith("Exception")) {
                            Toast.makeText(CameraResult.this, "Error has occurred", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            result_en = result;
                            main_prediction.setText(result);
                        }

                    }
                };
                Client myClient = new Client(postTaskListener, ip, 5000, photo, 5000);
                myClient.execute();
            }

            main_prediction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String toSpeak = main_prediction.getText().toString();
                    t1.setLanguage(DestLocale);
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            });

            btnTranslate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String toTranslate = result_en;
                ChosenLanguage = LangSpinner.getSelectedItem().toString();
                DestLanguage = Language.valueOf(ChosenLanguage.toUpperCase());
                TranslatedText = TranslateText(toTranslate, DestLanguage);
                main_prediction.setText(TranslatedText);
                Toast.makeText(CameraResult.this,TranslatedText, Toast.LENGTH_SHORT).show();
            }
        });


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
