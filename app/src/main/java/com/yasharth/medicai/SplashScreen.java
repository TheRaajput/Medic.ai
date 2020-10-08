package com.yasharth.medicai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        LottieAnimationView splashAnim = findViewById(R.id.AnimSplash);
        TextView companyName = findViewById(R.id.CompanyName);
        TextView tagLine = findViewById(R.id.TagLine);

        Animation frontAnim = AnimationUtils.loadAnimation(this,R.anim.myanim);

        splashAnim.startAnimation(frontAnim);
        companyName.startAnimation(frontAnim);
        tagLine.startAnimation(frontAnim);

        final Intent i = new Intent(this,MainActivity.class);
        Thread time = new Thread()
        {
            public void run (){
                try {
                    sleep(6500);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        time.start();

    }
}