package com.example.attendence;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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

import java.util.ArrayList;

import static android.widget.Toast.makeText;

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
            }
        });
    }

    private void call() {
        progressBar.setVisibility(View.VISIBLE);
        if(user_id.equals(""))
        {
            id.setError("Required");
            progressBar.setVisibility(View.GONE);
        }
        else if(user_pass.equals(""))
        {
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
                                intent.putExtra("Teacher_ID",user_id);
                                startActivity(intent);
                                Toast toast = Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT);
                                View view = toast.getView();
                                view.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
                                TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
                                toastMessage.setTextColor(R.drawable.toast_colour);
                                toastMessage.setTextColor(Color.BLACK);
                                toast.show();
                            }
                            else
                            {
                                progressBar.setVisibility(View.GONE);
                                Toast toast  = makeText(getApplicationContext()," PLEASE  ENTER  CORRECT  LOGIN  CREDENTIALS",Toast.LENGTH_SHORT);
                                View view = toast.getView();
                                view.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                                TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
                                toastMessage.setTextColor(R.drawable.toast_colour);
                                toastMessage.setTextColor(Color.RED);
                                toast.show();
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
