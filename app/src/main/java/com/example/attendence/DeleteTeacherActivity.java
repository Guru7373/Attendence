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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteTeacherActivity extends AppCompatActivity {

    EditText t_id,t_name,t_subname;
    String given_id,fetched_name,fetched_sub,fetched_id;
    Button fetch,delete;
    ProgressBar mprogressBar;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_teacher);

        db = FirebaseFirestore.getInstance();
        t_id = findViewById(R.id.et_id_del);
        t_name = findViewById(R.id.et_name_del);
        t_subname = findViewById(R.id.et_subject_del);
        fetch = findViewById(R.id.btn_fetch);
        delete = findViewById(R.id.btn_delete);
        mprogressBar = findViewById(R.id.delete_progress);

        mprogressBar.setVisibility(View.GONE);

        delete.setEnabled(false);

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogressBar.setVisibility(View.VISIBLE);
                given_id = t_id.getText().toString();
                if(given_id.equals(""))
                {
                    t_id.setError("Required");
                    mprogressBar.setVisibility(View.GONE);
                }
                else
                {
                    mprogressBar.setVisibility(View.VISIBLE);
                    db.collection("Teachers_list").document(given_id).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful())
                                    {
                                        DocumentSnapshot doc = task.getResult();
                                        if(doc!=null)
                                        {
                                            fetched_name = doc.getString("Name");
                                            fetched_sub = doc.getString("Subject Name");
                                            fetched_id = doc.getString("ID");
                                            t_name.setText(fetched_name);
                                            t_subname.setText(fetched_sub);
                                            delete.setEnabled(true);
                                            mprogressBar.setVisibility(View.GONE);
                                        }
                                        if(!(given_id.equals(fetched_id))){
                                            delete.setEnabled(false);
                                            mprogressBar.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(),"No Record found",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                    });
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogressBar.setVisibility(View.VISIBLE);
                given_id = t_id.getText().toString();
                if(given_id.equals(""))
                {
                    t_id.setError("Required");
                    mprogressBar.setVisibility(View.GONE);
                }
                else
                {
                    mprogressBar.setVisibility(View.VISIBLE);
                    if(!(t_name.getText().toString().equals("")))
                    {
                        db.collection("Teachers_list").document(given_id)
                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void task) {
                                mprogressBar.setVisibility(View.GONE);
                                t_id.setText("");
                                t_name.setText("");
                                t_subname.setText("");
                                delete.setEnabled(false);
                                Toast.makeText(getApplicationContext(),"Teacher ID " + given_id + " Removed ",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Could not be deleted",Toast.LENGTH_LONG).show();
                    }
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
