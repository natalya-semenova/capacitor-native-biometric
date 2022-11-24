package com.epicshaggy.biometric;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricPrompt;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.epicshaggy.biometric.capacitornativebiometric.R;

import java.util.concurrent.Executor;

public class AuthActivity extends AppCompatActivity {

    private Executor executor;
    private int maxAttempts;
    private int counter = 0;

    private BiometricPrompt.PromptInfo createPromptInfo() {
        BiometricPrompt.PromptInfo.Builder promptInfo = new BiometricPrompt.PromptInfo.Builder()
            .setTitle(getIntent().hasExtra("title") ? getIntent().getStringExtra("title") : "Authenticate")
            .setSubtitle(getIntent().hasExtra("subtitle") ? getIntent().getStringExtra("subtitle") : null)
            .setDescription(getIntent().hasExtra("description") ? getIntent().getStringExtra("description") : null);

        if(getIntent().getBooleanExtra("useFallback", false)) {
            promptInfo.setDeviceCredentialAllowed(true);
        } else {
            promptInfo.setNegativeButtonText(getIntent().hasExtra("negativeButtonText") ? getIntent().getStringExtra("negativeButtonText") : "Cancel");
        }

        if(getIntent().hasExtra("disableConfirmationRequired")) {
            promptInfo.setConfirmationRequired(false);
        }

        return promptInfo.build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_acitivy);

        maxAttempts = getIntent().getIntExtra("maxAttempts", 1);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            executor = this.getMainExecutor();
        }else{
            executor = new Executor() {
                @Override
                public void execute(Runnable command) {
                    new Handler().post(command);
                }
            };
        }

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);

                finishActivity(errString.toString(), errorCode);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                finishActivity("success", 0);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                counter++;
                if(counter == maxAttempts)
                    finishActivity("failed");
            }
        });

        biometricPrompt.authenticate(createPromptInfo());

    }

    void finishActivity(String result) {
        Intent intent = new Intent();
        intent.putExtra("result", "failed");
        intent.putExtra("errorDetails", "Authentication failed.");
        setResult(RESULT_OK, intent);
        finish();
    }

    void finishActivity(String result, int errorCode) {
        Intent intent = new Intent();
        if(errorCode != 0){
            intent.putExtra("result", "error");
            intent.putExtra("errorDetails",result);
            intent.putExtra("errorCode",String.valueOf(errorCode));
        }else{
            intent.putExtra("result", result);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

}
