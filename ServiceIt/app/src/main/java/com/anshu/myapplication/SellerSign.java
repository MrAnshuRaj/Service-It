package com.anshu.myapplication;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SellerSign extends AppCompatActivity {
    private MultiAutoCompleteTextView multiAutoCompleteServices;
    private TextView tvStartTime, tvEndTime;
    private EditText etAvgCharge, etExperienceYears, etExperienceMonths;
    private Button btnStartTime, btnEndTime, btnSubmit;
    private List<String> servicesList = Arrays.asList(
            "Plumbing", "Electrical Work", "House Cleaning", "Gardening", "Painting", "Carpentry"
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seller_sign);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        multiAutoCompleteServices = findViewById(R.id.multiAutoCompleteServices);
        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndTime = findViewById(R.id.tvEndTime);
        etAvgCharge = findViewById(R.id.etAvgCharge);
        etExperienceYears = findViewById(R.id.etExperienceYears);
        etExperienceMonths = findViewById(R.id.etExperienceMonths);
        btnStartTime = findViewById(R.id.btnStartTime);
        btnEndTime = findViewById(R.id.btnEndTime);
        btnSubmit = findViewById(R.id.btnSubmit);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, servicesList);
        multiAutoCompleteServices.setAdapter(adapter);
        multiAutoCompleteServices.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(tvStartTime);
            }
        });

        btnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(tvEndTime);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSubmit();
            }
        });
    }
    private void showTimePickerDialog(final TextView timeTextView) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = String.format("%02d:%02d", hourOfDay, minute);
                        timeTextView.setText(time);
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void handleSubmit() {
        String selectedServices = multiAutoCompleteServices.getText().toString().trim();
        String startTime = tvStartTime.getText().toString().trim();
        String endTime = tvEndTime.getText().toString().trim();
        String avgCharge = etAvgCharge.getText().toString().trim();
        String experienceYears = etExperienceYears.getText().toString().trim();
        String experienceMonths = etExperienceMonths.getText().toString().trim();

        if (selectedServices.isEmpty() || startTime.equals("Not Set") || endTime.equals("Not Set") ||
                avgCharge.isEmpty() || experienceYears.isEmpty() || experienceMonths.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Implement logic to handle the seller's setup details and proceed to the main activity
        Toast.makeText(this, selectedServices+"Seller Account Setup Successful", Toast.LENGTH_SHORT).show();

        // Example: Navigate to Main Activity
        // Intent intent = new Intent(SellerSetupActivity.this, MainActivity.class);
        // startActivity(intent);
        // finish();
    }
}