package com.example.basto.recognizer;

import android.Manifest;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecognitionListener {

    Button btn;
    TextView textview;
    ProgressBar progressBar;
    int REQUEST_PERMISSION_KEY = 1;
    SpeechRecognizer speech = null;
    Intent recognizerIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        textview = (TextView) findViewById(R.id.textview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        String[] permission = {Manifest.permission.RECORD_AUDIO};
        if (!Function.hasPermissions(this, permission)) {
            ActivityCompat.requestPermissions(this, permission, REQUEST_PERMISSION_KEY);
        }

        progressBar.setVisibility(View.INVISIBLE);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 10000);
        recognizerIntent.putExtra("android.speech.extra.DICTATION_MODE", true);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                speech.startListening(recognizerIntent);
                btn.setEnabled(false);

                /*To stop listening
                    progressBar.setVisibility(View.INVISIBLE);
                    speech.stopListening();
                    recordbtn.setEnabled(true);
                 */


            }
        });
    }

        @Override
        public void onReadyForSpeech (Bundle bundle){
            Log.d("Log", "onReadyOfSpeech");
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        public void onBeginningOfSpeech () {
            Log.d("Log", "onBeginningOfSpeech");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onRmsChanged ( float rmsdB){
            Log.d("Log", "onRmsChanged: " + rmsdB);
            progressBar.setProgress((int) rmsdB);
        }

        @Override
        public void onBufferReceived ( byte[] buffer){
            Log.d("Log", "onBufferReceived: " + buffer);
        }

        @Override
        public void onEndOfSpeech () {
            Log.d("Log", "onEndOfSpeech");
            progressBar.setVisibility(View.INVISIBLE);
            btn.setEnabled(true);
        }

        @Override
        public void onError ( int errorCode){
            String errorMessage = getErrorText(errorCode);
            Log.d("Log", "FAILED " + errorMessage);
            progressBar.setVisibility(View.INVISIBLE);
            textview.setText(errorMessage);
            btn.setEnabled(true);
        }
        @Override
        public void onEvent ( int arg0, Bundle arg1){
            Log.d("Log", "onEvent");

        }

        @Override
        public void onResults (Bundle bundle){
            Log.d("Log", "onResults");
        }

        @Override
        public void onPartialResults (Bundle arg0){
            Log.d("Log", "onPartialResults");

            ArrayList<String> matches = arg0.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String text = "";
        /* To get all close matchs
        for (String result : matches)
        {
            text += result + "\n";
        }
        */
            text = matches.get(0); //  Remove this line while uncommenting above codes
            textview.setText(text);
        }


    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

}

