package com.yasharth.medicai;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.tensorflow.lite.Interpreter;

import java.io.FileNotFoundException;
import java.io.IOException;


public class eyeActivity extends AppCompatActivity {
    protected Interpreter tflite;
    String modelFile = "CataractModel.tflite";

    private BottomSheetBehavior bottomSheetBehavior;
    private Button predictButton;
    private ImageView leftRetina, rightRetina;
    private final int GALLERY_REQ_CODE = 123;
    private int LoadedImg;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye);

        predictButton = findViewById(R.id.predButton);


        LinearLayout linearLayout = findViewById(R.id.bottom_navigation_container);
        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_DRAGGING);

        leftRetina = findViewById(R.id.leftRetina);
        rightRetina = findViewById(R.id.rightRetina);

        leftRetina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLeftImage();
            }
        });

        rightRetina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRightImage();
            }
        });

        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }
        });
    }

    private void getRightImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Pick an Image:"),GALLERY_REQ_CODE);
        LoadedImg = 2;
    }

    private void getLeftImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Pick an Image:"),GALLERY_REQ_CODE);
        LoadedImg = 1;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLERY_REQ_CODE && resultCode == RESULT_OK && data!= null)
        {
            Uri imageData = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageData);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (LoadedImg == 1)
            {
                leftRetina.setImageURI(imageData);
            }
            if (LoadedImg == 2)
            {
                rightRetina.setImageURI(imageData);
            }

        }
    }
}