package com.example.attendence;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class AddStudentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText stud_id,stud_name;
    Button save_btn;
    ProgressBar progressBar;
    AppCompatSpinner sec_spinner;
    ArrayAdapter<String> aa;
    String studid,studname,section;
    String fetched_id;
    String[] spinner_obj = {"Select Section","1CSE","2CSE","3CSE","4CSE","5CSE"};
    FirebaseFirestore db;
    DocumentReference dref;
    List<String> mylist = new ArrayList<>();
    Map<String,String> list = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        stud_id = findViewById(R.id.et_add_stud_id);
        stud_name = findViewById(R.id.et_add_stud_name);
        sec_spinner = findViewById(R.id.et_sec_spin);
        progressBar = findViewById(R.id.add_stud_progress);
        save_btn = findViewById(R.id.btn_add_stud);

        sec_spinner.setOnItemSelectedListener(this);

        aa = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, spinner_obj);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sec_spinner.setAdapter(aa);

        db = FirebaseFirestore.getInstance();

        for (int i = 1; i < spinner_obj.length; i++) {
            CollectionReference cref = db.collection(spinner_obj[i]);
            cref.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (queryDocumentSnapshots != null) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            mylist.add(doc.getId());
                            System.out.println(mylist);
                        }
                    }
                }
            });
        }

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studid = stud_id.getText().toString().toUpperCase();
                studname = stud_name.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if (studid.equals("")) {
                    stud_id.setError("Required");
                    progressBar.setVisibility(View.GONE);
                } else if (studname.equals("")) {
                    stud_name.setError("Required");
                    progressBar.setVisibility(View.GONE);
                } else if (section.equals("Select Section")) {
                    Toast.makeText(getApplicationContext(), "Select valid section", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else if (studid.length() != 10) {
                    Toast.makeText(getApplicationContext(), "Enter valid ID", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    list.put("Student ID", studid);
                    list.put("Student Name", studname);
                    list.put("Section", section);
                    if (mylist.contains(studid)) {
                        Toast.makeText(getApplicationContext(), "Student ID" + studid + " Already Exists", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        dref = db.collection(section).document(studid);
                        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot doc = task.getResult();
                                    if (doc != null) {
                                        fetched_id = doc.getString("Student ID");
                                        if (studid.equals(fetched_id)) {
                                            Toast.makeText(getApplicationContext(), "Student ID" + studid + " Already Exists", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            dref = db.collection(section).document(studid);
                                            dref.set(list).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getApplicationContext(), studid + " Saved successfully", Toast.LENGTH_LONG).show();
                                                    stud_name.setText("");
                                                    stud_id.setText("");
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            });
                                        }

                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        section = spinner_obj[position];
        parent.getSelectedItemPosition();
        ((TextView) sec_spinner.getSelectedView()).setTextColor(Color.BLACK);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),AChoiceActivity.class);
        startActivity(intent);
    }

}
