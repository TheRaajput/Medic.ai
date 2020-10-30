package com.yasharth.medicai;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.Operator;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;


public class brainActivity extends AppCompatActivity {
    protected Interpreter tflite;
    String modelFile = "brainModel.tflite";
    private int imageSizeX;
    private int imageSizeY;
    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;
    private static final float PROBABILITY_MEAN = 0.0f;
    private static final float PROBABILITY_STD = 255.0f;
    private TensorImage inputImageBuffer;
    private Bitmap bitmap;
    private TensorBuffer outputBuffer;
    private BottomSheetBehavior bottomSheetBehavior;
    private final int GALLERY_REQ_CODE = 123;
    ImageView brainMri;
    Button predictButton;
    TextView predictHead;
    TextView predictedScore;
    TextView predictedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brain);

        LinearLayout linearLayout = findViewById(R.id.bottom_navigation_container);
        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_DRAGGING);

        brainMri = findViewById(R.id.mriImage);
        predictButton = findViewById(R.id.predictButton);
        predictHead = findViewById(R.id.predHead);
        predictedScore = findViewById(R.id.predictedScore);
        predictedText = findViewById(R.id.predictedText);

        try {
            tflite = new Interpreter(LoadModelFile(this,modelFile));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        brainMri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery();
            }
        });

        predictButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                try {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                    int imageTensorIndex = 0;
                    int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape();
                    imageSizeY = imageShape[1];
                    imageSizeX = imageShape[2];
                    DataType imageDatatype = tflite.getInputTensor(imageTensorIndex).dataType();
                    inputImageBuffer = new TensorImage(imageDatatype);
                    float [][] outputBuffer = new float[][]{{0}};

                    inputImageBuffer = loadImage(bitmap);
                    tflite.run(inputImageBuffer.getBuffer(),outputBuffer);
                    showResult(outputBuffer);
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(brainActivity.this, "Give Image Input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showResult(float[][] outputBuffer) {
        predictHead.setText("Chances of having Brain Tumor: ");
        predictedScore.setText(outputBuffer[0][0]*100+"%");
        if (outputBuffer[0][0]>0.5)
        {
            predictedText.setText("Consult Nearest Doctor soon.");
            predictedText.setTextColor(Color.parseColor("#E71C23"));
        }
        else
        {
            predictedText.setText("You are safe,But go for other tests");
            predictedText.setTextColor(Color.parseColor("#43BE31"));
        }
        pulseAnimation(predictedText);
    }

    private TensorImage loadImage(final Bitmap bitmap) {
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

    private TensorOperator getPreProcessNormalizeOp() {
        return new NormalizeOp(IMAGE_MEAN,IMAGE_STD);
    }

    private TensorOperator getPostProcessNormalizeOp() {
        return new NormalizeOp(PROBABILITY_MEAN,PROBABILITY_STD);
    }


    private void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pick an Image"),GALLERY_REQ_CODE);
    }

    private MappedByteBuffer LoadModelFile(Activity activity, String Model_File) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(Model_File);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declareLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declareLength);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQ_CODE && resultCode == RESULT_OK && data != null)
        {
            Uri imageData = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageData);
                brainMri.setImageURI(imageData);
            }
            catch (IOException e)
            {
                e.printStackTrace();
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