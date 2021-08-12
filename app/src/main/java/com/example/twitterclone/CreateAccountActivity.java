package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateAccountActivity extends AppCompatActivity {
    ImageButton back;

    EditText nameEdt;
    EditText emailEdt;
    EditText dobEdt;

    Button nextBtn;

    Calendar calendar;
    String dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nameEdt = findViewById(R.id.nameEdt);
        emailEdt = findViewById(R.id.emailEdt);
        dobEdt = findViewById(R.id.dobEdt);

        nextBtn = findViewById(R.id.next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(nameEdt.getText().toString(), emailEdt.getText().toString(), dobEdt.getText().toString())) {
                    Intent intent = new Intent(CreateAccountActivity.this, SignUpActivity.class);
                    intent.putExtra("name", nameEdt.getText().toString());
                    intent.putExtra("email", emailEdt.getText().toString());
                    intent.putExtra("dob", dob);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateDobEdt();
            }
        };

        dobEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateAccountActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    Boolean validate(String name, String email, String DOB) {
        if (name.equals("") || email.equals("") || DOB.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    void updateDobEdt() {
        String date = "mm/dd/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(date, Locale.US);

        dobEdt.setText(simpleDateFormat.format(calendar.getTime()));
        dob = simpleDateFormat.format(calendar.getTime());
    }
}