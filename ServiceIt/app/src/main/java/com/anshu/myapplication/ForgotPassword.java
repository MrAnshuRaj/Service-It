package com.anshu.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.regex.Pattern;


public class ForgotPassword extends AppCompatActivity {
    EditText etOtp, etNewPassword, etConfirmNewPassword;
    Button btnValidateOtp, btnResetPassword;
    LinearLayout passwordFieldsLayout;
    ImageView ivShowHideNewPassword, ivShowHideConfirmNewPassword;
    boolean isNewPasswordVisible = false;
    boolean isConfirmNewPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etOtp = findViewById(R.id.etOtp);
        btnValidateOtp = findViewById(R.id.btnValidateOtp);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        passwordFieldsLayout = findViewById(R.id.passwordFieldsLayout);
        ivShowHideNewPassword = findViewById(R.id.ivShowHideNewPassword);
        ivShowHideConfirmNewPassword = findViewById(R.id.ivShowHideConfirmNewPassword);

        btnValidateOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleValidateOtp();
            }
        });

        ivShowHideNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(etNewPassword, ivShowHideNewPassword, isNewPasswordVisible);
                isNewPasswordVisible = !isNewPasswordVisible;
            }
        });

        ivShowHideConfirmNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(etConfirmNewPassword, ivShowHideConfirmNewPassword, isConfirmNewPasswordVisible);
                isConfirmNewPasswordVisible = !isConfirmNewPasswordVisible;
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleResetPassword();
            }
        });
    }
    private void handleValidateOtp() {
        String otp = etOtp.getText().toString().trim();

        if (otp.isEmpty()) {
            Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isOtpValid(otp)) {
            Toast.makeText(this, "OTP Validated", Toast.LENGTH_SHORT).show();
            passwordFieldsLayout.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isOtpValid(String otp) {
        // Implement OTP validation logic here
        // For now, returning false to simulate invalid OTP
        return true;
    }

    private void handleResetPassword() {
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmNewPassword = etConfirmNewPassword.getText().toString().trim();

        if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all password fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isPasswordStrong(newPassword)) {
            Toast.makeText(this, "Password must be at least 8 characters long and include an upper case letter, a lower case letter, a digit, and a special character.", Toast.LENGTH_LONG).show();
            return;
        }

        // Implement password reset logic here
        Toast.makeText(this, "Password Reset Successful", Toast.LENGTH_SHORT).show();
    }

    private boolean isPasswordStrong(String password) {
        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Pattern lowerCasePattern = Pattern.compile("[a-z]");
        Pattern digitPattern = Pattern.compile("[0-9]");
        Pattern specialCharPattern = Pattern.compile("[^a-zA-Z0-9]");

        return password.length() >= 8 &&
                upperCasePattern.matcher(password).find() &&
                lowerCasePattern.matcher(password).find() &&
                digitPattern.matcher(password).find() &&
                specialCharPattern.matcher(password).find();
    }

    private void togglePasswordVisibility(EditText editText, ImageView imageView, boolean isVisible) {
        if (isVisible) {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imageView.setImageResource(R.drawable.ic_eye);
        } else {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imageView.setImageResource(R.drawable.ic_eye_off);
        }
        editText.setSelection(editText.getText().length());
    }
}