package com.example.text_to_speech;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Locale;

/**
 * The app provides a text to speech generator for user input.
 * This Activity creates a possibility for the user to put in text, pitch, speed and the language.
 * The program makes an audio output of the text with the values of pitch and speed.
 * It is possible to change the language by using the RadioButtons.
 *
 * Layout File: activity_main.xml
 *
 * @author Lukas Plenk
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextToSpeech textToSpeech;
    private EditText editText;
    private SeekBar seekBar_pitch;
    private SeekBar seekBar_speed;
    private RadioGroup radioGroup;
    private RadioButton selectedRadioButton;
    private Button button;

    /**
     * This method references different UI components.
     * The Button for submitting the input gets disabled.
     * It should only work of the initiation of the TextToSpeech was successful.
     * The TextToSpeech gets defined with an OnInitListener.
     * @param savedInstanceState is a standard parameter.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.edit_text);
        seekBar_pitch = findViewById(R.id.seekBar_pitch);
        seekBar_speed = findViewById(R.id.seekBar_speed);
        radioGroup = findViewById(R.id.radiogroup);
        button = findViewById(R.id.button);

        button.setEnabled(false);
        button.setOnClickListener(this);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            /**
             * This method handles every step that needs to be done while initializing the TextToSpeech.
             * The initializing and the support of the chosen language needs to be checked.
             * The language checking happens in the selectLanguage method.
             * @param status explains if the TextToSpeech itself is set up correctly.
             */
            @Override
            public void onInit(int status) {

                // Checking for correct initializing of the TextToSpeech
                if (status == TextToSpeech.SUCCESS) {

                    selectLanguage();
                }
                else {

                    Toast.makeText(MainActivity.this, "Couldn`t initialize Generator", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Method for checking the RadioButtons and selecting the language.
     * The int variable result takes the language.
     * In case that everything works as expected, the Button will be enabled.
     */
    private void selectLanguage() {
        
        int result;

        int radioId = radioGroup.getCheckedRadioButtonId();

        selectedRadioButton = findViewById(radioId);

        if (selectedRadioButton.getId() == R.id.rb_english) {

            result = textToSpeech.setLanguage(Locale.US);
        }
        else {

            result = textToSpeech.setLanguage(Locale.GERMAN);
        }

        // Checking if the language is not available on the phone of the user
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

            Toast.makeText(MainActivity.this, "Language not supported", Toast.LENGTH_LONG).show();
        }
        else {

            button.setEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {

        selectLanguage();
        transformTextToAudio();
    }

    /**
     * This method takes the pitch and speed values from the SeekBars.
     * The values get transformed and transferred to the TextToSpeech.
     * The TextToSpeech makes an output of the user input.
     * QUEUE_FLUSH means that the previous text gets canceled.
     */
    private void transformTextToAudio() {

        // The values need to get standardized
        float pitch = (float) seekBar_pitch.getProgress() / 50;
        float speed = (float) seekBar_speed.getProgress() / 50;

        textToSpeech.setPitch(pitch);
        textToSpeech.setSpeechRate(speed);

        textToSpeech.speak(editText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
    }
}