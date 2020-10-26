package com.yasharth.medicai;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
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
import org.tensorflow.lite.Interpreter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class heartActivity extends AppCompatActivity {

    private BottomSheetBehavior bottomSheetBehavior;
    private Interpreter interpreter;
    String modelFile = "heartModel.tflite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart);
        final Button predictButton = findViewById(R.id.predictBtn);
        final TextView predictedText = findViewById(R.id.predictedText);
        final TextView predicted_result = findViewById(R.id.predictedScore);
        final TextView predictHead = findViewById(R.id.predHead);


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


        try{
            interpreter = new Interpreter(LoadModelFile(heartActivity.this,modelFile));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        predictButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                try {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

                    float PatientAge = (float) ((Float.parseFloat(age.getText().toString())-54.4971751)/9.21400164); //Patient Age Details

                    float PatientBP = (float) ((Float.parseFloat(bloodPressure.getText().toString())-131.630885)/17.07277287); //Patient Blood Pressure

                    float PatientChol = (float) ((Float.parseFloat(cholestrol.getText().toString())-245.656309)/51.15139039); //Patient's Cholestrol Details

                    float PatientMaxHeart = (float) ((Float.parseFloat(maxHeartRate.getText().toString())-149.436911)/22.84577938); // Patient's Max Heart Rate

                    float PatientStDep = (float) ((Float.parseFloat(stDepression.getText().toString())-1.03672316)/1.15724517); // St depression value

                    float PatientMVessel = (float) ((Float.parseFloat(majorVessel.getText().toString())-0.750470810)/1.02183081); //Flouroscopy major Vessel

                    int selectedID = sex.getCheckedRadioButtonId();
                    RadioButton radioSexButton = findViewById(selectedID);
                    float PatientGender = (float) ((Float.parseFloat(radioSexButton.getTag().toString())-0.685499058)/0.46431681); // Patient's gender

                    int selectedID2 = chestPainType.getCheckedRadioButtonId();
                    RadioButton radioChestPain = findViewById(selectedID2);
                    float chestPainType = (float) ((Float.parseFloat(radioChestPain.getTag().toString())-0.942561205)/1.0285456); // Chest Pain Type

                    int selectedID3 = bloodSugar.getCheckedRadioButtonId();
                    RadioButton radioBloodSugar = findViewById(selectedID3);
                    float bloodSugarLevel = (float) ((Float.parseFloat(radioBloodSugar.getTag().toString())-0.155367232)/0.36225441); // Blood sugar> 120mg/dl?

                    int selectedID4 = ecgReport.getCheckedRadioButtonId();
                    RadioButton radioECG = findViewById(selectedID4);
                    float PatientECGReport = (float) ((Float.parseFloat(radioECG.getTag().toString())-0.535781544)/0.53161827); // ECG report

                    int selectedID5 = exercisePain.getCheckedRadioButtonId();
                    RadioButton radioPain = findViewById(selectedID5);
                    float PatientChestPain = (float) ((Float.parseFloat(radioPain.getTag().toString())-0.341807910)/0.47431557);// exercise Induced pain

                    int selectedID6 = slopePeak.getCheckedRadioButtonId();
                    RadioButton radioPeak = findViewById(selectedID6);
                    float PatientSlopePeak = (float) ((Float.parseFloat(radioPeak.getTag().toString())-1.39265537)/0.61926291); // slope of ST depression

                    int selectedID7 = thalSlope.getCheckedRadioButtonId();
                    RadioButton radioThal = findViewById(selectedID7);
                    float PatientThalSlope = (float) ((Float.parseFloat(radioThal.getTag().toString())-2.30414313)/0.61583145); // Thalassmia Slope



                    float[][] input = new float[][]{{PatientAge,PatientGender,chestPainType,PatientBP,PatientChol,bloodSugarLevel,PatientECGReport,
                            PatientMaxHeart,PatientChestPain,PatientStDep,PatientSlopePeak,PatientMVessel,PatientThalSlope}};
                    float[][] out = new float[][]{{0}};
                    interpreter.run(input,out);

                    if (out[0][0]>0.4)
                    {
                        predictedText.setText("Consult Nearest Doctor soon.");
                        predictedText.setTextColor(Color.parseColor("#E71C23"));
                    }
                    else
                    {
                        predictedText.setText("You are safe.But do workout");
                        predictedText.setTextColor(Color.parseColor("#43BE31"));
                    }
                    predictHead.setText("Chances of heart failure:");
                    pulseAnimation(predictedText);
                    predicted_result.setText((out[0][0])*100 + "%");
                }
                catch (Exception e)
                {
                    Toast.makeText(heartActivity.this, "Please give all the inputs", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    ObjectAnimator objAnim;
    private void pulseAnimation(TextView obj){
        objAnim= ObjectAnimator.ofPropertyValuesHolder(obj, PropertyValuesHolder.ofFloat("scaleX", 1.2f), PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        objAnim.setDuration(300);
        objAnim.setRepeatCount(ObjectAnimator.INFINITE);
        objAnim.setRepeatMode(ObjectAnimator.REVERSE);
        objAnim.start();
    }

    private MappedByteBuffer LoadModelFile(Activity activity, String Model_File) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(Model_File);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declareLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declareLength);
    }

}