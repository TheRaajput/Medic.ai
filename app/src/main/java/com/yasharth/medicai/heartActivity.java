package com.yasharth.medicai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class heartActivity extends AppCompatActivity {

    private BottomSheetBehavior bottomSheetBehavior;
    private final String modelPath = "heartModel.tflite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart);
        final Button predictButton = findViewById(R.id.predictBtn);

        final TextView predicted_result = findViewById(R.id.predictedScore);

        final EditText age = findViewById(R.id.Age);
        final EditText bloodPressure = findViewById(R.id.bloodPressure);
        final EditText cholestrol = findViewById(R.id.EnterCholestrol);
        final EditText maxHeartRate = findViewById(R.id.MaxHeartRate);
        final EditText stDepression = findViewById(R.id.StDepression);
        final EditText majorVessel = findViewById(R.id.majorVessel);

        final RadioGroup sex = findViewById(R.id.sex);
        final RadioGroup chestPainType = findViewById(R.id.chestPainType);
        final RadioGroup bloodSugar = findViewById(R.id.bloodSugar);
        final RadioGroup ecgReport = findViewById(R.id.ecgReport);
        final RadioGroup exercisePain = findViewById(R.id.exercisePain);
        final RadioGroup slopePeak = findViewById(R.id.slopePeak);
        final RadioGroup thalSlope = findViewById(R.id.thalSlope);



        LinearLayout linearLayout = findViewById(R.id.bottom_navigation_container);

        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

                float PatientAge = Float.parseFloat(age.getText().toString()); //Patient Age Details

                float PatientBP = Float.parseFloat(bloodPressure.getText().toString()); //Patient Blood Pressure

                float PatientChol = Float.parseFloat(cholestrol.getText().toString()); //Patient's Cholestrol Details

                float PatientMaxHeart = Float.parseFloat(maxHeartRate.getText().toString()); // Patient's Max Heart Rate

                float PatientStDep = Float.parseFloat(stDepression.getText().toString()); // St depression value

                float PatientMVessel = Float.parseFloat(majorVessel.getText().toString()); //Flouroscopy major Vessel

                int selectedID = sex.getCheckedRadioButtonId();
                RadioButton radioSexButton = findViewById(selectedID);
                float PatientGender = Float.parseFloat(radioSexButton.getTag().toString()); // Patient's gender

                int selectedID2 = chestPainType.getCheckedRadioButtonId();
                RadioButton radioChestPain = findViewById(selectedID2);
                float chestPainType = Float.parseFloat(radioChestPain.getTag().toString()); // Chest Pain Type

                int selectedID3 = bloodSugar.getCheckedRadioButtonId();
                RadioButton radioBloodSugar = findViewById(selectedID3);
                float bloodSugarLevel = Float.parseFloat(radioBloodSugar.getTag().toString()); // Blood sugar> 120mg/dl?

                int selectedID4 = ecgReport.getCheckedRadioButtonId();
                RadioButton radioECG = findViewById(selectedID4);
                float PatientECGReport = Float.parseFloat(radioECG.getTag().toString()); // ECG report

                int selectedID5 = exercisePain.getCheckedRadioButtonId();
                RadioButton radioPain = findViewById(selectedID5);
                float PatientChestPain = Float.parseFloat(radioPain.getTag().toString());// exercise Induced pain

                int selectedID6 = slopePeak.getCheckedRadioButtonId();
                RadioButton radioPeak = findViewById(selectedID6);
                float PatientSlopePeak = Float.parseFloat(radioPeak.getTag().toString()); // slope of ST depression

                int selectedID7 = thalSlope.getCheckedRadioButtonId();
                RadioButton radioThal = findViewById(selectedID7);
                float PatientThalSlope = Float.parseFloat(radioThal.getTag().toString()); // Thalassmia Slope
            }
        });

    }
}