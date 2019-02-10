package com.example.attendence;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AChoiceActivity extends AppCompatActivity {

    TextView addteacher,updateteacher,deleteteacher;
    Button adminlogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achoice);

        adminlogout = findViewById(R.id.admin_logout);
        addteacher = findViewById(R.id.add_teacher);
        updateteacher = findViewById(R.id.update_teacher);
        deleteteacher = findViewById(R.id.delete_teacher);

        adminlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AChoiceActivity.this,AdminActivity.class));
                finish();
            }
        });

        addteacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AChoiceActivity.this,AddTeacherActivity.class));
                finish();
            }
        });

        updateteacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AChoiceActivity.this,UpdateTeacherActivity.class));
                finish();
            }
        });

        deleteteacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AChoiceActivity.this,DeleteTeacherActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}