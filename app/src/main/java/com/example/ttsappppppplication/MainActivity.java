package com.example.ttsappppppplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener{
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context;
        TextToSpeech.OnInitListener listener;
        tts = new TextToSpeech(this,this);
    }

    @Override
    public void onInit(int status) {
        if (TextToSpeech.SUCCESS == status) {
            Locale locale = Locale.ENGLISH;
            if (tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                tts.setLanguage(locale);
                Log.d("", "Error OK");

                tts.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                    @Override
                    public void onUtteranceCompleted(String utteranceId){
                        Log.d("tag", "Speech Completed! :" + utteranceId);
                    }

                });
            } else {
                Log.d("", "Error SetLocale");
            }
        } else {
            Log.d("", "Error Init");
        }
    }


    public void onClick(View v){
        EditText editText = findViewById(R.id.editText);
        String text = editText.getText().toString();
        Log.d("onClick",text);
        if(0 < text.length()){
            if(tts.isSpeaking()){
                tts.stop();
                return;
            }
            setSpeechRate(1.0f);
            setSpeechPitch(1.0f);

            if(Build.VERSION.SDK_INT >= 21){
                tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,"messageID");
            }
            else{
                HashMap<String,String> map = new HashMap<String, String>();
                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"messageID");
                tts.speak(text,TextToSpeech.QUEUE_FLUSH,map);
            }

        }
    }

    public void downloadfile(View v){
        Log.d("aa","donwload");

        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Log.d("downloadfile", String.valueOf(file));
        Log.d("downloadfile", String.valueOf(tts));
        HashMap<String, String> params = new HashMap<String, String>();

        File fileWriter = new File(file+"/speak.wav");
        try {
            fileWriter.createNewFile();
            int r = tts.synthesizeToFile("hello", params, fileWriter.getPath());

            if(r == TextToSpeech.SUCCESS) Toast.makeText(this,"save success！",Toast.LENGTH_LONG);
            Log.d("Error", "Errored");
        } catch (IOException e) {
            e.printStackTrace();
        }



//        HashMap<String, String> myHashRender = new HashMap();
//        String wakeUpText = "Are you up yet?";
//        String destFileName = file.getPath()+"/wakeUp.wav";
//        Log.d("path",destFileName);
//        myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, wakeUpText);
//        tts.synthesizeToFile(wakeUpText, myHashRender, destFileName);
//        Log.d("tts", String.valueOf(TextToSpeech.SUCCESS));
    }


    //speak speed
    private void setSpeechRate(float rate){
        if(null != tts){
            tts.setSpeechRate(rate);
        }
    }

    private void setSpeechPitch(float pitch){
        if(null != tts){
            tts.setPitch(pitch);
        }
    }


}
