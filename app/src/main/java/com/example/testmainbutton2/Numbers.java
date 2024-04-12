package com.example.testmainbutton2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Numbers extends AppCompatActivity {
    EditText number1, number2, number3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numbers_activity);
        number1 = findViewById(R.id.inputNumber);
        number2 = findViewById(R.id.inputNumber2);
        number3 = findViewById(R.id.inputNumber3);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        number1.setText(sharedPreferences.getString("phoneNumber1", ""));
        number2.setText(sharedPreferences.getString("phoneNumber2", ""));
        number3.setText(sharedPreferences.getString("phoneNumber3", ""));

        Button okButton = findViewById(R.id.button_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Сохраняем введенные номера
                saveNumbers();
                // Возвращаемся в предыдущую активность
                finish();
            }
        });
    }

    private void saveNumbers() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("phoneNumber1", number1.getText().toString().trim());
            editor.putString("phoneNumber2", number2.getText().toString().trim());
            editor.putString("phoneNumber3", number3.getText().toString().trim());
            editor.apply();
            Toast.makeText(Numbers.this, "Numbers saved successfully", Toast.LENGTH_SHORT).show();
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Numbers.this, "Failed to save numbers", Toast.LENGTH_SHORT).show();
        }
    }
}
