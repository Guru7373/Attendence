package com.example.attendence;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminActivity extends AppCompatActivity {

    Button adminlogin;
    TextView mainpage;
    EditText admin_id,admin_pass;
    String id,pass;
    ProgressBar adminprogress;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth = FirebaseAuth.getInstance();

        adminlogin = findViewById(R.id.admin_login);
        mainpage = findViewById(R.id.main_page);

        admin_id = findViewById(R.id.et_admin_email);
        admin_pass = findViewById(R.id.et_admin_password);

        adminprogress = findViewById(R.id.admin_progress);
        //authentication

        adminprogress.setVisibility(View.GONE);
        adminlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminprogress.setVisibility(View.VISIBLE);
                id = admin_id.getText().toString();
                pass = admin_pass.getText().toString();
                if(id.equals("") && pass.equals("")){
                    admin_id.setError("Required");
                    admin_pass.setError("Required");
                }
                else
                {
                    adminprogress.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(id,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),AChoiceActivity.class);
                                    startActivity(intent);
                                    adminprogress.setVisibility(View.GONE);
                                } else {
                                    adminprogress.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            }
        });

        mainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
