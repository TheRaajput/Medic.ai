package com.yasharth.medicai;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class heartActivity extends AppCompatActivity {

    private BottomSheetBehavior bottomSheetBehavior;
    private EditText Age, bloodPressure, cholestrol, maxHeartRate, stDepression, majorVessel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart);
        Button predictButton = findViewById(R.id.predictBtn);
        LinearLayout linearLayout = findViewById(R.id.bottom_navigation_container);

        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                getValues();
            }
        });
    }

    private void getValues() {
        
    }
}