package com.yasharth.medicai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.lang.Object;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.nio.MappedByteBuffer;

public class heartActivity extends AppCompatActivity {

    private BottomSheetBehavior bottomSheetBehavior;
    private final String modelPath = "heartModel.tflite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart);
        Button predictButton = findViewById(R.id.predictBtn);

        TextView predicted_result = findViewById(R.id.predictedScore);

        EditText age = findViewById(R.id.Age);
        EditText bloodPressure = findViewById(R.id.bloodPressure);
        EditText cholestrol = findViewById(R.id.EnterCholestrol);
        EditText maxHeartRate = findViewById(R.id.MaxHeartRate);
        EditText stDepression = findViewById(R.id.StDepression);
        EditText majorVessel = findViewById(R.id.majorVessel);

        RadioGroup sex = findViewById(R.id.sex);
        RadioGroup chestPainType = findViewById(R.id.chestPainType);
        RadioGroup bloodSugar = findViewById(R.id.bloodSugar);
        RadioGroup ecgReport = findViewById(R.id.ecgReport);
        RadioGroup exercisePain = findViewById(R.id.exercisePain);
        RadioGroup slopePeak = findViewById(R.id.slopePeak);
        RadioGroup thalSlope = findViewById(R.id.thalSlope);



        LinearLayout linearLayout = findViewById(R.id.bottom_navigation_container);

        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

            }
        });

    }
    
}