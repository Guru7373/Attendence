package com.example.attendence;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    EditText id,pass;
    String user_id,user_pass;
    String db_id,db_pass;
    Button teach_login;
    TextView adminpage;
    FirebaseFirestore db;
    DocumentReference user;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teach_login = findViewById(R.id.teacher_login);
        adminpage = findViewById(R.id.admin_page);

        db = FirebaseFirestore.getInstance();

        id = findViewById(R.id.et_teacher_id);
        pass = findViewById(R.id.et_teacher_pass);
        progressBar = findViewById(R.id.teach_progress);

        progressBar.setVisibility(View.GONE);
        teach_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                user_id = id.getText().toString();
                user_pass = pass.getText().toString();
                call();
            }
        });

        adminpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AdminActivity.class));
                finish();
            }
        });
    }

    private void call() {
        progressBar.setVisibility(View.VISIBLE);
        if(user_pass.equals("") && user_id.equals(""))
        {
            id.setError("Required");
            pass.setError("Required");
            progressBar.setVisibility(View.GONE);
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            user = db.collection("Teachers_list").document(user_id);
            user.get().addOnCompleteListener(new OnCompleteListener < DocumentSnapshot > () {
                @Override
                public void onComplete(@NonNull Task < DocumentSnapshot > task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if(doc != null) {
                            db_id = doc.getString("ID");
                            db_pass = doc.getString("password");
                            if(user_id.equals(db_id) && user_pass.equals(db_pass))
                            {
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(getApplicationContext(),TChoiceActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Unsuccessful",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

    }

}