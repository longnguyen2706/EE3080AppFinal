package com.example.zhuosheng.ee3080app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.rmtheis.yandtran.language.Language;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class HistoryResult extends Activity {
    // Declare Variable
    TextView main_prediction, second_prediction, third_prediction, fourth_prediction, fifth_prediction;
    ImageView imageview;

    Spinner LangSpinner;
    Context c = this;
    Button btnTranslate;
    Bitmap photo;
    TakenPicture obj;
    TextToSpeech t1;
    String TranslatedText_r1, TranslatedText_r2, TranslatedText_r3, TranslatedText_r4, TranslatedText_r5;
    Language DestLanguage = Language.ENGLISH;
    Locale DestLocale = Locale.UK;
    String ChosenLanguage = "English";
    String main_result_en, second_result_en, third_result_en, fourth_result_en, fifth_result_en;
    String TAG = "HIstory_result";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from view_image.xml
        setContentView(R.layout.activity_history_result);

        main_prediction = (TextView) findViewById(R.id.main_prediction);
        second_prediction = (TextView) findViewById(R.id.second_prediction);
        third_prediction = (TextView) findViewById(R.id.third_prediction);
        fourth_prediction = (TextView) findViewById(R.id.fourth_prediction);
        fifth_prediction = (TextView) findViewById(R.id.fifth_prediction);
        imageview = (ImageView) findViewById(R.id.takenImage);
        btnTranslate = (Button) findViewById(R.id.btnTranslate);
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

        // Retrieve data from ListImageActivity on GridView item click
        Intent i = getIntent();
        List<TakenPicture> history = MyPreferences.loadSharedPreferencesLogList(this.getApplicationContext());
        // Get the position
        int position = i.getExtras().getInt("position");

        // Get String arrays FilePathStrings
        String[] filepath = i.getStringArrayExtra("filepath");

        // Get String arrays FileNameStrings
        String[] filename = i.getStringArrayExtra("filename");

        main_result_en = history.get(position).getMainName();
        second_result_en = history.get(position).getSuggestion()[0];
        third_result_en = history.get(position).getSuggestion()[1];
        fourth_result_en = history.get(position).getSuggestion()[2];
        fifth_result_en = history.get(position).getSuggestion()[3];

        second_prediction.setText(second_result_en);
        third_prediction.setText(third_result_en);
        fourth_prediction.setText(fourth_result_en);
        fifth_prediction.setText(fifth_result_en);
        // Locate the TextView in view_image.xml
        main_prediction.setText(main_result_en);
        // Locate the ImageView in view_image.xml

        // Decode the filepath with BitmapFactory followed by the position
//        Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);
        Bitmap bmp = BitmapFactory.decodeFile(history.get(position).getLinkToPicture());
        // Set the decoded bitmap into ImageView
        imageview.setImageBitmap(bmp);

        main_prediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toSpeak = main_prediction.getText().toString();
                t1.setLanguage(DestLocale);
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        second_prediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toSpeak = second_prediction.getText().toString();
                t1.setLanguage(DestLocale);
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        third_prediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toSpeak = third_prediction.getText().toString();
                t1.setLanguage(DestLocale);
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        fourth_prediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toSpeak = fourth_prediction.getText().toString();
                t1.setLanguage(DestLocale);
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        fifth_prediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toSpeak = fifth_prediction.getText().toString();
                t1.setLanguage(DestLocale);
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        btnTranslate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String toTranslate_r1 = main_result_en;
                String toTranslate_r2 = second_result_en;
                String toTranslate_r3 = third_result_en;
                String toTranslate_r4 = fourth_result_en;
                String toTranslate_r5 = fifth_result_en;

                ChosenLanguage = LangSpinner.getSelectedItem().toString();
                DestLanguage = Language.valueOf(ChosenLanguage.toUpperCase());
                DestLocale = new Locale(DestLanguage.toString());
                PostTaskListener<String> postTaskListenerforTranslate = new PostTaskListener<String>() {
                    @Override
                    public void onPostTask(String result) {
                        String[] translation_arr = result_split(result);
                        setResultTextView(translation_arr[0], translation_arr[1], translation_arr[2], translation_arr[3], translation_arr[4]);
                    }

                };
                Translation translation = new Translation(postTaskListenerforTranslate, toTranslate_r1, toTranslate_r2, toTranslate_r3,
                        toTranslate_r4, toTranslate_r5, DestLanguage, c);
                translation.execute();
            }

        });

    }


    public void addItemsOnLangSpinner() {

        LangSpinner = (Spinner) findViewById(R.id.LangSpinner);

        Set<Locale> locales = t1.getAvailableLanguages();
        ArrayList<String> languages = new ArrayList<String>();
        for (Locale locale : locales) {
            String language = locale.getDisplayLanguage();
            if (language.trim().length() > 0 && !languages.contains(language) && Language.contains(language))
                if (!language.equalsIgnoreCase("English"))
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

    public String[] result_split(String result) {
        String[] result_arr = result.split(",");
        return result_arr;
    }

    public void setResultTextView(String r1, String r2, String r3, String r4, String r5) {
        main_prediction.setText(r1);
        second_prediction.setText(r2);
        third_prediction.setText(r3);
        fourth_prediction.setText(r4);
        fifth_prediction.setText(r5);
    }

}