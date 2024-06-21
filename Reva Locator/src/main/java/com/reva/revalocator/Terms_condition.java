package com.reva.revalocator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Terms_condition extends AppCompatActivity {

    private CheckBox acceptCheckBox;
    private Button acceptButton;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView termsTextView = findViewById(R.id.termsTextView);
        acceptCheckBox = findViewById(R.id.acceptCheckBox);
        acceptButton = findViewById(R.id.acceptButton);

        // Set the terms and conditions text
        String termsAndConditions =
                "Last updated: [21 June 2024]\n" +
                        "\n" +
                        "Welcome to Reva Locator! By downloading or using our mobile application (\"Service\"), " +
                        "you agree to be bound by these Terms and Conditions (\"Terms\"). If you do not agree to these Terms, please do not use the Service. These Terms apply to all visitors, users, and others who access or use the Service. "+
                        "\n" +"\n"+
                        "The location of your device in which you are using our Service will be tracked to provide location-based services. The device should always be connected to an internet source to ensure proper functionality of the Service. The management of the university and your parents will have access to your location. All other data will remain confidential unless circumstances arise that necessitate disclosure, in which case it will be disclosed with your consent."+"\n"+
                        "\n"+
                        "I reserve the right to terminate or suspend access to our Service immediately, without prior notice or liability, for any reason whatsoever, including without limitation if you breach the Terms. All provisions of the Terms which by their nature should survive termination shall survive termination, including, without limitation, ownership provisions, warranty disclaimers, indemnity, and limitations of liability."+
                        "\n" +"\n"+
                        "I may update our Terms from time to time. I will notify you of any changes by posting the new Terms on this page. You are advised to review these Terms periodically for any changes. Changes to these Terms are effective when they are posted on this page.\n" +
                        "\n" +"\n"+
                        "If you have any questions about these Terms, please feel free to leave a comment.\n" +
                        "\n" +
                        "Thank you for using Reva Locator!";
        termsTextView.setText(termsAndConditions);

        // Set the checkbox listener to enable/disable the button
        acceptCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> acceptButton.setEnabled(isChecked));

        // Set the click listener for the accept button
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the accept action (e.g., save acceptance to shared preferences)
                acceptTermsAndConditions();
            }
        });
    }
    private void acceptTermsAndConditions() {
        // Save the acceptance to shared preferences or proceed to the next activity
        // Example: Save to shared preferences
        getSharedPreferences("app_prefs", MODE_PRIVATE)
                .edit()
                .putBoolean("terms_accepted", true)
                .apply();

        // Proceed to the main activity or another activity
        Intent intent = new Intent(Terms_condition.this, Registration.class);
        startActivity(intent);
        finish();
    }
}