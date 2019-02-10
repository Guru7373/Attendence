package com.example.attendence;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class TChoiceActivity extends AppCompatActivity {

    TextView takeattendence,viewattendence;
    Button teach_logout;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tchoice);

        takeattendence = findViewById(R.id.take_attendence);
        viewattendence = findViewById(R.id.view_attendence);
        teach_logout = findViewById(R.id.teacher_logout);
        mAuth = FirebaseAuth.getInstance();


        takeattendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( TChoiceActivity.this,TakeAttendenceActivity.class));
                finish();
            }
        });

        viewattendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TChoiceActivity.this,ViewAttendenceActivity.class));
                finish();
            }
        });


        teach_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TChoiceActivity.this,MainActivity.class));
                Toast.makeText(getApplicationContext(),"Logged out",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //
    }
}
