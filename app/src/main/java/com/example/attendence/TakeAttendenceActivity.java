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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.text.format.Time;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

public class TakeAttendenceActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    ListView student_list;
    ProgressBar progressBar;
    List<String> mylist = new ArrayList<>();
    int n;
    String section_selected = "";
    String[] Sections = {"1CSE", "2CSE", "3CSE", "4CSE", "5CSE"};
    ArrayList<ModelClass> modelArrayList = new ArrayList<>();
    TextView dates,proceed_click;
    Button btnselect, btndeselect,next_btn;
    ArrayAdapter<String> aa;
    ArrayAdapterForTakeAttendence arrayAdapterForTakeAttendence;
    FirebaseFirestore mFirestore;
    Intent in,t;
    String[] ids;
    String te_id;

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
        Intent intents = getIntent();
        te_id = intents.getStringExtra("take_id");

        in = new Intent(this,TChoiceActivity.class);
        in.putExtra("o_id",te_id);

        proceed_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    progressBar.setVisibility(View.VISIBLE);
                    ids = new String[mylist.size()];
                    for (int i = 0; i < mylist.size(); i++) {
                        ids = mylist.toArray(ids);
                    }
                    callprepared();
                }catch(Exception e){e.toString();}
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
                            startActivity(t);
                        }
                    });
            }
        });
    }

    private void callprepared() {
        n = ids.length;
        progressBar.setVisibility(View.GONE);
        modelArrayList = getModel(true);
        arrayAdapterForTakeAttendence = new ArrayAdapterForTakeAttendence(this,modelArrayList);
        student_list.setAdapter(arrayAdapterForTakeAttendence);
    }

    private ArrayList<ModelClass> getModel(boolean isSelect){
        ArrayList<ModelClass> list_array = new ArrayList<>();
        for(int i = 0; i < n; i++){

            ModelClass model = new ModelClass();
            model.setSelected(isSelect);
            model.setId_no(ids[i]);
            list_array.add(model);
        }
        return list_array;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        section_selected = Sections[position];



        t = new Intent(this,NextActivityOfTakeAttendence.class);
        t.putExtra("take_ID",te_id);
        t.putExtra("sec",section_selected);

        mylist.clear();

        CollectionReference cref = mFirestore.collection(section_selected);
        cref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        mylist.add(doc.getId());
                    }
                    System.out.println(mylist);
                }
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}
