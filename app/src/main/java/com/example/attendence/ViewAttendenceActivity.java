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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

public class ViewAttendenceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView tv_id,tv_percentage,fetch;
    EditText date;
    AppCompatSpinner spinner;
    ArrayAdapter<String> aa;
    Intent in;
    String[] spinner_list = {"1CSE","2CSE","3CSE","4CSE","5CSE"};
    String[] out;
    String section,teacher_id;
    FirebaseFirestore db;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendence);

        tv_id = findViewById(R.id.id_of_student);
        tv_percentage = findViewById(R.id.percentage_of_student);
        date = findViewById(R.id.et_date);
        spinner = findViewById(R.id.sec_spin);
        fetch = findViewById(R.id.pro_fetch);
        db = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.vpgb);

        spinner.setOnItemSelectedListener(this);

        aa = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, spinner_list);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        Intent intent = getIntent();
        teacher_id = intent.getStringExtra("id");

        in = new Intent(this,TChoiceActivity.class);
        in.putExtra("t_id",teacher_id);

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String dt = date.getText().toString();
                tv_id.setText("");
                if(dt.equals(""))
                {
                    date.setError("dd-m-yyyy");
                    Toast.makeText(getApplicationContext(),"Enter Correct Date",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                        DocumentReference dref = db.collection("Attendence").document(teacher_id).collection(section).document(dt);
                        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    DocumentSnapshot doc = task.getResult();
                                    if(doc != null)
                                    {
                                        out = new String[15];
                                        for(int i=0;i<15;i++)
                                        {
                                            out[i] = doc.getString(String.valueOf(i));
                                        }
                                        for(String o : out)
                                        {
                                            if(o != null)
                                            {
                                                tv_id.append(o +"\n");
                                            }
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"No Record Found",Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"No Record Found",Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        section = spinner_list[position];
        parent.getSelectedItemPosition();
        ((TextView) spinner.getSelectedView()).setTextColor(Color.WHITE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        startActivity(in);
    }
}
