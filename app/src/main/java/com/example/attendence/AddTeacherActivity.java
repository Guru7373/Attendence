package com.example.attendence;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddTeacherActivity extends AppCompatActivity {

    EditText name,ids,pass,confpass,subname;
    Button button;
    String given_name,given_id,given_pass,db_id,conf_pass,sub_name;
    FirebaseFirestore db;
    ProgressBar progressBar;
    Map<String,String> list = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        name = findViewById(R.id.et_name_add);
        ids = findViewById(R.id.et_id_add);
        pass = findViewById(R.id.et_password_add);
        confpass = findViewById(R.id.et_conf_password_add);
        subname = findViewById(R.id.et_subject);
        progressBar = findViewById(R.id.save_progress);

        button = findViewById(R.id.btn_add);

        db = FirebaseFirestore.getInstance();

        progressBar.setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                given_name = name.getText().toString();
                given_id = ids.getText().toString();
                given_pass = pass.getText().toString();
                conf_pass = confpass.getText().toString();
                sub_name = subname.getText().toString().toUpperCase();

                if(given_name.equals("") && given_id.equals("") && given_pass.equals("") && conf_pass.equals("") && sub_name.equals(""))
                {
                    name.setError("Required");
                    ids.setError("Required");
                    pass.setError("Required");
                    confpass.setError("Required");
                    subname.setError("Required");
                    progressBar.setVisibility(View.GONE);
                }
                else if(!(given_pass.equals(conf_pass)))
                {
                    Toast.makeText(getApplicationContext(),"Password mismatch",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                    list.put("ID",given_id);
                    list.put("Name",given_name);
                    list.put("password",given_pass);
                    list.put("Subject Name",sub_name);

                    call();

                }
            }
        });
    }

    private void call() {
        progressBar.setVisibility(View.VISIBLE);
        DocumentReference dref = db.collection("Teachers_list").document(given_id);
        dref.get().addOnCompleteListener(new OnCompleteListener < DocumentSnapshot > () {
            @Override
            public void onComplete(@NonNull Task < DocumentSnapshot > task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc != null) {
                        db_id = doc.getString("ID");
                    }
                    if(given_id.equals(db_id)){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"User ID " + given_id + " already exists",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        db.collection("Teachers_list").document(given_id)
                                .set(list).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),given_id + " Saved successfully",Toast.LENGTH_LONG).show();
                                name.setText("");
                                pass.setText("");
                                confpass.setText("");
                                subname.setText("");
                            }
                        });
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),AChoiceActivity.class);
        startActivity(intent);
    }
}
