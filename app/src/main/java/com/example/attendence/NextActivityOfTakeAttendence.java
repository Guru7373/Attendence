package com.example.attendence;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NextActivityOfTakeAttendence extends AppCompatActivity {

    FirebaseFirestore db;
    Button save_btn;
    TextView tv;
    String[] p_id = new String[ArrayAdapterForTakeAttendence.model_list.size()];
    String[] p_name = new String[ArrayAdapterForTakeAttendence.model_list.size()];
    String[] present_status = new String[ArrayAdapterForTakeAttendence.model_list.size()];
    String[] absent_status = new String[ArrayAdapterForTakeAttendence.model_list.size()];
    String section,dates;
    PojoClass modelClass = new PojoClass();
    Map<String,String> list = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_of_take_attendence);

        tv = (TextView) findViewById(R.id.tv);
        save_btn = findViewById(R.id.final_save_btn);

        db = FirebaseFirestore.getInstance();

        for (int i = 0; i < ArrayAdapterForTakeAttendence.model_list.size(); i++){
            if(ArrayAdapterForTakeAttendence.model_list.get(i).getSelected())
            {
               // tv.setText(tv.getText() + " " + ArrayAdapterForTakeAttendence.model_list.get(i).getId_no()+ " "+ ArrayAdapterForTakeAttendence.model_list.get(i).getName()+ " PRESENT "+ "\n");
                p_id[i] = ArrayAdapterForTakeAttendence.model_list.get(i).getId_no();
                present_status[i] =  tv.getText() + " " + ArrayAdapterForTakeAttendence.model_list.get(i).getId_no()+ " "+ ArrayAdapterForTakeAttendence.model_list.get(i).getName()+ " PRESENT "+ "\n";
                tv.setText(present_status[i]);
            }
            if(!(ArrayAdapterForTakeAttendence.model_list.get(i).getSelected()))
            {
                absent_status[i] = tv.getText() + " " + ArrayAdapterForTakeAttendence.model_list.get(i).getId_no()+ " "+ ArrayAdapterForTakeAttendence.model_list.get(i).getName()+ " ABSENT "+ "\n";
                tv.setText(absent_status[i]);
                //tv.setText(tv.getText() + " " + ArrayAdapterForTakeAttendence.model_list.get(i).getId_no()+ " "+ ArrayAdapterForTakeAttendence.model_list.get(i).getName()+ " ABSENT "+"\n");
            }

        }

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        String dd = String.valueOf(today.monthDay);
        String mm = String.valueOf(today.month + 1);
        String yy = String.valueOf(today.year);
        dates = dd + "-" + mm + "-" + yy;

        final Map<String,String> final_p_date = new HashMap<>();
        final Map<String,String> final_p_id = new HashMap<>();

        String[] keys = new String[present_status.length];

        for(int i=0;i<present_status.length;i++){
            keys[i] = String.valueOf(i);
        }

        if(keys.length == present_status.length){
            for(int index = 0; index < keys.length; index++){
//                final_p_id.put(keys[index], p_id[index]);
                modelClass.setP_id(p_id[index]);
            }
        }

        if(keys.length == present_status.length){
            for(int index = 0; index < keys.length; index++){
//                final_p_date.put(keys[index], dates);
                modelClass.setP_name(p_name[index]);
            }
        }

//        if(keys.length == present_status.length){
//            for(int index = 0; index < keys.length; index++){
//                final_present_status.put(keys[index], present_status[index]);
//            }
//        }

        Intent intent = getIntent();
        section = intent.getStringExtra("section_selected");

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                db.collection("Attendence").document(section)
//                        .set(final_p_date).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
//                    }
//                });
                db.collection("Attendence").document(section)
                        .set(list).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}
