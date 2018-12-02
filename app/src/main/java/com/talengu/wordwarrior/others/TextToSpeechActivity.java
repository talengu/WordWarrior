

package com.talengu.wordwarrior.others;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.talengu.wordwarrior.R;

import java.util.Locale;


public class TextToSpeechActivity extends Activity implements TextToSpeech.OnInitListener {


    private TextToSpeech mTts;
    private Button mAgainButton,mclearButton;
    private EditText tEditText;
   // void showToast(CharSequence msg) {
   //     Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
   // }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_to_speech);

        // Initialize text-to-speech. This is an asynchronous operation.
        // The OnInitListener (second argument) is called after initialization completes.
        mTts = new TextToSpeech(this,this  // TextToSpeech.OnInitListener
            );

        // The button is disabled in the layout.
        // It will be enabled upon initialization of the TTS engine.
        mAgainButton = (Button) findViewById(R.id.speak_button);
        mclearButton = (Button) findViewById(R.id.clear_button);
        tEditText=(EditText)findViewById(R.id.Tedit);
        mAgainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sayHello(tEditText.getText().toString());
            }
        });
        mclearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tEditText.setText("");
            }
        });
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown!
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }

        super.onDestroy();
    }

    // Implements TextToSpeech.OnInitListener.
    public void onInit(int status) {
        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // Note that a language may not be available, and the result will indicate this.
            int result = mTts.setLanguage(Locale.US);
            // Try this someday for some interesting results.
            // int result mTts.setLanguage(Locale.FRANCE);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
               // Lanuage data is missing or the language is not supported.
               
            } else {
                // Check the documentation for other possible result codes.
                // For example, the language may be available for the locale,
                // but not for the specified country and variant.

                // The TTS engine has been successfully initialized.
                // Allow the user to press the button for the app to speak again.
                mAgainButton.setEnabled(true);
                // Greet the user.
              
            }
        } else {
            // Initialization failed.
            
        }
    }

     private void sayHello(String hello) {
         Intent intent = new Intent();
         intent.setAction(Intent.ACTION_PROCESS_TEXT);
         intent.setType("text/plain");
         intent.putExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, true);
         intent.putExtra(Intent.EXTRA_PROCESS_TEXT, hello);
         startActivity(intent);
      // tts 在我的手机上不工作
//        mTts.speak(hello,
//            TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
//            null);
    }



     
     

}
