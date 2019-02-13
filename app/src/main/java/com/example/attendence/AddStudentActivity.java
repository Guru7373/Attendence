package com.example.attendence;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddStudentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText stud_id,stud_name;
    Button save_btn;
    AppCompatSpinner sec_spinner;
    ArrayAdapter<String> aa;
    String studid,studname,section;
    String[] spinner_obj = {"Select Section","1CSE","2CSE","3CSE","4CSE","5CSE"};
    FirebaseFirestore db;
    Map<String,String> list = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        stud_id = findViewById(R.id.et_add_stud_id);
        stud_name = findViewById(R.id.et_add_stud_name);
        sec_spinner = findViewById(R.id.et_sec_spin);
        save_btn = findViewById(R.id.btn_add_stud);

        sec_spinner.setOnItemSelectedListener(this);

        aa = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,spinner_obj);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sec_spinner.setAdapter(aa);

        db = FirebaseFirestore.getInstance();

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studid = stud_id.getText().toString();
                studname = stud_name.getText().toString();
                if(section.equals("Select Section"))
                {
                    Toast.makeText(getApplicationContext(),"Select valid section",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(studid.equals("") || studname.equals(""))
                    {
                        stud_id.setError("Required");
                        stud_name.setError("Required");
                    }
                    else
                    {
                        list.put("Student ID",studid);
                        list.put("Student Name",studname);
                        list.put("Section",section);
                        db.collection("Students_list").document(section).collection(studid).document()
                                .set(list).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Student ID " + studid + "saved",Toast.LENGTH_SHORT).show();
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
}
