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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateTeacherActivity extends AppCompatActivity {

    EditText et_name,et_ids,et_confpass,et_pass,et_subname;
    Button fet_btn,updatebtn;
    String given_id;
    String fetched_name,fetched_id,fetched_pass,fetched_sub_name;
    String update_id,update_name,update_pass,update_conf_pass,update_sub;
    FirebaseFirestore db;
    ProgressBar progressBar;
    Map<String,Object> list = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);

        et_name = findViewById(R.id.et_update_name);
        et_ids = findViewById(R.id.et_update_id);
        et_pass = findViewById(R.id.et_update_password);
        et_confpass = findViewById(R.id.et_update_conf_password);
        et_subname = findViewById(R.id.et_update_subject);

        fet_btn = findViewById(R.id.btn_fetch_update);
        updatebtn = findViewById(R.id.btn_update);
        progressBar = findViewById(R.id.update_progress);

        db = FirebaseFirestore.getInstance();

        progressBar.setVisibility(View.GONE);

        updatebtn.setEnabled(false);

        fet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                given_id = et_ids.getText().toString();
                if(given_id.equals(""))
                {
                    et_ids.setError("Required");
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    db.collection("Teachers_list").document(given_id)
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful())
                                    {
                                        DocumentSnapshot doc = task.getResult();
                                        if(doc!=null)
                                        {
                                            fetched_name = doc.getString("Name");
                                            fetched_sub_name = doc.getString("Subject Name");
                                            fetched_pass = doc.getString("password");
                                            fetched_id = doc.getString("ID");

                                            et_ids.setText(fetched_id);
                                            et_name.setText(fetched_name);
                                            et_subname.setText(fetched_sub_name);
                                            et_pass.setText(fetched_pass);
                                            et_confpass.setText(fetched_pass);

                                            fet_btn.setEnabled(false);
                                            updatebtn.setEnabled(true);

                                            et_ids.setEnabled(false);

                                            et_name.setEnabled(true);
                                            et_pass.setEnabled(true);
                                            et_confpass.setEnabled(true);
                                            et_subname.setEnabled(true);

                                            progressBar.setVisibility(View.GONE);
                                        }
                                        if(!(given_id.equals(fetched_id)))
                                        {
                                            updatebtn.setEnabled(false);
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(),"No Record found",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                update_id = fetched_id;
                update_name = et_name.getText().toString();
                update_pass = et_pass.getText().toString();
                update_conf_pass = et_confpass.getText().toString();
                update_sub = et_subname.getText().toString();

                if(update_name.equals(""))
                {
                    et_name.setError("Required");
                    progressBar.setVisibility(View.GONE);
                }
                else if(update_pass.equals(""))
                {
                    et_pass.setError("Required");
                    progressBar.setVisibility(View.GONE);
                }
                else if(update_conf_pass.equals(""))
                {
                    et_confpass.setError("Required");
                    progressBar.setVisibility(View.GONE);
                }
                else if(update_sub.equals(""))
                {
                    et_subname.setError("Required");
                    progressBar.setVisibility(View.GONE);
                }
                else if(!(update_pass.equals(update_conf_pass)))
                {
                    Toast.makeText(getApplicationContext(),"Password mismatch",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                else if(!(update_name.equals("")))
                {
                    DocumentReference update_ref = db.collection("Teachers_list").document(fetched_id);
                    update_ref.update("ID",fetched_id);
                    update_ref.update("Name",update_name);
                    update_ref.update("Subject Name",update_sub);
                    update_ref.update("password",update_pass)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(@NonNull Void task) {
                                    progressBar.setVisibility(View.GONE);

                                    et_ids.setEnabled(true);

                                    et_name.setEnabled(false);
                                    et_pass.setEnabled(false);
                                    et_confpass.setEnabled(false);
                                    et_subname.setEnabled(false);

                                    et_ids.setText("");
                                    et_name.setText("");
                                    et_subname.setText("");
                                    et_pass.setText("");
                                    et_confpass.setText("");

                                    updatebtn.setEnabled(false);
                                    fet_btn.setEnabled(true);
                                    Toast.makeText(getApplicationContext(),"Teacher ID " + given_id + " Updated ",Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Could not be updated",Toast.LENGTH_LONG).show();
                    }
                }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),AChoiceActivity.class);
        startActivity(intent);
    }
}
