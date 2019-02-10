package com.example.attendence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.ColorSpace;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.text.format.Time;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TakeAttendenceActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    private static final String TAG = TakeAttendenceActivity.class.getName();
    ListView student_list;
    ProgressBar progressBar;
    List<String> serial = new ArrayList<String>();
    List<String> enroll = new ArrayList<String>();
    List<String> name = new ArrayList<String>();
    String[] sl_item;
    String[] id_item;
    String[] name_item;
    int n;
    String section_selected = "";
    String[] Sections = {"1CSE", "2CSE", "3CSE", "4CSE", "5CSE"};
    ArrayList<ModelClass> modelArrayList = new ArrayList<>();
    TextView dates,proceed_click;
    Button btnselect, btndeselect,next_btn;
    ArrayAdapter<String> aa;
    ArrayAdapterForTakeAttendence arrayAdapterForTakeAttendence;
    FirebaseFirestore mFirestore;
    Intent intent_section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendence);

        mFirestore = FirebaseFirestore.getInstance();

        dates = findViewById(R.id.date);
        progressBar = findViewById(R.id.progress);
        proceed_click = findViewById(R.id.proceed);
        btnselect = (Button) findViewById(R.id.select);
        btndeselect = (Button) findViewById(R.id.deselect);
        next_btn = findViewById(R.id.next);
        student_list = findViewById(R.id.students_list);

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        String dd = String.valueOf(today.monthDay);
        String mm = String.valueOf(today.month + 1);
        String yy = String.valueOf(today.year);
        dates.append(dd + "-" + mm + "-" + yy);

        AppCompatSpinner spin = findViewById(R.id.section_spinner);

        spin.setOnItemSelectedListener(this);
        aa = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, Sections);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        progressBar.setVisibility(View.GONE);

        proceed_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try {
                        progressBar.setVisibility(View.VISIBLE);
                        DocumentReference dref = mFirestore.collection("section_list").document(section_selected);
                        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot != null) {
                                        serial = (List<String>) task.getResult().get("sl_no");
                                        enroll = (List<String>) task.getResult().get("id_no");
                                        name = (List<String>) task.getResult().get("name");

                                        callprepared();

                                        intent_section = new Intent(getApplicationContext(),NextActivityOfTakeAttendence.class);
                                        intent_section.putExtra("section_selected",section_selected);
                                        
                                        progressBar.setVisibility(View.GONE);
                                    }
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "Please select valid section", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
            }
        });


        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelArrayList = getModel(true);
                arrayAdapterForTakeAttendence = new ArrayAdapterForTakeAttendence(TakeAttendenceActivity.this,modelArrayList);
                student_list.setAdapter(arrayAdapterForTakeAttendence);
            }
        });

        btndeselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelArrayList = getModel(false);
                arrayAdapterForTakeAttendence = new ArrayAdapterForTakeAttendence(TakeAttendenceActivity.this,modelArrayList);
                student_list.setAdapter(arrayAdapterForTakeAttendence);
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),NextActivityOfTakeAttendence.class);
                startActivity(intent);
                startActivity(intent_section);
            }
        });
    }

    private void callprepared() {
        sl_item = new String[serial.size()];
        id_item = new String[enroll.size()];
        name_item = new String[name.size()];

        sl_item = serial.toArray(sl_item);
        id_item = enroll.toArray(id_item);
        name_item = name.toArray(name_item);

        n = sl_item.length;

        modelArrayList = getModel(true);
        arrayAdapterForTakeAttendence = new ArrayAdapterForTakeAttendence(this,modelArrayList);
        student_list.setAdapter(arrayAdapterForTakeAttendence);
    }

    private ArrayList<ModelClass> getModel(boolean isSelect){
        ArrayList<ModelClass> list_array = new ArrayList<>();
        for(int i = 0; i < n; i++){

            ModelClass model = new ModelClass();
            model.setSelected(isSelect);
            model.setSl_no(sl_item[i]);
            model.setId_no(id_item[i]);
            model.setName(name_item[i]);
            list_array.add(model);
        }
        return list_array;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        section_selected = Sections[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
