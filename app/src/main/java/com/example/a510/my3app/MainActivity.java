package com.example.a510.my3app;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    protected Button btSpeech,btSpeak;//protected는 외부접근을 허락하기도 하고 안하기도 함 private와 public의 중간, 상속을 받았을때만 외부접근 허용
    protected TextView txSpeech;
    private static final int SPEECH_CODE = 1234;//메모리 관리 방식 설정 final(값을 변경안함,상수),static(한번만 메모리 할당),private(변수에 접근을 허락하지 않음)<->public
    protected TextToSpeech tts;

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS){
            tts.setLanguage(Locale.KOREAN);//한국말 설정
            tts.setPitch(0.5f);
            tts.setSpeechRate(1.0f);
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == SPEECH_CODE){
            if (resultCode == RESULT_OK && data != null){
                ArrayList<String> arrayList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);//getEXTRA로 받아옴 스트링어레이리스트로

                String strSpeech = arrayList.get(0);
                txSpeech.setText(strSpeech);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txSpeech = (TextView)findViewById(R.id.txSpeech);
        btSpeech =(Button)findViewById(R.id.btSpeech);
        btSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH); //구문분석해서 표시
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREAN);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Recognizeing...");
                startActivityForResult(intent,SPEECH_CODE);
            }
        });

        btSpeak = (Button) findViewById(R.id.btSpeak);
        btSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = txSpeech.getText().toString();
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
        tts = new TextToSpeech(this, this);
    }
}
