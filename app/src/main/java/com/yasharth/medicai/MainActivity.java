package com.yasharth.medicai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView heartCard = findViewById(R.id.heartDiag);
        CardView lungCard = findViewById(R.id.lungDiag);
        CardView eyeCard = findViewById(R.id.eyeDiag);
        final CardView brainActivity = findViewById(R.id.brainDiagnosis);

        heartCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHeart = new Intent(MainActivity.this,heartActivity.class);
                startActivity(toHeart);
            }
        });
        lungCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLung = new Intent(MainActivity.this,lung_activity.class);
                startActivity(toLung);
            }
        });
        brainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toBrain = new Intent(MainActivity.this,brainActivity.class);
                startActivity(toBrain);
            }
        });
        eyeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toEye = new Intent(MainActivity.this,eyeActivity.class);
                startActivity(toEye);
            }
        });
    }

}