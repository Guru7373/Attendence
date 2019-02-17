package com.example.attendence;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class DeleteStudentActivity extends AppCompatActivity {

    EditText et_stud_id,et_stud_name,et_stud_sect;
    String student_id,student_name,student_section,sec;
    Button btn_fetch,btn_delete;
    ProgressBar progressBar;
    FirebaseFirestore db;
    List<String> mylist = new ArrayList<>();
    String[] secs = {"1CSE","2CSE","3CSE","4CSE","5CSE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_student);

        et_stud_id = findViewById(R.id.et_delete_stud__id);
        et_stud_name = findViewById(R.id.et_delete_stud_name);
        et_stud_sect = findViewById(R.id.et_delete_stud_sec);
        progressBar = findViewById(R.id.delete_stud_progress);
        btn_fetch = findViewById(R.id.btn_stud_fetch_delete);
        btn_delete = findViewById(R.id.btn_stud_delete);

        db = FirebaseFirestore.getInstance();

        btn_delete.setEnabled(false);

        btn_fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                student_id = et_stud_id.getText().toString().toUpperCase();
                progressBar.setVisibility(View.VISIBLE);
                if(student_id.equals(""))
                {
                    et_stud_id.setError("Required");
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                    call();
                }
            }

            private void call() {
                for (int i=0;i<secs.length;i++)
                {
                    CollectionReference cref = db.collection(secs[i]);
                    final int finalI = i;
                    cref.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (queryDocumentSnapshots != null) {
                                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                    mylist.add(doc.getId());
                                    if(doc.getId().equals(student_id))
                                    {
                                        sec = secs[finalI];
                                        if(sec != null)
                                        {
                                            rest_of_the_action();
                                        }
                                        else
                                        {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(),student_id + " Doesnot Exist",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    Log.d("My list data",mylist.toString());
                                    System.out.println(mylist);
                                }
                            }
                        }

                        private void rest_of_the_action() {
                            if(mylist.contains(student_id))
                            {
                                DocumentReference dref = db.collection(sec).document(student_id);
                                dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful())
                                        {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            if(documentSnapshot != null)
                                            {
                                                student_section = documentSnapshot.getString("Section");
                                                student_id = documentSnapshot.getString("Student ID");
                                                student_name = documentSnapshot.getString("Student Name");

                                                et_stud_id.setText(student_id);

                                                et_stud_name.setText(student_name);
                                                et_stud_sect.setText(student_section);

                                                btn_fetch.setEnabled(false);
                                                btn_delete.setEnabled(true);

                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if(!(student_section.equals("")))
                {
                    DocumentReference dref = db.collection(student_section).document(student_id);
                    dref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Student ID "+ student_id + " Deleted",Toast.LENGTH_SHORT).show();
                            et_stud_id.setText("");
                            et_stud_name.setText("");
                            et_stud_sect.setText("");

                            btn_fetch.setEnabled(true);
                            btn_delete.setEnabled(false);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Try again",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
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
