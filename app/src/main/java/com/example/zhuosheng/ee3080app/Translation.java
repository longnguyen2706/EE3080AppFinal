package com.example.zhuosheng.ee3080app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.rmtheis.yandtran.translate.Translate;
import com.rmtheis.yandtran.language.Language;

import static com.rmtheis.yandtran.ApiKeys.YANDEX_API_KEY;

/**
 * Created by Long Nguyen on 7/5/2017.
 */

public class Translation extends AsyncTask<Void, Void, String> {
    private Context context;
    ProgressDialog progDailog;
    String TranslatedText_r1, TranslatedText_r2, TranslatedText_r3, TranslatedText_r4, TranslatedText_r5;
    String toTranslate_r1, toTranslate_r2, toTranslate_r3, toTranslate_r4, toTranslate_r5;
    Language DestLanguage;
    String translationResult = "";
    private PostTaskListener<String> postTaskListener;

    Translation(PostTaskListener<String> postTaskListener, String toTranslate_r1, String toTranslate_r2,
                String toTranslate_r3, String toTranslate_r4, String toTranslate_r5, Language DestLanguage, Context ctx) {

        this.postTaskListener = postTaskListener;
        this.toTranslate_r1 = toTranslate_r1;
        this.toTranslate_r2 = toTranslate_r2;
        this.toTranslate_r3 = toTranslate_r3;
        this.toTranslate_r4 = toTranslate_r4;
        this.toTranslate_r5 = toTranslate_r5;
        this.DestLanguage = DestLanguage;
        context = ctx;
        progDailog = new ProgressDialog(context);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progDailog.setMessage("Translating ...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
    }

    @Override
    protected String doInBackground(Void... arg0) {

        TranslatedText_r1 = TranslateText(toTranslate_r1, DestLanguage);
        TranslatedText_r2 = TranslateText(toTranslate_r2, DestLanguage);
        TranslatedText_r3 = TranslateText(toTranslate_r3, DestLanguage);
        TranslatedText_r4 = TranslateText(toTranslate_r4, DestLanguage);
        TranslatedText_r5 = TranslateText(toTranslate_r5, DestLanguage);

        translationResult = translationResult + TranslatedText_r1 + "," + TranslatedText_r2 + "," +
                TranslatedText_r3 + "," + TranslatedText_r4 + "," + TranslatedText_r5;

        return translationResult;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null && postTaskListener != null) {
            postTaskListener.onPostTask(result);
        }
        progDailog.dismiss();
    }


    String TranslateText(String toTranslate, Language destLanguage) {
        try {
            Translate.setKey(YANDEX_API_KEY);
            return Translate.execute(toTranslate, Language.ENGLISH, destLanguage);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
