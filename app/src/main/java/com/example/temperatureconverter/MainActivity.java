package com.example.temperatureconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView farenheit, kelvin;
    EditText celcius;
    ImageView thermo;
    SeekBar seekbar;
    private boolean shouldUpdateSeekBar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        celcius = findViewById(R.id.celcius);
        farenheit = findViewById(R.id.farenheit);
        thermo=findViewById(R.id.thermo);
        kelvin = findViewById(R.id.kelvin);
        seekbar = findViewById(R.id.seekbar);

        // Initialize Celsius EditText with default hint value
        seekbar.setProgress(0);
        celcius.setHint("0");
        farenheit.setHint("32");
        kelvin.setHint("273.15");

        celcius.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if(Double.parseDouble(celcius.getText().toString())< -273.15)
                    {
                        Toast.makeText(MainActivity.this, "0 Kelvin is the lowest possible temperature", Toast.LENGTH_SHORT).show();
                        celcius.setText("-273.15");
                        updateValuesforCelcius(celcius.getText().toString());
                        celcius.clearFocus();
                    }
                    else{
                        updateValuesforCelcius(celcius.getText().toString());
                        celcius.clearFocus();
                    }
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        // Set SeekBar listener to update Celsius EditText
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (shouldUpdateSeekBar) {
                    celcius.setText(String.valueOf(progress));
                    updateValuesforCelcius(celcius.getText().toString());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateValuesforCelcius(String celsiusString) {
        if (celsiusString.isEmpty()) {
            return;
        }

        // Convert Celsius to Fahrenheit and Kelvin
        double celsiusValue = Double.parseDouble(celsiusString);
        double fahrenheitValue = (celsiusValue * 9/5) + 32;
        double kelvinValue = celsiusValue + 273.15;

        // Update EditTexts for Fahrenheit and Kelvin
        farenheit.setText(String.format("%.2f", fahrenheitValue));
        kelvin.setText(String.format("%.2f", kelvinValue));
        if(celsiusValue>100.0)
        {
            vibratePhone();
            thermo.setImageResource(R.drawable.thermometerbroken);
        }
        else {
            thermo.setImageResource(R.drawable.thermometer);
        }

        // Update SeekBar based on Celsius input (allowing values beyond 0-100 range)
        shouldUpdateSeekBar = false; // Disable SeekBar updates temporarily
        seekbar.setProgress((int) celsiusValue);
        shouldUpdateSeekBar = true; // Enable SeekBar updates again
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(celcius.getWindowToken(), 0);
        }
    }

    private void vibratePhone() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            // Vibrate for 500 milliseconds (0.5 seconds)
            vibrator.vibrate(500);
        }
    }
}
