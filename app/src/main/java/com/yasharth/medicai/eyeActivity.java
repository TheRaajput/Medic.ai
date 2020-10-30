package com.yasharth.medicai;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageOperator;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;


public class eyeActivity extends AppCompatActivity {
    protected Interpreter tflite;
    String modelFile = "CataractModel.tflite";

    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView leftRetina, rightRetina;
    private final int GALLERY_REQ_CODE = 123;
    private int LoadedImg;
    private Bitmap bitmap1, bitmap2;
    private int imageSizeX;
    private int imageSizeY;
    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;
    private static final float PROBABILITY_MEAN = 0.0f;
    private static final float PROBABILITY_STD = 255.0f;
    private TensorImage inputImageBuffer, inputImageBuffer1, inputImageBuffer2;
    TextView predictHead;
    TextView predictedScore;
    TextView predictedText;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye);

        Button predictButton = findViewById(R.id.predButton);
        predictHead = findViewById(R.id.predHead);
        predictedScore = findViewById(R.id.predictedScore);
        predictedText = findViewById(R.id.predictedText);
        progressBar = findViewById(R.id.progressBar);

        LinearLayout linearLayout = findViewById(R.id.bottom_navigation_container);
        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_DRAGGING);

        leftRetina = findViewById(R.id.leftRetina);
        rightRetina = findViewById(R.id.rightRetina);

        progressBar.setVisibility(View.INVISIBLE);

        try {
            tflite = new Interpreter(LoadModelFile(eyeActivity.this,modelFile));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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
                progressBar.setVisibility(View.VISIBLE);
                int imageTensorIndex = 0;
                int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape();
                imageSizeX = imageShape[2];
                imageSizeY = imageShape[1];
                DataType imageDatatype = tflite.getInputTensor(imageTensorIndex).dataType();
                inputImageBuffer1 = new TensorImage(imageDatatype);
                inputImageBuffer2 = new TensorImage(imageDatatype);
                float[][] output1 = new float[][]{{0}};
                float[][] output2 = new float[][]{{0}};

                inputImageBuffer1 = loadImage(bitmap1);
                tflite.run(inputImageBuffer1.getBuffer(),output1);
                inputImageBuffer2 = loadImage(bitmap2);
                tflite.run(inputImageBuffer2.getBuffer(),output2);
                showResult(output1,output2);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showResult(float[][] output1, float[][] output2) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        predictHead.setText("Chances of having Cataract: ");
        if (output1[0][0]>0.5)
        {
            predictedScore.setText(output1[0][0]*100+"%");
            predictedText.setText("Left Eye has cataract");
            predictedText.setTextColor(Color.parseColor("#E71C23"));
        }
        else if (output2[0][0]>0.5)
        {
            predictedScore.setText(output2[0][0]*100+"%");
            predictedText.setText("Right Eye has cataract");
            predictedText.setTextColor(Color.parseColor("#E71C23"));
        }
        else if (output1[0][0]>0.5 | output2[0][0]>0.5)
        {
            predictedText.setText("Both Eye has cataract");
            predictedText.setTextColor(Color.parseColor("#E71C23"));
        }
        else
        {
            predictedText.setText("You are safe,But go for other tests");
            predictedText.setTextColor(Color.parseColor("#43BE31"));
        }
        pulseAnimation(predictedText);
    }

    private TensorImage loadImage(Bitmap bitmap) {
        inputImageBuffer.load(bitmap);

        int cropSize = Math.min(bitmap.getWidth(),bitmap.getHeight());
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize,cropSize))
                        .add(new ResizeOp(imageSizeX,imageSizeY,ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(getPostProcessNormalizeOp())
                        .build();
        return imageProcessor.process(inputImageBuffer);
    }

    private TensorOperator getPostProcessNormalizeOp() {
        return new NormalizeOp(PROBABILITY_MEAN,PROBABILITY_STD);
    }

    private void getLeftImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Pick an Image: "), GALLERY_REQ_CODE);
        LoadedImg = 1;
    }


    private void getRightImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Pick an Image: "), GALLERY_REQ_CODE);
        LoadedImg = 2;
    }


    private MappedByteBuffer LoadModelFile(Activity activity, String ModelFile) throws IOException{
        AssetFileDescriptor assetFileDescriptor = activity.getAssets().openFd(ModelFile);
        FileInputStream inputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long declareLength = assetFileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declareLength);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQ_CODE && resultCode == RESULT_OK && data!= null)
        {
            Uri imageData = data.getData();
            if (LoadedImg == 1)
            {
                try {
                    bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(),imageData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                leftRetina.setImageURI(imageData);
            }
            if (LoadedImg == 2)
            {
                try {
                    bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(),imageData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                rightRetina.setImageURI(imageData);
            }

        }
    }

    ObjectAnimator objAnim;
    private void pulseAnimation(TextView obj){
        objAnim= ObjectAnimator.ofPropertyValuesHolder(obj, PropertyValuesHolder.ofFloat("scaleX", 1.1f), PropertyValuesHolder.ofFloat("scaleY", 1.1f));
        objAnim.setDuration(300);
        objAnim.setRepeatCount(ObjectAnimator.INFINITE);
        objAnim.setRepeatMode(ObjectAnimator.REVERSE);
        objAnim.start();
    }
}