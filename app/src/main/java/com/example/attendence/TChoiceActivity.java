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
    Intent in,i;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tchoice);

        takeattendence = findViewById(R.id.take_attendence);
        viewattendence = findViewById(R.id.view_attendence);
        teach_logout = findViewById(R.id.teacher_logout);
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        s = intent.getStringExtra("Teacher_ID");
        if(s == null)
        {
            s = intent.getStringExtra("o_id");
            s = intent.getStringExtra("my_id");
            s = intent.getStringExtra("t_id");
        }


        Toast.makeText(getApplicationContext(),"Welcome "+ s, Toast.LENGTH_SHORT).show();

        in = new Intent(this,TakeAttendenceActivity.class);

        i = new Intent(this,ViewAttendenceActivity.class);

        takeattendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in.putExtra("take_id",s);
                startActivity(in);
            }
        });

        viewattendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("id",s);
                startActivity(i);
            }
        });


        teach_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TChoiceActivity.this,MainActivity.class));
                Toast.makeText(getApplicationContext(),"Logged out",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //
    }
}
