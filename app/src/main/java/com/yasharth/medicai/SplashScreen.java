package com.yasharth.medicai;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


//        Variables
        TextView companyName = findViewById(R.id.CompanyName);
        TextView tagLine = findViewById(R.id.TagLine);

//        Animation
        Animation frontAnim = AnimationUtils.loadAnimation(this,R.anim.myanim);
        companyName.startAnimation(frontAnim);
        tagLine.startAnimation(frontAnim);

        final Intent i = new Intent(this,MainActivity.class);
        Thread time = new Thread()
        {
            public void run (){
                try {
                    sleep(4000);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}