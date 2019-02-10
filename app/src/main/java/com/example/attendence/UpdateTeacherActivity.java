package com.example.attendence;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateTeacherActivity extends AppCompatActivity {

    EditText name,ids,confpass,pass,subname;
    Button fet_btn,updatebtn;
    String given_name,given_id,given_pass,sub_name,fetched_id;
    String update_name,update_pass,update_conf_pass,update_sub;
    FirebaseFirestore db;
    ProgressBar progressBar;
    Map<String,Object> list = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);

        name = findViewById(R.id.et_name_update);
        ids = findViewById(R.id.et_id_update);
        pass = findViewById(R.id.et_password_update);
        confpass = findViewById(R.id.et_conf_password_update);
        subname = findViewById(R.id.et_subject_update);
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
                given_id = ids.getText().toString();
                if(given_id.equals(""))
                {
                    ids.setError("Required");
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    db.collection("Teachers_list").document(given_id).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful())
                                    {
                                        DocumentSnapshot doc = task.getResult();
                                        if(doc!=null)
                                        {
                                            given_name = doc.getString("Name");
                                            sub_name = doc.getString("Subject Name");
                                            given_pass = doc.getString("password");
                                            fetched_id = doc.getString("ID");

                                            name.setText(given_name);
                                            subname.setText(sub_name);
                                            pass.setText(given_pass);
                                            confpass.setText(given_pass);

                                           // ids.setFocusable(false);

//                                            name.setFocusable(true);
//                                            subname.setFocusable(true);
//                                            pass.setFocusable(true);
//                                            confpass.setFocusable(true);

                                            update_name = name.getText().toString();
                                            update_pass = pass.getText().toString();
                                            update_conf_pass = confpass.getText().toString();
                                            update_sub = subname.getText().toString();

                                            if(!(update_pass.equals(update_conf_pass)))
                                            {
                                                Toast.makeText(getApplicationContext(),"Password mismatch",Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                list.put("ID",given_id);
                                                list.put("Name",update_name);
                                                list.put("password",update_pass);
                                                list.put("Subject Name",update_sub);
                                                updatebtn.setEnabled(true);
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                        if(!(given_id.equals(fetched_id))){
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
                given_id = ids.getText().toString();
                if(given_id.equals(""))
                {
                    ids.setError("Required");
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    if(!(name.getText().toString().equals("")))
                    {
                        db.collection("Teachers_list").document(given_id)
                                .update(list).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.GONE);

                                ids.setFocusable(true);

                                name.setFocusable(false);
                                subname.setFocusable(false);
                                pass.setFocusable(false);
                                confpass.setFocusable(false);

                                ids.setText("");
                                name.setText("");
                                subname.setText("");
                                pass.setText("");
                                confpass.setText("");

                                updatebtn.setEnabled(false);
                                Toast.makeText(getApplicationContext(),"Teacher ID " + given_id + " Updated ",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Could not be updated",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
