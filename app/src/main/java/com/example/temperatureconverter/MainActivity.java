package com.example.temperatureconverter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView farenheit, kelvin;
    EditText celcius;
    SeekBar seekbar;
    private boolean shouldUpdateSeekBar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        celcius = findViewById(R.id.celcius);
        farenheit = findViewById(R.id.farenheit);
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
                    updateValuesforCelcius(celcius.getText().toString());
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

        // Update SeekBar based on Celsius input (allowing values beyond 0-100 range)
        shouldUpdateSeekBar = false; // Disable SeekBar updates temporarily
        seekbar.setProgress((int) celsiusValue);
        shouldUpdateSeekBar = true; // Enable SeekBar updates again
    }
}
